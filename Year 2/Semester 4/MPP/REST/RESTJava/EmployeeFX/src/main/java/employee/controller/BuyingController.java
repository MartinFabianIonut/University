package employee.controller;
import rest.domain.ShowDTO;
import rest.domain.Ticket;
import rest.service.IService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class BuyingController {
    @FXML
    TextField nameField, seatField;
    @FXML
    Button buyingButton;
    private IService service;
    private ShowDTO show;

    @FXML
    public void setService(IService service) {
        this.service = service;
    }

    @FXML
    public void setShow(ShowDTO show) {
        this.show = show;
    }

    @FXML
    public void buyingAction(ActionEvent actionEvent) {
        String error = "";
        if (nameField.getText().length() > 0) {
            if (seatField.getText().length() > 0) {
                try {
                    int noOfSeats = Integer.parseInt(seatField.getText());
                    if (show.getAvailable() >= noOfSeats) {
                        for (int i = 1; i <= noOfSeats; i++)
                            service.addTickets(new Ticket(1, show.getId(), nameField.getText(), noOfSeats));
                        this.buyingButton.getScene().getWindow().hide();
                    }
                    else error += "There are not enough available seats!\n";
                } catch (Exception e) {
                    error += "The input from the number of seats is not an integer!\n";
                }
            } else error += "No number of seats!\n";
        } else error += "No name for the costumer!\n";
        if (error.length() > 0) {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setHeaderText("Error message");
            message.setContentText("Error:\n" + error);
            message.initOwner(nameField.getScene().getWindow());
            message.showAndWait();
        }
    }
}
