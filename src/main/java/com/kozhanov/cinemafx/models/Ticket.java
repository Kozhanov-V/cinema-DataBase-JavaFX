package com.kozhanov.cinemafx.models;

import java.sql.Timestamp;

public class Ticket {
    private int idTicket;
    private int idSchedule;
    private int idSeriesInHall;
    private int place;
    private int row;
    private Timestamp startTime;

    public Ticket(){

    }
    public Ticket(int idTicket,int idSchedule,int place,int row){
        this.idTicket=idTicket;
        this.idSchedule=idSchedule;
        this.place=place;
        this.row = row;
    }


    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(int idSchedule) {
        this.idSchedule = idSchedule;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public int getIdSeriesInHall() {
        return idSeriesInHall;
    }

    public void setIdSeriesInHall(int idSeriesInHall) {
        this.idSeriesInHall = idSeriesInHall;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }


}
