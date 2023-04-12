package com.example.temasaptamana5;

import com.example.temasaptamana5.controller.LoginController;
import com.example.temasaptamana5.domain.Artist;
import com.example.temasaptamana5.domain.Employee;
import com.example.temasaptamana5.domain.Show;
import com.example.temasaptamana5.domain.Ticket;
import com.example.temasaptamana5.repository.*;
import com.example.temasaptamana5.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties properties=new Properties();
        try {
            properties.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find db.config "+e);
        }
        IRepository<Integer, Artist> artistDBRepository = new ArtistDBIRepository(properties);
        IRepository<Integer, Employee> employeeDBRepository = new EmployeeDBIRepository(properties);
        IRepository<Integer, Show> showDBRepository = new ShowDBIRepository(properties);
        IRepository<Integer, Ticket> ticketDBRepository = new TicketDBIRepository(properties);
        Service service = new Service(artistDBRepository,employeeDBRepository,showDBRepository,ticketDBRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}