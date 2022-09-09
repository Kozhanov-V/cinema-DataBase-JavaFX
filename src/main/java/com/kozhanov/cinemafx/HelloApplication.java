package com.kozhanov.cinemafx;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        HelloApplication.primaryStage = primaryStage;
    }

    static private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        setPrimaryStage(primaryStage);
        Group group = new Group();
        Parent content = FXMLLoader.load(getClass().getResource("fxml/film.fxml"));
        BorderPane root = new BorderPane();
        root.setCenter(content);
        group.getChildren().add(root);
        Scene scene = new Scene(group,900,600);
        scene.getStylesheets().add(getClass().getResource("css/filmScene.css").toExternalForm());


        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        launch();
    }
}