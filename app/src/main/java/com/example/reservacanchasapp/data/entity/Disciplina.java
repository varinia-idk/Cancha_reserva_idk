package com.example.reservacanchasapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "disciplinas")
public class Disciplina {
    @PrimaryKey(autoGenerate = true)
    private int idDisciplina;
    private String nombre;
    private String descripcion;
    public Disciplina(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    public int getIdDisciplina() { return idDisciplina; }
    public void setIdDisciplina(int id) { this.idDisciplina = id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}