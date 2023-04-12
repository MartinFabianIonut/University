package com.example.temasaptamana5.controller;

import com.example.temasaptamana5.HelloApplication;
import com.example.temasaptamana5.domain.Employee;
import com.example.temasaptamana5.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private Service service;

    @FXML
    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    private void loginAction() throws IOException {
        if (usernameField.getText().length() > 0 && passwordField.getText().length() > 0) {
            try {
                Employee employee = service.authenticateEmployee(usernameField.getText(), passwordField.getText());
                if (employee != null) {
                    FXMLLoader employeeLoader = new FXMLLoader();
                    employeeLoader.setLocation(HelloApplication.class.getResource("main_stage.fxml"));
                    Stage employeeStage = new Stage();
                    Scene employeeScene = new Scene(employeeLoader.load(), 1200, 800);
                    ServiceController serviceController = employeeLoader.getController();
                    serviceController.setEmployee(employee);
                    serviceController.setService(service);

                    employeeStage.setTitle("Employee: " + employee);
                    employeeStage.setScene(employeeScene);
                    employeeStage.show();
                    controlLabel.setText("");
                }
                else
                    controlLabel.setText("Error: no such employee!");
            } catch (IllegalArgumentException e) {
                controlLabel.setText("Error: no such employee!");
            }
            usernameField.clear();
            passwordField.clear();
        } else
            controlLabel.setText("One of the fields is empty!");
    }
}
