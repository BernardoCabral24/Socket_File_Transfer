package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static final int PORT = 6666;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void switchScene(MouseEvent event, String path){
        try {
            Parent root1 = FXMLLoader.load(Main.class.getResource(path));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root1);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) throws Exception {
        List<Thread> threadList= new ArrayList<>();
        //System.out.println(ran.getFileBytes("test.gif")+"--->bytes_file");
        ServerSocket serverSocket= new ServerSocket(PORT);
        System.out.println("[+]Server Started");
        //Thread x = new Thread(new Client("localhost",PORT));
        //x.start();
        while(true){
            Socket connection = serverSocket.accept();
            Thread t = new Thread(new Server(connection));
            t.start();
        }


        //server.sendCommand("shutdown");

        //launch(args);
    }
}
