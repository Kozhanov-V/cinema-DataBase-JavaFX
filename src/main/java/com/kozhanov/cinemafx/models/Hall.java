package com.kozhanov.cinemafx.models;

public class Hall {

    private int idHall;
    private int idTypeHall;

    Hall(){

    }
    Hall(int idHall,int idTypeHall){
        this.idHall=idHall;
        this.idTypeHall=idTypeHall;
    }
    public int getIdHall() {
        return idHall;
    }

    public void setIdHall(int idHall) {
        this.idHall = idHall;
    }

    public int getIdTypeHall() {
        return idTypeHall;
    }

    public void setIdTypeHall(int idTypeHall) {
        this.idTypeHall = idTypeHall;
    }
}
