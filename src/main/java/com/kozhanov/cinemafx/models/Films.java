package com.kozhanov.cinemafx.models;

import javax.persistence.GeneratedValue;
import java.sql.Date;

public class Films {
    private int idFilm;
    private String tittle;
    private String duration;
    private Date startDate;
    private Date stopDate;
    public Films(){

    }

    Films(int idFilm, String tittle, String duration){
        this.idFilm=idFilm;
        this.tittle=tittle;
        this.duration=duration;
    }
    Films(int idFilm, String tittle, String duration, Date startDate, Date stopDate) {
        this.idFilm=idFilm;
        this.tittle=tittle;
        this.duration=duration;
        this.startDate=startDate;
        this.stopDate=stopDate;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }
}
