package com.example.reservacanchasapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "canchas")
public class Cancha {
    @PrimaryKey(autoGenerate = true)
    private int idCancha;
    private String nombre;
    private String tipo; // "Fútbol 5", "Fútbol 7", "Vóley"...
    private double precioBase;
    private String estado = "Activa";

    public Cancha(String nombre, String tipo, double precioBase, String estado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.precioBase = precioBase;
        this.estado = estado;
    }
    public int getIdCancha() { return idCancha; }
    public void setIdCancha(int idCancha) { this.idCancha = idCancha; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public double getPrecioBase() { return precioBase; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
}
