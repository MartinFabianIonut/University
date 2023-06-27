package fx.controller;

import domain.*;
import javafx.scene.layout.AnchorPane;
import martin.service.IObserver;
import martin.service.IService;
import martin.service.MyException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ServiceController implements Initializable, IObserver {
    private Player player;
    private IService service;
    private Stage loginStage;

    @FXML
    TableView<DTO> allTableView, rankingTableView;
    ObservableList<DTO> observableList;
    @FXML
    AnchorPane anchorStart, anchorPlay;
    @FXML
    Button startButton, generateButton;
    @FXML
    Label control;
    @FXML
    TextField propose, proposeLetter;

    public ServiceController() {
        System.out.println("ServiceController created");
    }

    @FXML
    public void setEmployee(Player game) {
        this.player = game;
    }

    @FXML
    public void setService(IService service) throws MyException {
        this.service = service;
    }

    @FXML
    public void setStage(Stage stage) {
        this.loginStage = stage;
    }

    private void showAll() throws MyException {
        List<DTO> scores = (List<DTO>) this.service.getAllScores();
        if (scores == null) {
            return;
        }
        observableList = FXCollections.observableArrayList(scores);
        allTableView.setItems(observableList);
        allTableView.refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init() {
        anchorPlay.setVisible(false);
        anchorStart.setVisible(true);
    }

    public void update(Score score) {
        Platform.runLater(() -> {
            try {
                showAll();
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void updateRanking() {
        Platform.runLater(() -> {
            try {
                showRanking();
                generateButton.setDisable(true);
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void showRanking() throws MyException {
        rankingTableView.setVisible(true);
        List<DTO> scores = (List<DTO>) this.service.getRanking();
        if (scores == null) {
            return;
        }
        observableList = FXCollections.observableArrayList(scores);
        rankingTableView.setItems(observableList);
        rankingTableView.refresh();
    }

    public void logout() {
        try {
            service.logout(player, this);
            loginStage.show();
        } catch (MyException e) {
            System.out.println("Logout error " + e);
        }
    }

    public void logoutAction() {
        logout();
        Stage stage = (Stage) this.allTableView.getScene().getWindow();
        stage.close();
    }

    public void start() throws MyException {
        if (propose.getText().equals("")) {
            control.setText("Control: Propose a word!");
            return;
        }
        if (propose.getText().length() < 6) {
            control.setText("Control: Word must have at least 6 letters!");
            return;
        }
        String word = propose.getText();
        Game game = new Game(0, player,null,null, word, null, null);
        boolean begin = service.startGame(game);
        if (begin) {
            anchorPlay.setVisible(true);
            rankingTableView.setVisible(false);
            anchorStart.setVisible(false);
            showAll();
        }
    }

    public void generate() {
        try{
            if (allTableView.getSelectionModel().getSelectedItem() == null) {
                control.setText("Control: Select a row from the table!");
                return;
            }
            if (proposeLetter.getText().equals("") || proposeLetter.getText().length() > 1) {
                control.setText("Control: Enter a letter!");
                return;
            }
            if(Objects.equals(allTableView.getSelectionModel().getSelectedItem().getPlayer(),player)) {
                control.setText("Control: You cannot propose for yourself!");
                return;
            }
            DTO dto = allTableView.getSelectionModel().getSelectedItem();
            Score score = new Score(0, service.getGame(), dto.getPlayer(), player, 0, 0, proposeLetter.getText());
            service.generate(score);
        } catch (MyException e) {
            control.setText("Control: " + e.getMessage());
        }
    }
}
