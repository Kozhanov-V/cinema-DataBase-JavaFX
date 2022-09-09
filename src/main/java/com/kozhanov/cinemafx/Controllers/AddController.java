package com.kozhanov.cinemafx.Controllers;

import com.kozhanov.cinemafx.HelloController;
import com.kozhanov.cinemafx.models.Films;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.*;
import java.time.ZoneId;

public class AddController {

    private static ObservableList<Films> filmsData = FXCollections.observableArrayList();

    @FXML
    public TextField textFieldNameFilm;

    @FXML
    public TextField textFieldDuration;

    @FXML
    public DatePicker startDateDataPicker;

    @FXML
    public DatePicker stopDateDataPicker;

    @FXML
    public Button btnComfirmAddFilm;

    public void comfirmAddFilm() throws SQLException {

    if(!textFieldNameFilm.getText().isEmpty() && !textFieldDuration.getText().isEmpty() && !startDateDataPicker.getValue().equals("") && !stopDateDataPicker.equals("")){

        try (Connection connection = DriverManager.getConnection(HelloController.getConnectionUrl(),HelloController.getUserName(), HelloController.getPassword());
             Statement statement = connection.createStatement()) {
            java.util.Date date = java.util.Date.from(startDateDataPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date startDateSql = new Date(date.getTime());
            date = java.util.Date.from(stopDateDataPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date stopDateSql = new Date(date.getTime());
             statement.executeUpdate("INSERT INTO `cinema`.`film` (`tittle`, `duration`, `start_date`, `stop_date`) VALUES ('"+textFieldNameFilm.getText()+"', '"+textFieldDuration.getText()+"', '"+startDateSql+"', '"+stopDateSql+"');");
            ResultSet resultSet = statement.executeQuery("select  * from film ORDER by id_film DESC limit 1; ");
            while (resultSet.next()) {

                Films newFilm = new Films();
                newFilm.setIdFilm(resultSet.getInt("id_film"));
                newFilm.setTittle(resultSet.getString("tittle"));
                newFilm.setDuration(resultSet.getString("duration"));
                newFilm.setStartDate(resultSet.getDate("start_date"));
                newFilm.setStopDate(resultSet.getDate("stop_date"));
                HelloController.getFilmsData().add(newFilm);
            }


        }
        HelloController.getAddFilmStage().close();
    }
    else{

    }
    }
}
