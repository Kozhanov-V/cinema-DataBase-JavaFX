package com.kozhanov.cinemafx.Controllers;

import com.kozhanov.cinemafx.HelloApplication;
import com.kozhanov.cinemafx.models.Films;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class HelloController {
    public static ObservableList<Films> getFilmsData() {
        return filmsData;
    }

    private static ObservableList<Films> filmsData = FXCollections.observableArrayList();
    private static String userName = "root";
    private static String password = "password";
    private static String connectionUrl = "jdbc:mysql://localhost:3306/cinema";
    private static Stage addFilmStage = new Stage();

    @FXML
    private AnchorPane secondPane;

    @FXML
    private TableView<Films> filmsTableView;

    @FXML
    private TableColumn<Films, Integer> idColumn;

    @FXML
    private TableColumn<Films, String> tittleColumn;

    public static Stage getAddFilmStage() {
        return addFilmStage;
    }

    @FXML
    private TableColumn<Films, String> durationColumn;

    @FXML
    private TableColumn<Films, Date> startDateColumn;

    @FXML
    private TableColumn<Films, Date> stopDateColumn;

    @FXML
    public TextField textFieldNameFilm;

    @FXML
    public TextField textFieldDuration;

    @FXML
    public DatePicker startDateDataPicker;

    @FXML
    public DatePicker stopDateDataPicker;

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getConnectionUrl() {
        return connectionUrl;
    }

    // инициализируем форму данными
    @FXML
    private void initialize() {
            try {
                initData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            filmsTableView.setEditable(true);
            idColumn.setCellValueFactory(new PropertyValueFactory<Films, Integer>("idFilm"));
            tittleColumn.setCellValueFactory(new PropertyValueFactory<Films, String>("tittle"));
            tittleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            tittleColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Films, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Films, String> filmsStringCellEditEvent) {
                    ((Films) filmsStringCellEditEvent.getTableView().getItems().get(
                            filmsStringCellEditEvent.getTablePosition().getRow())
                    ).setTittle(filmsStringCellEditEvent.getNewValue());
                    Connection connection = null;
                    try {
                        connection = DriverManager.getConnection(connectionUrl, userName, password);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE `cinema`.`film` SET `tittle` = '" + filmsStringCellEditEvent.getNewValue() + "' WHERE (`id_film` = '" +
                                ((Films) filmsStringCellEditEvent.getTableView().getItems().get(
                                        filmsStringCellEditEvent.getTablePosition().getRow())
                                ).getIdFilm() + "');");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            durationColumn.setCellValueFactory(new PropertyValueFactory<Films, String>("duration"));
            durationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            durationColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Films, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Films, String> filmsIntegerCellEditEvent) {
                    ((Films) filmsIntegerCellEditEvent.getTableView().getItems().get(
                            filmsIntegerCellEditEvent.getTablePosition().getRow())
                    ).setDuration(filmsIntegerCellEditEvent.getNewValue());
                    Connection connection = null;
                    try {
                        connection = DriverManager.getConnection(connectionUrl, userName, password);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE `cinema`.`film` SET `duration` = '" + filmsIntegerCellEditEvent.getNewValue() + "' WHERE (`id_film` = '" + ((Films)
                                filmsIntegerCellEditEvent.getTableView().getItems().get(filmsIntegerCellEditEvent.getTablePosition().getRow())
                        ).getIdFilm() + "');");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }


            });


            startDateColumn.setCellValueFactory(new PropertyValueFactory<Films, Date>("startDate"));


            stopDateColumn.setCellValueFactory(new PropertyValueFactory<Films, Date>("stopDate"));
            // заполняем таблицу данными
            filmsTableView.setItems(filmsData);

            TableColumn<Films, Films> unfriendCol = new TableColumn<>("Удалить данные");
            unfriendCol.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
            unfriendCol.setCellFactory(param -> new TableCell<Films, Films>() {
                private final Button deleteButton = new Button("Удалить запись");


                @Override
                protected void updateItem(Films film, boolean empty) {
                    super.updateItem(film, empty);

                    if (film == null) {
                        setGraphic(null);
                        return;
                    }

                    setGraphic(deleteButton);
                    deleteButton.setOnAction(actionEvent -> {
                        getTableView().getItems().remove(film);
                        Connection connection = null;
                        try {
                            connection = DriverManager.getConnection(connectionUrl, userName, password);
                            Statement statement = connection.createStatement();
                            statement.executeUpdate("DELETE FROM `cinema`.`film` WHERE (`id_film` = '" + film.getIdFilm() + "');");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                }
            });
            filmsTableView.getColumns().add(unfriendCol);


    }



    // подготавливаем данные для таблицы
    // вы можете получать их с базы данных
    private void initData() throws SQLException {


        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connection.createStatement()) {
             ResultSet resultSet = statement.executeQuery("select * from film");
            while (resultSet.next()) {

                Films newFilm = new Films();
                newFilm.setIdFilm(resultSet.getInt("id_film"));
                newFilm.setTittle(resultSet.getString("tittle"));
                newFilm.setDuration(resultSet.getString("duration"));
                newFilm.setStartDate(resultSet.getDate("start_date"));
                newFilm.setStopDate(resultSet.getDate("stop_date"));
                filmsData.add(newFilm);
            }


        }
    }


    public void addFilm() throws SQLException, IOException {

        Group group = new Group();
         Parent content = FXMLLoader.load(getClass().getResource("fxml/addFilm.fxml"));
        BorderPane root = new BorderPane();
        root.setCenter(content);
        group.getChildren().add(root);
        Scene scene = new Scene(group);
        addFilmStage.setScene(scene);
        addFilmStage.setMaximized(false);
        addFilmStage.show();
    }

    public void openSchedule() throws IOException {
        filmsTableView.getItems().clear();
        Group group = new Group();
        Parent content = FXMLLoader.load(getClass().getResource("fxml/schedule.fxml"));
        BorderPane root = new BorderPane();
        root.setCenter(content);
        group.getChildren().add(root);
        Scene scene = new Scene(group);
        scene.getStylesheets().add(getClass().getResource("css/schedule.css").toExternalForm());
        HelloApplication.getPrimaryStage().setScene(scene);


    }
    public void openTicketsPage() throws IOException {
        filmsTableView.getItems().clear();
        Group group = new Group();
        Parent content = FXMLLoader.load(getClass().getResource("fxml/tickets.fxml"));
        BorderPane root = new BorderPane();
        root.setCenter(content);
        group.getChildren().add(root);
        Scene scene = new Scene(group);
        scene.getStylesheets().add(getClass().getResource("css/tickets.css").toExternalForm());
        HelloApplication.getPrimaryStage().setScene(scene);
    }
}