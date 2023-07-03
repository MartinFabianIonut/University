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

import java.lang.reflect.InvocationTargetException;
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
    TextField firstProp, secondProp, thirdProp, proposePosition;

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
                proposePosition.setDisable(true);
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
        if (firstProp.getText().equals("") || secondProp.getText().equals("") || thirdProp.getText().equals("")) {
            control.setText("Control: Propose 3 positions!");
            return;
        }
        try {
            int poz1 = Integer.parseInt(firstProp.getText());
            int poz2 = Integer.parseInt(secondProp.getText());
            int poz3 = Integer.parseInt(thirdProp.getText());
            if (poz1 < 1 || poz1 > 9 || poz2 < 1 || poz2 > 9 || poz3 < 1 || poz3 > 9) {
                control.setText("Control: Positions must be between 1 and 9!");
                return;
            }
            if (poz1 == poz2 || poz1 == poz3 || poz2 == poz3) {
                control.setText("Control: Positions must be different!");
                return;
            }
            poz1--; poz2--; poz3--;
            // create string of lenght 9 with 0 on all positions and 1 on positions poz1, poz2, poz3
            String cuvant = "000000000";
            StringBuilder sb = new StringBuilder(cuvant);
            sb.setCharAt(poz1, '1');
            sb.setCharAt(poz2, '1');
            sb.setCharAt(poz3, '1');
            cuvant = sb.toString();
            Game game = new Game(0, player, null, null, cuvant, null, null);
            boolean begin = service.startGame(game);
            if (begin) {
                anchorPlay.setVisible(true);
                rankingTableView.setVisible(false);
                anchorStart.setVisible(false);
                showAll();
            }
        } catch (NumberFormatException e) {
            control.setText("Control: Insert 3 numbers!");
        }
    }

    public void generate() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        try {
            if (allTableView.getSelectionModel().getSelectedItem() == null) {
                control.setText("Control: Select one row!");
                return;
            }
            if (proposePosition.getText().equals("") || proposePosition.getText().length() > 1) {
                control.setText("Control: Insert one number between 1 and 9!");
                return;
            }
            if (Objects.equals(allTableView.getSelectionModel().getSelectedItem().getPlayer(), player)) {
                control.setText("Control: You can't generate for yourself!");
                return;
            }
            int poz = Integer.parseInt(proposePosition.getText());
            DTO dto = allTableView.getSelectionModel().getSelectedItem();
            Score score = new Score(0, service.getGame(), dto.getPlayer(), player, 0, 0, poz - 1);
            service.generate(score);
        } catch (MyException | NumberFormatException e) {
            control.setText("Control: " + e.getMessage());
        }

    }
}
