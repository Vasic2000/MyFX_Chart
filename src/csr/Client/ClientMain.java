package csr.Client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientMain extends Application implements Cloneable {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MyFXClient.fxml"));
        primaryStage.setTitle("Chat 2k19");
        Scene scene = new Scene(root, 275, 375);
        primaryStage.setScene(scene);
        primaryStage.show();


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
            System.out.println("Stage is closing");
            System.exit(0);
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
