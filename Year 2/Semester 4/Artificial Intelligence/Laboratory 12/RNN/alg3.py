import torch
import torch.nn as nn
import torch.optim as optim
import torchvision
import torchvision.datasets as datasets
import torchvision.transforms as transforms
from torch.utils.data import DataLoader
from torch.utils.tensorboard import SummaryWriter


# Discriminator Code - functionally same as DCGAN discriminator
# DCGAN = Deep Convolutional Generative Adversarial Network
class Discriminator(nn.Module):
    def __init__(self, channels_img, features_d):
        super(Discriminator, self).__init__()
        self.disc = nn.Sequential(
            nn.Conv2d(channels_img, features_d, kernel_size=4, stride=2, padding=1),
            nn.LeakyReLU(0.2),
            self._block(features_d, features_d * 2, 4, 2, 1), # img: 16x16
            self._block(features_d * 2, features_d * 4, 4, 2, 1), # img: 8x8
            self._block(features_d * 4, features_d * 8, 4, 2, 1), # img: 4x4
            nn.Conv2d(features_d * 8, 1, kernel_size=4, stride=2, padding=0), # img: 1x1
            nn.Sigmoid(),
        )

    def _block(self, in_channels, out_channels, kernel_size, stride, padding):
        return nn.Sequential(
            nn.Conv2d(in_channels, out_channels, kernel_size, stride, padding, bias=False),
            nn.LeakyReLU(0.2),
        )

    def forward(self, x):
        return self.disc(x)


# Generator Code - functionally same as DCGAN generator
# DCGAN = Deep Convolutional Generative Adversarial Network
# Paper: https://arxiv.org/pdf/1511.06434.pdf
#
class Generator(nn.Module):
    def __init__(self, channels_noise, channels_img, features_g):
        super(Generator, self).__init__()
        self.net = nn.Sequential(
            self._block(channels_noise, features_g * 16, 4, 1, 0),  # img: 4x4
            self._block(features_g * 16, features_g * 8, 4, 2, 1),  # img: 8x8
            self._block(features_g * 8, features_g * 4, 4, 2, 1),  # img: 16x16
            self._block(features_g * 4, features_g * 2, 4, 2, 1),  # img: 32x32
            nn.ConvTranspose2d(features_g * 2, channels_img, kernel_size=4, stride=2, padding=1),
            nn.Tanh(),
        )

    def _block(self, in_channels, out_channels, kernel_size, stride, padding):
        return nn.Sequential(
            nn.ConvTranspose2d(in_channels, out_channels, kernel_size, stride, padding, bias=False),
            nn.ReLU(),
        )

    def forward(self, x):
        return self.net(x)


def initialize_weights(model):
    # Initializes weights according to the DCGAN paper
    for m in model.modules():
        if isinstance(m, (nn.Conv2d, nn.ConvTranspose2d, nn.BatchNorm2d)):
            nn.init.normal_(m.weight.data, 0.0, 0.02)


if __name__ == "__main__":
    # Hyperparameters etc.
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    learning_rate = 2e-4  # could also use two lrs, one for gen and one for disc
    # batch_size set to 128 for GPU training
    batch_size = 256
    image_size = 64
    # channels_img set to 3 for RGB images (3 channels)
    channels_img = 3
    # noise_dim set to 100 for 100 random numbers
    noise_dim = 100
    # num_epochs set to 5 for 5 epochs
    num_epochs = 5
    # features_disc set to 64 for 64 features (same as generator)
    features_disc = 64
    # features_gen set to 64 for 64 features (same as discriminator)
    features_gen = 64

    # Transforms for dataset preprocessing
    transform = transforms.Compose(
        [
            transforms.Resize(image_size),
            transforms.ToTensor(),
            transforms.Normalize(
                mean=[0.5 for _ in range(channels_img)],
                std=[0.5 for _ in range(channels_img)]
            ),
        ]
    )

    # Load the dataset
    dataset = datasets.ImageFolder(root="celeb_dataset", transform=transform)

    # Create a dataloader
    dataloader = DataLoader(dataset, batch_size=batch_size, shuffle=True)

    # Initialize the generator and discriminator
    generator = Generator(noise_dim, channels_img, features_gen).to(device)
    discriminator = Discriminator(channels_img, features_disc).to(device)
    initialize_weights(generator)
    initialize_weights(discriminator)

    # Define the optimizers and loss criterion
    optimizer_generator = optim.Adam(generator.parameters(), lr=learning_rate, betas=(0.5, 0.999))
    optimizer_discriminator = optim.Adam(discriminator.parameters(), lr=learning_rate, betas=(0.5, 0.999))
    criterion = nn.BCELoss()

    # Generate fixed noise for visualization during training
    fixed_noise = torch.randn(32, noise_dim, 1, 1).to(device)

    # Initialize TensorBoard writers
    writer_real = SummaryWriter(f"logs/real")
    writer_fake = SummaryWriter(f"logs/fake")

    step = 0

    # Set the models in training mode
    generator.train()
    discriminator.train()

    # Training loop
    for epoch in range(num_epochs):
        for batch_idx, (real_images, _) in enumerate(dataloader):
            real_images = real_images.to(device)
            noise = torch.randn(batch_size, noise_dim, 1, 1).to(device)

            # ---------------------
            # Train Discriminator
            # ---------------------

            fake_images = generator(noise)

            # Compute discriminator outputs for real and fake images
            discriminator_real = discriminator(real_images).reshape(-1)
            discriminator_fake = discriminator(fake_images.detach()).reshape(-1)

            # Compute discriminator loss
            loss_discriminator_real = criterion(discriminator_real, torch.ones_like(discriminator_real))
            loss_discriminator_fake = criterion(discriminator_fake, torch.zeros_like(discriminator_fake))
            loss_discriminator = (loss_discriminator_real + loss_discriminator_fake) / 2

            # Backpropagation and optimizer step for the discriminator
            discriminator.zero_grad()
            loss_discriminator.backward()
            optimizer_discriminator.step()

            # -----------------
            # Train Generator
            # -----------------

            fake_images = generator(noise)
            discriminator_fake = discriminator(fake_images).reshape(-1)

            # Compute generator loss
            loss_generator = criterion(discriminator_fake, torch.ones_like(discriminator_fake))

            # Backpropagation and optimizer step for the generator
            generator.zero_grad()
            loss_generator.backward()
            optimizer_generator.step()

            # Print losses occasionally and write to TensorBoard
            if batch_idx % 100 == 0:
                print(
                    f"Epoch [{epoch}/{num_epochs}] Batch {batch_idx}/{len(dataloader)} "
                    f"Loss D: {loss_discriminator:.7f}, Loss G: {loss_generator:.7f}"
                )

                with torch.no_grad():
                    fake_images = generator(fixed_noise)
                    image_grid_real = torchvision.utils.make_grid(real_images[:32], normalize=True)
                    image_grid_fake = torchvision.utils.make_grid(fake_images[:32], normalize=True)

                    writer_real.add_image("Real Images", image_grid_real, global_step=step)
                    writer_fake.add_image("Fake Images", image_grid_fake, global_step=step)

                step += 1

    # Close the TensorBoard writers
    writer_real.close()
    writer_fake.close()
