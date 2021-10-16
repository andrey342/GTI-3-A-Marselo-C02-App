package com.example.gti_3a_kuzminandrey_sprint0.Main.POJO;

public class Medicion {
    private int id;
    private String fecha;
    private int lectura;
    private int user_id;
    private double latX;
    private double latY;

    public Medicion() { }

    public Medicion(int id, String fecha, int lectura, int user_id, double latX, double latY) {
        this.id = id;
        this.fecha = fecha;
        this.lectura = lectura;
        this.user_id = user_id;
        this.latX = latX;
        this.latY = latY;
    }

    public Medicion(String fecha, int lectura, double latX, double latY) {
        this.fecha = fecha;
        this.lectura = lectura;
        this.latX = latX;
        this.latY = latY;
    }

    //getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getLectura() {
        return lectura;
    }

    public void setLectura(int lectura) {
        this.lectura = lectura;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getLatX() {
        return latX;
    }

    public void setLatX(double latX) {
        this.latX = latX;
    }

    public double getLatY() {
        return latY;
    }

    public void setLatY(double latY) {
        this.latY = latY;
    }
}
