package com.androidavanzado.pruebatecnicainterfell.Models;

public class Contraversia {

    public int Id;
    public String Plate;
    public String Date;
    public String Hour;

    public Contraversia(int id, String plate, String date, String hour) {
        Id = id;
        Plate = plate;
        Date = date;
        Hour = hour;
    }

    public Contraversia() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPlate() {
        return Plate;
    }

    public void setPlate(String plate) {
        Plate = plate;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }
}
