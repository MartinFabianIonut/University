package com.example.temasaptamana5.controller;

import com.example.temasaptamana5.HelloApplication;
import com.example.temasaptamana5.domain.Artist;
import com.example.temasaptamana5.domain.Employee;
import com.example.temasaptamana5.domain.ShowDTO;
import com.example.temasaptamana5.service.Service;
import com.example.temasaptamana5.utils.observer.Observer;
import com.example.temasaptamana5.utils.utils.ActualEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ServiceController implements Observer<ActualEvent> {

    private Employee employee;
    private Service service;

    @FXML
    TableView<ShowDTO> allShowsTableView;
    @FXML
    TableView<ShowDTO>filteredShowsTableView;
    ObservableList<ShowDTO> showsObservableList;
    @FXML
    TableColumn<ShowDTO, String> select;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox<String> artistsCombobox;

    @FXML
    public void setEmployee(Employee employee){
        this.employee = employee;
    }

    @FXML
    public void setService(Service service){
        this.service = service;
        this.service.addObservers(this);
        showAllShows();
        for(Artist artist: service.getAllArtists())
            artistsCombobox.getItems().add(artist.toString());
        initialiseTable();
    }

    private void showAllShows() {
        List<ShowDTO> showDTOS = (List<ShowDTO>) this.service.getAllShowsDTO();
        showsObservableList = FXCollections.observableArrayList(showDTOS);
        allShowsTableView.setItems(showsObservableList);
        allShowsTableView.refresh();
    }

    private void initialiseTable(){
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
                            ShowDTO showDTO =getTableView().getItems().get(getIndex());
                            buyingButton.setDisable(showDTO.getAvailable()==0);
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

    @Override
    public void update(ActualEvent event) {
        showAllShows();
    }

    public void showFilteredShows() {
        if(artistsCombobox.getSelectionModel().getSelectedItem()!=null && datePicker.getValue()!=null) {
            String artist = artistsCombobox.getSelectionModel().getSelectedItem();
            List<ShowDTO> showDTOS = (List<ShowDTO>) this.service.getAllShowsDTO();
            List<ShowDTO> filteredShows = showDTOS.stream().filter(showDTO ->
                            Objects.equals(service.findArtist(showDTO.getIdArtist()).toString(), artist)
                            && showDTO.getDate().compareTo(datePicker.getValue()) == 0)
                    .toList();
            showsObservableList = FXCollections.observableArrayList(filteredShows);
            filteredShowsTableView.setItems(showsObservableList);
            filteredShowsTableView.refresh();
        }
    }
}
