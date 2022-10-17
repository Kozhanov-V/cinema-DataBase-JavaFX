package com.kozhanov.cinemafx.Controllers;

import com.kozhanov.cinemafx.HelloApplication;
import com.kozhanov.cinemafx.models.Ticket;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TicketsController {

    public static ObservableList<Ticket> TicketDate = FXCollections.observableArrayList();

    @FXML
    private TableView<Ticket>ticketTableView;

    @FXML
    private TableColumn<Ticket,Integer> idSchedule;

    @FXML
    private TableColumn<Ticket,Integer> idTicket;

    @FXML
    private TableColumn<Ticket,Integer> row;

    @FXML
    private TableColumn<Ticket,Integer> place;

    @FXML
    private TextField idTicketTextField;

    @FXML
    private TextField idScheduleTextField;

    @FXML
    private void initialize() {
        initData();

        idTicket.setCellValueFactory(new PropertyValueFactory<Ticket,Integer>("idTicket"));
        idSchedule.setCellValueFactory(new PropertyValueFactory<Ticket,Integer>("idSchedule"));
        row.setCellValueFactory(new PropertyValueFactory<Ticket,Integer>("row"));
        place.setCellValueFactory(new PropertyValueFactory<Ticket,Integer>("place"));
        ticketTableView.setItems(TicketDate);

        TableColumn<Ticket, Ticket> unfriendCol = new TableColumn<>("Удалить данные");
        unfriendCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        unfriendCol.setCellFactory(param -> new TableCell<Ticket, Ticket>() {
            private final Button deleteButton = new Button("Удалить запись");


            @Override
            protected void updateItem(Ticket ticket, boolean empty) {
                super.updateItem(ticket, empty);

                if (ticket == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(actionEvent -> {
                    getTableView().getItems().remove(ticket);
                    try {

                        Statement statement = ScheduleController.getConnectin().createStatement();
                        statement.executeUpdate("DELETE FROM `cinema`.`ticket` WHERE (`id_ticket` = '" + ticket.getIdTicket() + "');");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

            }
        });
        ticketTableView.getColumns().add(unfriendCol);

        idTicketTextField.setOnAction(actionEvent -> {
            try {
                Statement statement = ScheduleController.getConnectin().createStatement();
                ResultSet rs;
                if(idTicketTextField.getText().equals("")){
                    rs = statement.executeQuery("select * from ticket;");
                }else {
                    rs = statement.executeQuery("select * from ticket where id_ticket=" + idTicketTextField.getText() + ";");
                }
                ticketTableView.getItems().clear();
                fillTable(rs);


            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        idScheduleTextField.setOnAction(actionEvent -> {
            try {
                Statement statement = ScheduleController.getConnectin().createStatement();
                ResultSet rs;
                if(idScheduleTextField.getText().equals("")){
                    rs = statement.executeQuery("select * from ticket;");
                }
                else{
                    rs = statement.executeQuery("select * from ticket where id_schedule="+idScheduleTextField.getText()+";");
                }
                ticketTableView.getItems().clear();
                fillTable(rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    private void initData(){
        try {
            Statement statement = ScheduleController.getConnectin().createStatement();
            ResultSet rs = statement.executeQuery("select * from ticket;");
            fillTable(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void fillTable(ResultSet rs) throws SQLException {
        while(rs.next()){
            Ticket newTicket = new Ticket();
            newTicket.setIdTicket(rs.getInt("id_ticket"));
            newTicket.setIdSchedule(rs.getInt("id_schedule"));
            newTicket.setPlace(rs.getInt("place"));
            newTicket.setRow(rs.getInt("row"));
            TicketDate.add(newTicket);
        }

    }

    public void openFilmsPage() throws IOException {
        ticketTableView.getItems().clear();
        Group group = new Group();
        Parent content = FXMLLoader.load(getClass().getResource("fxml/film.fxml"));
        BorderPane root = new BorderPane();
        root.setCenter(content);
        group.getChildren().add(root);
        Scene scene = new Scene(group);
        scene.getStylesheets().add(getClass().getResource("css/filmScene.css").toExternalForm());
        HelloApplication.getPrimaryStage().setScene(scene);
    }
    public void openSchedulePage() throws IOException {
        ticketTableView.getItems().clear();
        Group group = new Group();
        Parent content = FXMLLoader.load(getClass().getResource("fxml/schedule.fxml"));
        BorderPane root = new BorderPane();
        root.setCenter(content);
        group.getChildren().add(root);
        Scene scene = new Scene(group);
        scene.getStylesheets().add(getClass().getResource("css/schedule.css").toExternalForm());
        HelloApplication.getPrimaryStage().setScene(scene);


    }
}
