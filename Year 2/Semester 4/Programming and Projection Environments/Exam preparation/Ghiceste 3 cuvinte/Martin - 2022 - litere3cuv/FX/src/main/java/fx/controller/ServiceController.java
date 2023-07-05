package fx.controller;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import martin.service.IObserver;
import martin.service.IService;
import martin.service.MyException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceController implements Initializable, IObserver {
    private Player player;
    private IService service;
    private Stage loginStage;

    @FXML
    TableView<DTO> rankingTableView;
    ObservableList<DTO> observableList;
    @FXML
    AnchorPane anchorStart;
    @FXML
    Label control, gameOver;
    @FXML
    GridPane gridPane;
    @FXML
    TextField wordTextField;
    @FXML
    Button button;

    public ServiceController() {
        System.out.println("ServiceController created");
    }

    @FXML
    public void setEmployee(Player game) {
        this.player = game;
    }

    @FXML
    public void setService(IService service) {
        this.service = service;
    }

    @FXML
    public void setStage(Stage stage) {
        this.loginStage = stage;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init() throws MyException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        showRanking();
        Game game = service.getGame(player);
        String letters = game.getLetters();
        int length = letters.length();
        for (int col = 0; col < length; col++) {
            Label label = new Label(letters.charAt(col) + "");
            gridPane.add(label, col, 0);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, javafx.geometry.VPos.CENTER);
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / length);
            gridPane.getColumnConstraints().add(columnConstraints);
        }

    }

    @Override
    public void updateRanking() {
        Platform.runLater(() -> {
            try {
                showRanking();
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void updateControl(Player player) throws MyException {
        Platform.runLater(() -> {
            try {
                gameOver.setText(service.setControlGameOver(player));
                button.setDisable(true);
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void showRanking() throws MyException {
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

    public void buttonAction() throws MyException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (wordTextField.getText().equals("")) {
            return;
        }
        control.setText(service.guess(new Pair(player, wordTextField.getText())));
    }
}
