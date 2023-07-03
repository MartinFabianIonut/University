package employee.controller;

import SeventhWeekServerClient.domain.Employee;
import SeventhWeekServerClient.service.IService;
import SeventhWeekServerClient.service.MyException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button loginButton;
    @FXML
    Label controlLabel;
    private IService service;
    private ServiceController serviceController;
    private Parent mainParent;
    private final Stage employeeStage = new Stage();

    @FXML
    public void setService(IService service) {
        this.service = service;
    }
    public void setServiceController(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    public void setParent(Parent parent) {
        this.mainParent = parent;
    }

    @FXML
    private void loginAction(ActionEvent actionEvent) throws IOException {
        if (usernameField.getText().length() > 0 && passwordField.getText().length() > 0) {
            try {
                Employee trying = new Employee(0,null,null,usernameField.getText(), passwordField.getText());
                Employee employee = service.authenticateEmployee(trying, serviceController);
                if (employee != null) {
                    employeeStage.setOnCloseRequest(event -> {
                        serviceController.logout();
                    });
                    employeeStage.setTitle("Employee: " + employee);
                    if (employeeStage.getScene()==null)
                        employeeStage.setScene(new Scene(mainParent, 1200, 800));
                    employeeStage.show();
                    serviceController.setEmployee(employee);
                    serviceController.setStage((Stage) usernameField.getScene().getWindow());
                    serviceController.init();
                    controlLabel.setText("");
                    Stage stage = (Stage) ((Node)(actionEvent.getSource())).getScene().getWindow();
                    stage.close();
                }
                else
                    controlLabel.setText("Error: no such employee!");
            } catch (IllegalArgumentException | MyException e) {
                controlLabel.setText("Error: no such employee!");
            }
            usernameField.clear();
            passwordField.clear();
        } else
            controlLabel.setText("One of the fields is empty!");
    }
}
