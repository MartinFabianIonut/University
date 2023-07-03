package employee.controller;

import gRPCProject.domain.Artist;
import gRPCProject.domain.Employee;
import gRPCProject.domain.ShowDTO;
import gRPCProject.domain.Ticket;
import gRPCProject.service.IObserver;
import gRPCProject.service.IService;
import gRPCProject.service.MyException;
import employee.HelloApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ServiceController implements Initializable, IObserver {

    private Employee employee;
    private IService service;
    private Stage loginStage;

    @FXML
    TableView<ShowDTO> allShowsTableView;
    @FXML
    TableView<ShowDTO> filteredShowsTableView;
    ObservableList<ShowDTO> showsObservableList;
    @FXML
    TableColumn<ShowDTO, String> select;
    @FXML
    DatePicker datePicker;


    public ServiceController() {
        System.out.println("ServiceController created");
    }

    @FXML
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @FXML
    public void setService(IService service) throws MyException {
        this.service = service;
    }

    @FXML
    public void setStage(Stage stage) {
        this.loginStage = stage;
    }

    private void showAllShows() throws MyException {
        List<ShowDTO> showDTOS = (List<ShowDTO>) this.service.getAllShowsDTO();
        showsObservableList = FXCollections.observableArrayList(showDTOS);
        allShowsTableView.setItems(showsObservableList);
        allShowsTableView.refresh();
    }

    private void initialiseTable() {
        select.setCellFactory(new Callback<>() {
            @Override
            public TableCell call(final TableColumn<ShowDTO, String> param) {
                return new TableCell<ShowDTO, String>() {
                    final Button buyingButton = new Button("Buy ticket(s)!");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            ShowDTO showDTO = getTableView().getItems().get(getIndex());
                            buyingButton.setDisable(showDTO.getAvailable() == 0);
                            buyingButton.setOnAction(event -> {
                                FXMLLoader buyingLoader = new FXMLLoader();
                                buyingLoader.setLocation(HelloApplication.class.getResource("buy.fxml"));
                                Stage buyingStage = new Stage();
                                Scene buyingScene;
                                try {
                                    buyingScene = new Scene(buyingLoader.load(), 300, 200);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                BuyingController buyingController = buyingLoader.getController();
                                buyingController.setService(service);
                                buyingController.setShow(showDTO);

                                buyingStage.setTitle("Buy a ticket to the concert: " + showDTO.getTitle());
                                buyingStage.setScene(buyingScene);
                                buyingStage.show();
                            });
                            setGraphic(buyingButton);
                            setText(null);
                        }
                    }
                };
            }
        });
        allShowsTableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ShowDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null)
                    setStyle("");
                else if (item.getAvailable() != 0)
                    setStyle("-fx-background-color: #baffba;");
                else if (item.getAvailable() == 0)
                    setStyle("-fx-background-color: #f5b2aa;");
                else
                    setStyle("");
            }
        });
    }

    public void showFilteredShows() throws MyException {
        if (datePicker.getValue() != null) {
            List<ShowDTO> showDTOS = (List<ShowDTO>) this.service.getAllShowsDTO();
            List<ShowDTO> filteredShows = showDTOS.stream().filter(showDTO ->
                    showDTO.getDate().compareTo(datePicker.getValue()) == 0)
                    .toList();
            showsObservableList = FXCollections.observableArrayList(filteredShows);
            filteredShowsTableView.setItems(showsObservableList);
            filteredShowsTableView.refresh();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (employee != null) {
            try {
                showAllShows();
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
            initialiseTable();
        }
    }

    public void init() {
        try {
            showAllShows();
        } catch (MyException e) {
            throw new RuntimeException(e);
        }
        initialiseTable();
    }

    public void ticketAdded(Ticket ticket) throws MyException {
        Platform.runLater(() -> {
            try {
                showAllShows();
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void logout() {
        try {
            service.logout(employee, this);
            loginStage.show();
        } catch (MyException e) {
            System.out.println("Logout error " + e);
        }
    }

    public void logoutAction() {
        logout();
        Stage stage = (Stage) this.allShowsTableView.getScene().getWindow();
        stage.close();
    }

}
