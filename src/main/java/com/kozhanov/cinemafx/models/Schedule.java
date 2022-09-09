package com.kozhanov.cinemafx.models;
import java.sql.Timestamp;
public class Schedule {
    private int idSchedule;
    private int idFilm;
    private int idHall;
    private Timestamp startTime;

    public Schedule(){

    }

    public int getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(int idSchedule) {
        this.idSchedule = idSchedule;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public int getIdHall() {
        return idHall;
    }

    public void setIdHall(int idHall) {
        this.idHall = idHall;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Schedule(int idSchedule, int idFilm, int idHall, Timestamp startTime){
        this.idSchedule=idSchedule;
        this.idFilm = idFilm;
        this.idHall=idHall;
        this.startTime=startTime;
    }
}
