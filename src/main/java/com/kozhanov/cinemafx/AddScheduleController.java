package com.kozhanov.cinemafx;


import com.kozhanov.cinemafx.models.ScheduleTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import com.kozhanov.cinemafx.ScheduleController;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class AddScheduleController {

    @FXML
    public  ComboBox choseFilm;

    @FXML
    private ComboBox choseHall;

    @FXML
    private DatePicker setDate;

    @FXML
    private ComboBox choseTime;

    private int selectedValueChoseHall;
    private Date selectedValueDate;
    AddScheduleController(String valueChoseHall, LocalDate date){

        FXMLLoader loader= new FXMLLoader(getClass().getResource("fxml/addSchedule.fxml"));
        loader.setController(this);
        try {
            ScheduleController.getAddScheduleStage().setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fillChoseFilmComboBox();
        fillChoseHallComboBox();
        fillChoseTimeComboBox();
        setDate.setValue(date);
        this.selectedValueChoseHall=Character.digit(valueChoseHall.charAt(4),10);
        this.choseHall.setValue(selectedValueChoseHall);
        ScheduleController.getAddScheduleStage().setMaximized(false);
        ScheduleController.getAddScheduleStage().show();
     }
    AddScheduleController(String valueChoseHall){
        FXMLLoader loader= new FXMLLoader(getClass().getResource("fxml/addSchedule.fxml"));
        loader.setController(this);
        try {
            ScheduleController.getAddScheduleStage().setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fillChoseFilmComboBox();
        fillChoseHallComboBox();
        fillChoseTimeComboBox();
        this.selectedValueChoseHall=Character.digit(valueChoseHall.charAt(4),10);
        this.choseHall.setValue(selectedValueChoseHall);
        ScheduleController.getAddScheduleStage().setMaximized(false);
        ScheduleController.getAddScheduleStage().show();
    }
    AddScheduleController(LocalDate date){
        FXMLLoader loader= new FXMLLoader(getClass().getResource("fxml/addSchedule.fxml"));
        loader.setController(this);
        try {
            ScheduleController.getAddScheduleStage().setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fillChoseFilmComboBox();
        fillChoseHallComboBox();
        fillChoseTimeComboBox();
        setDate.setValue(date);
        ScheduleController.getAddScheduleStage().setMaximized(false);
        ScheduleController.getAddScheduleStage().show();
    }
    AddScheduleController(){
        FXMLLoader loader= new FXMLLoader(getClass().getResource("fxml/addSchedule.fxml"));
        loader.setController(this);
        try {
            ScheduleController.getAddScheduleStage().setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fillChoseFilmComboBox();
        fillChoseHallComboBox();
        fillChoseTimeComboBox();
        ScheduleController.getAddScheduleStage().setMaximized(false);
        ScheduleController.getAddScheduleStage().show();
    }
    private void fillChoseFilmComboBox() {
        ObservableList<String> choseFilmList = FXCollections.observableArrayList();
        try {
            Statement statement = ScheduleController.getConnectin().createStatement();
            ResultSet rs = statement.executeQuery("select tittle from film;");
            while (rs.next()){
                choseFilmList.add(rs.getString("tittle"));
            }
          //  choseFilm.setItems(choseFilmList);
            choseFilm.setItems(choseFilmList);
            choseFilm.setValue(choseFilmList.get(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void fillChoseHallComboBox() {
        ObservableList<String> choseHallList = FXCollections.observableArrayList();
        try {
            Statement statement = ScheduleController.getConnectin().createStatement();
            ResultSet rs = statement.executeQuery("select id_hall from hall;");
            while (rs.next()){
                choseHallList.add(rs.getString("id_hall"));
            };

            this.choseHall.setItems(choseHallList);
            this.choseHall.setValue(choseHallList.get(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void fillChoseTimeComboBox(){
        ObservableList<String> choseTimeList = FXCollections.observableArrayList("08:00","09:35","11:00","12:35","14:10","15:45","17:20","19:00","20:35","22:00","23:00");
            choseTime.setItems(choseTimeList);
            choseTime.setValue(choseTimeList.get(0));
    }
    @FXML
    private void sendToDatabase(){
        java.util.Date date = java.util.Date.from(setDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date setDateSql = new Date(date.getTime());
        long convertTimeToMilliseconds =Character.digit(String.valueOf(choseTime.getValue()).charAt(0),10)*36000000;
        convertTimeToMilliseconds +=Character.digit(String.valueOf(choseTime.getValue()).charAt(1),10)*3600000;
        convertTimeToMilliseconds +=Character.digit(String.valueOf(choseTime.getValue()).charAt(3),10)*600000;
        convertTimeToMilliseconds +=Character.digit(String.valueOf(choseTime.getValue()).charAt(4),10)*60000;
        Timestamp newTimeStamp = new Timestamp(setDateSql.getTime()+convertTimeToMilliseconds);
        try {
            Statement statementForResult = ScheduleController.getConnectin().createStatement();
            ResultSet rs = statementForResult.executeQuery("select id_film from film where tittle='"+choseFilm.getValue()+"';");
            int idFilm=0;
            while(rs.next()){
                idFilm = rs.getInt("id_film");
            }
            Statement statement = ScheduleController.getConnectin().createStatement();
            String dateTime = newTimeStamp.toString().substring(0,19);
            statement.executeUpdate("INSERT INTO `cinema`.`schedule` (`id_film`, `id_hall`, `start_time`) VALUES ('"+idFilm+"', '"+choseHall.getValue()+"', '"+dateTime+"');");
            Statement statement1 = ScheduleController.getConnectin().createStatement();
            ResultSet resultSet = statement1.executeQuery("select  * from schedule ORDER by id_schedule DESC limit 1; ");
            while (resultSet.next()) {

                ScheduleTableView newSchedule = new ScheduleTableView();
                newSchedule.setIdSchedule(resultSet.getInt("id_schedule"));
                newSchedule.setIdHall(resultSet.getInt("id_film"));
                newSchedule.setTittleFilm(String.valueOf(choseFilm.getValue()));
                newSchedule.setStartTime(dateTime);
                HelloController.getFilmsData().add(newSchedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ScheduleController.getAddScheduleStage().close();
    }
}
