package com.kozhanov.cinemafx.models;

import java.sql.Timestamp;

public class ScheduleTableView extends Films {
    private int idSchedule;
    private String tittleFilm;
    private int idHall;
    private String startTime;

    public ScheduleTableView(){

    }
    public ScheduleTableView(int idSchedule, String tittleFilm, int idHall, String startTime){
        this.idSchedule=idSchedule;
        this.tittleFilm=tittleFilm;
        this.idHall=idHall;
        this.startTime=startTime;
    }
    public int getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(int idSchedule) {
        this.idSchedule = idSchedule;
    }

    public String getTittleFilm() {
        return tittleFilm;
    }

    public void setTittleFilm(String tittleFilm) {
        this.tittleFilm = tittleFilm;
    }

    public int getIdHall() {
        return idHall;
    }

    public void setIdHall(int idHall) {
        this.idHall = idHall;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startDateTime) {
        this.startTime = startDateTime;
    }
}
