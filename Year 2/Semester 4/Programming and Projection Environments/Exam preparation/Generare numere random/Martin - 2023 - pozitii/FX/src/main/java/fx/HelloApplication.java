package fx;

import martin.network.rpcprotocol.ServicesRpcProxy;
import martin.service.IService;
import fx.controller.LoginController;
import fx.controller.ServiceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(HelloApplication.class.getResourceAsStream("/fx.properties"));
            System.out.println("Game properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find fx.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IService service = new ServicesRpcProxy(serverIP, serverPort);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service);

        FXMLLoader cloader = new FXMLLoader(getClass().getResource("main_stage.fxml"));
        Parent parent=cloader.load();
        ServiceController serviceController = cloader.getController();
        serviceController.setService(service);
        loginController.setServiceController(serviceController);
        loginController.setParent(parent);

        primaryStage.setTitle("MPP server-client app");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}