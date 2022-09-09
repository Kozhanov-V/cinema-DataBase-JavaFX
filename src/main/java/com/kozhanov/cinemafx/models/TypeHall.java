package com.kozhanov.cinemafx.models;

public class TypeHall {


    private int idTypeHall;
    private String nameHall;

    TypeHall(){

    }
    TypeHall(int idTypeHall,String nameHall){
        this.idTypeHall=idTypeHall;
        this.nameHall=nameHall;
    }
    public int getIdTypeHall() {
        return idTypeHall;
    }

    public void setIdTypeHall(int idTypeHall) {
        this.idTypeHall = idTypeHall;
    }

    public String getNameHall() {
        return nameHall;
    }

    public void setNameHall(String nameHall) {
        this.nameHall = nameHall;
    }
}
