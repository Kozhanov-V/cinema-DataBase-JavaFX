package com.kozhanov.cinemafx;

import com.kozhanov.cinemafx.models.Films;
import com.kozhanov.cinemafx.models.Schedule;
import com.kozhanov.cinemafx.models.ScheduleTableView;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import java.sql.Date;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.*;


public class ScheduleController {
    public static ObservableList<ScheduleTableView> getScheduleData() {
        return ScheduleData;
    }

    private static ObservableList<ScheduleTableView> ScheduleData = FXCollections.observableArrayList();

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(HelloController.getConnectionUrl(), HelloController.getUserName(), HelloController.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnectin(){
        return connection;
    }

    @FXML
    private AnchorPane leftPane;

    @FXML
    private AnchorPane secondPane;

    @FXML
    private Button filmsButton;

    @FXML
    private Button scheduleButton;

    @FXML
    private Button ticketsButton;

    @FXML
    private Button menuButton;

    @FXML
    private Button add;

    @FXML
    private Button clearSetting;

    @FXML
    private DatePicker whereDatePicker;

    @FXML
    private ComboBox choseHall;


    @FXML
    TableView<ScheduleTableView> scheduleTableView;

    @FXML
    TableColumn<ScheduleTableView,Integer> idColumn;

    @FXML
    TableColumn<ScheduleTableView,String> idFilm;

    public DatePicker getWhereDatePicker() {
        return whereDatePicker;
    }

    public ComboBox getChoseHall() {
        return choseHall;
    }

    @FXML
    TableColumn<ScheduleTableView, Integer> idHall;

    @FXML
    TableColumn<ScheduleTableView,String> startDateTimeColumn;

    private static Stage addScheduleStage = new Stage();

    public static Stage getAddScheduleStage(){
        return addScheduleStage;
    }


    @FXML
    private void initialize() {
    initData();
        try {
            settingComboBox();
            settingWhereDatePicker();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scheduleTableView.setEditable(true);
    idColumn.setCellValueFactory(new PropertyValueFactory<ScheduleTableView,Integer>("idSchedule"));
    idFilm.setCellValueFactory(new PropertyValueFactory<ScheduleTableView,String>("tittleFilm"));
    idHall.setCellValueFactory(new PropertyValueFactory<ScheduleTableView,Integer>("idHall"));
    startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<ScheduleTableView,String>("startTime"));
    scheduleTableView.setItems(ScheduleData);
        TableColumn<ScheduleTableView, ScheduleTableView> unfriendCol = new TableColumn<>("Удалить данные");
        unfriendCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        unfriendCol.setCellFactory(param -> new TableCell<ScheduleTableView, ScheduleTableView>() {
            private final Button deleteButton = new Button("Удалить запись");


            @Override
            protected void updateItem(ScheduleTableView scheduleTableView, boolean empty) {
                super.updateItem(scheduleTableView, empty);

                if (scheduleTableView == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(actionEvent -> {
                    getTableView().getItems().remove(scheduleTableView);
                    try {

                        Statement statement = getConnectin().createStatement();
                        statement.executeUpdate("DELETE FROM `cinema`.`schedule` WHERE (`id_schedule` = '" + scheduleTableView.getIdSchedule() + "');");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

            }
        });
        scheduleTableView.getColumns().add(unfriendCol);

    }

    private void settingWhereDatePicker(){
        whereDatePicker.setOnAction(actionEvent -> {
            if(whereDatePicker.getValue()!=null) {
                java.util.Date date = java.util.Date.from(whereDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                 Date DateSql = new Date(date.getTime());

            scheduleTableView.getItems().clear();
            try {
                Statement statement = getConnectin().createStatement();
                if(choseHall.getValue()==null){
                    ResultSet rs = statement.executeQuery("select * from schedule where start_time > '"+DateSql.toString()+" 00:00:00' and start_time < '"+DateSql.toString()+" 23:59:59';");
                     fillScheduleTable(rs);
                }
                else{
                    ResultSet rs = statement.executeQuery("select * from schedule where start_time > '"+DateSql.toString()+" 00:00:00' and start_time < '"+DateSql.toString()+" 23:59:59' and id_hall="+String.valueOf(choseHall.getValue()).charAt(4)+";");
                    fillScheduleTable(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            }
        });
    }

    private void settingComboBox() throws SQLException {

             Statement statement = getConnectin().createStatement();
            ResultSet rs = statement.executeQuery("select id_hall from hall;");
            ObservableList<String> itemsComboBox = FXCollections.observableArrayList();

            while (rs.next()){

                itemsComboBox.add("Зал " + String.valueOf(rs.getInt("id_hall")));

            }
            choseHall.setItems(itemsComboBox);

           choseHall.setOnAction((e)->{
                scheduleTableView.getItems().clear();
                try ( Statement newStatement = getConnectin().createStatement()) {
                    ResultSet resultOfHall = null;
                    if (choseHall.getValue() != null) {
                        if (whereDatePicker.getValue() == null) {
                            resultOfHall = newStatement.executeQuery("select * from schedule where id_hall = " + String.valueOf(choseHall.getValue()).charAt(4));
                        } else {
                            java.util.Date date = java.util.Date.from(whereDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                            Date DateSql = new Date(date.getTime());
                            resultOfHall = statement.executeQuery("select * from schedule where start_time > '" + DateSql.toString() + " 00:00:00' and start_time < '" + DateSql.toString() + " 23:59:59' and id_hall=" + String.valueOf(choseHall.getValue()).charAt(4) + ";");
                        }
                        fillScheduleTable(resultOfHall);
                    }
                    } catch(SQLException ex){
                        ex.printStackTrace();
                    }

            });

    }

       public void addSchedule() throws IOException {
        if(choseHall.getValue()!=null&&whereDatePicker.getValue()!=null) {
            AddScheduleController addScheduleController = new AddScheduleController(choseHall.getValue().toString(), whereDatePicker.getValue());
        }
        if(choseHall.getValue()!=null &&whereDatePicker.getValue()==null){
            AddScheduleController addScheduleController = new AddScheduleController(choseHall.getValue().toString());
        }
        if(choseHall.getValue()==null && whereDatePicker.getValue()!=null){
            AddScheduleController addScheduleController = new AddScheduleController(whereDatePicker.getValue());
        }
        if(choseHall.getValue()==null && whereDatePicker.getValue()==null){
            AddScheduleController addScheduleController = new AddScheduleController();
        }
       }

        void fillScheduleTable(ResultSet resultSet) throws SQLException {
            List<Schedule> tableSchedules = new ArrayList<Schedule>();
            while (resultSet.next()){
                Schedule newSchedule = new Schedule();
                newSchedule.setIdSchedule(resultSet.getInt("id_schedule"));
                newSchedule.setIdFilm(resultSet.getInt("id_film"));
                newSchedule.setIdHall(resultSet.getInt("id_hall"));
                newSchedule.setStartTime(resultSet.getTimestamp("start_time"));
                tableSchedules.add(newSchedule);
            }
            resultSet.close();
            Statement statement1 = getConnectin().createStatement();
            for (Schedule item:tableSchedules) {

                ResultSet rs = statement1.executeQuery("select * from film where id_film = "+item.getIdFilm()+";");
                rs.next();
                ScheduleTableView scheduleTableView = new ScheduleTableView();
                scheduleTableView.setIdSchedule(item.getIdSchedule());
                scheduleTableView.setIdHall(item.getIdHall());
                scheduleTableView.setTittleFilm(rs.getString("tittle"));
                scheduleTableView.setStartTime(item.getStartTime().toString());
                ScheduleData.add(scheduleTableView);
            }
        }

        private void initData(){

            try {
               Statement statement = getConnectin().createStatement();
                ResultSet resultSet = statement.executeQuery("select * from schedule");
                fillScheduleTable(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }



        }


    public void openFilmsPage() throws IOException {
        scheduleTableView.getItems().clear();
        Group group = new Group();
        Parent content = FXMLLoader.load(getClass().getResource("fxml/film.fxml"));
        BorderPane root = new BorderPane();
        root.setCenter(content);
        group.getChildren().add(root);
        Scene scene = new Scene(group);
        scene.getStylesheets().add(getClass().getResource("css/filmScene.css").toExternalForm());
        HelloApplication.getPrimaryStage().setScene(scene);
    }
    public void clearSetting() throws SQLException {
        choseHall.setValue(null);
        whereDatePicker.setValue(null);
        Statement statement = getConnectin().createStatement();
        ResultSet resultSet = statement.executeQuery("select * from schedule");
        fillScheduleTable(resultSet);

    }
    public void openTicketsPage() throws IOException {
        scheduleTableView.getItems().clear();
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
