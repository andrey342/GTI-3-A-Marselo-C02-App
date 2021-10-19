package com.example.gti_3a_kuzminandrey_sprint0.main.POJO;

import com.google.firebase.Timestamp;

// -----------------------------------------------------------------------------------
// @author: Andrey Kuzmin
// -----------------------------------------------------------------------------------

//POJO Medicion
public class Medicion {

    //atributos
    private int id;
    private Timestamp fecha;
    private int lectura;
    private int user_id;
    private double latX;
    private double latY;

    /**
     * La descripción de Medicion. Constuctor vacio.
     */
    public Medicion() { }

    /**
     * La descripción de Medicion. Costructor con parametros
     *
     * @param id id de la medicion.
     * @param fecha fecha de la medicion.
     * @param lectura lectura de la medicion.
     * @param user_id id del usario al que pertenece.
     * @param latX direccion X.
     * @param latY direccion Y.
     *
     */
    public Medicion(int id, Timestamp fecha, int lectura, int user_id, double latX, double latY) {
        this.id = id;
        this.fecha = fecha;
        this.lectura = lectura;
        this.user_id = user_id;
        this.latX = latX;
        this.latY = latY;
    }

    /**
     * La descripción de Medicion. Costructor con parametros
     *
     * @param fecha fecha de la medicion.
     * @param lectura lectura de la medicion.
     * @param latX direccion X.
     * @param latY direccion Y.
     *
     */
    public Medicion(Timestamp fecha, int lectura, double latX, double latY) {
        this.fecha = fecha;
        this.lectura = lectura;
        this.latX = latX;
        this.latY = latY;
    }

    /**
     *
     * Getters & setters de todos los atributos del POJO.
     *
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
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
