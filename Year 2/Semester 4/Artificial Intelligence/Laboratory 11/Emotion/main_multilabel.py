import os

import cv2
import numpy as np
import pandas as pd
import pytorch_lightning as pl
import timm
import torch
import torch.nn as nn
import torch.nn.functional as F
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split
from torch import tensor
from torch.utils.data import DataLoader


class ImageDataset(torch.utils.data.Dataset):

    def __init__(self, df, transforms=None):
        self.df = df
        self.transforms = transforms

    def __len__(self):
        return len(self.df)

    def __getitem__(self, idx):
        img = cv2.imread(self.df.iloc[idx, 0])
        label = self.df.iloc[idx, 2:]
        if self.transforms:
            return self.transforms(torch.Tensor(img).permute([2, 1, 0])), torch.Tensor([label])
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        img = img / 255.
        img = cv2.resize(img, (384, 384))
        return torch.Tensor(img).permute([2, 1, 0]), torch.Tensor(label)


class MLCNNet(nn.Module):

    def __init__(self, backbone, n_classes):
        super(MLCNNet, self).__init__();
        self.model = backbone
        self.classifier = nn.Sequential(nn.Linear(2048, 256),
                                        nn.ReLU(),
                                        nn.Linear(256, n_classes))

    def forward(self, x):
        x = self.model(x)
        x = self.classifier(x)
        return x


class LitMLCNet(pl.LightningModule):

    def __init__(self, model):
        super().__init__();
        self.model = model

    def training_step(self, batch, batch_idx):
        x, y = batch
        outputs = self.model(x)
        loss = F.binary_cross_entropy_with_logits(outputs, y)
        self.log("train/loss", loss.item() / len(y))
        return loss

    def validation_step(self, batch, batch_idx):
        x, y = batch
        outputs = self.model(x)
        loss = F.binary_cross_entropy_with_logits(outputs, y)
        self.log("val/loss", loss.item() / len(y))

    def predict_step(self, batch, batch_idx, dataloader_idx=0):
        x, y = batch
        preds = self.model(x)
        return preds, y

    def configure_optimizers(self):
        optim = torch.optim.AdamW(self.parameters(), lr=8e-5)
        return optim


if __name__ == '__main__':
    train_dir = "multi-label/images"
    train_csv_path = "multi-label/multilabel_classification(6)-reduced_modified.csv"
    train_df = pd.read_csv(train_csv_path)
    train_df["Image_Name"] = train_df["Image_Name"].map(lambda x: os.path.join(train_dir, x))
    # keep only 30% of the data

    train_df = train_df.sample(frac=0.1, random_state=21)

    train_set, dev_set = train_test_split(train_df, test_size=0.3, random_state=21)
    val_set, test_set = train_test_split(dev_set, test_size=0.3, random_state=21)
    train_dataset = ImageDataset(train_set)
    val_dataset = ImageDataset(val_set)
    test_dataset = ImageDataset(test_set)
    train_dataloader = torch.utils.data.DataLoader(train_dataset,batch_size=16,shuffle=True)
    val_dataloader = torch.utils.data.DataLoader(val_dataset,batch_size=16,shuffle=False)
    test_dataloader = torch.utils.data.DataLoader(test_dataset,batch_size=1,shuffle=False)
    timm.list_models("resnet*")
    backbone = timm.create_model("resnetv2_50", pretrained=True, num_classes=0)
    Model = MLCNNet(backbone, 10)
    pl_Model  = LitMLCNet(Model)
    trainer = pl.Trainer(default_root_dir='./',
                         max_epochs=10,
                         log_every_n_steps=5,
                         accelerator='gpu',
                         devices=1,
                         )
    trainer.fit(pl_Model , train_dataloader, val_dataloader)
    preds_labels = trainer.predict(pl_Model,test_dataloader)

    preds,labels =[],[]
    for item in preds_labels:
        preds.append(torch.round(torch.sigmoid(item[0][0])).detach().numpy().tolist())
        labels.append(item[1][0].detach().numpy().tolist())
    print(f"test_accuracy  - {accuracy_score(preds, labels)*100}")
