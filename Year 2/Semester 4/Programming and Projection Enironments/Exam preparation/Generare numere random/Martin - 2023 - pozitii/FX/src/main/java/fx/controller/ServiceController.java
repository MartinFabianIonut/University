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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import martin.service.IObserver;
import martin.service.IService;
import martin.service.MyException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ServiceController implements Initializable, IObserver {
    private Player player;
    private IService service;
    private Stage loginStage;

    @FXML
    TableView<DTO> rankingTableView;
    ObservableList<DTO> observableList;
    @FXML
    Label control;
    @FXML
    GridPane gridPane, gridPanePlayer;
    @FXML
    Button genereaza;

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

    public void init() throws MyException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        showRanking();
        Game game = service.getGame(player);
        Method myMethod;
        for (int i = 0; i < 5; i++) {
            Label label = new Label();
            int j = i + 1;
            String method = "getValue" + j;
            myMethod = game.getClass().getMethod(method);
            assert false;
            Integer value = (Integer) myMethod.invoke(game);
            label.setText(value.toString());
            gridPane.add(label, i, 0);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, javafx.geometry.VPos.CENTER);
        }
        for (int i = 0; i < 5; i++) {
            Label label = new Label();
            label.setText("");
            gridPanePlayer.add(label, i, 0);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, javafx.geometry.VPos.CENTER);
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
                control.setText(service.setControlGameOver(player));
                genereaza.setDisable(true);
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

    public void genereazaAction() {
        try {
            Integer position = service.generate(player);
            if (position != null) {
                for (Node node : gridPanePlayer.getChildren()) {
                    if (node instanceof Label) {
                        Integer columnIndex = GridPane.getColumnIndex(node);
                        Integer rowIndex = GridPane.getRowIndex(node);
                        if (rowIndex == 0 && columnIndex.equals(position-1)) {
                            ((Label) node).setText("x");
                        } else {
                            ((Label) node).setText("");
                        }
                    }
                }
            }
            control.setText("Control: esti pe pozitia = " + position);
        } catch (MyException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.out.println("Genereaza error " + e);
        }
    }

}
