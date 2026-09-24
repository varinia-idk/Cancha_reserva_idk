package com.example.reservacanchasapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "reservas",
        foreignKeys = @ForeignKey(
                entity = Cliente.class,
                parentColumns = "idCliente",
                childColumns = "idCliente",
                onDelete = ForeignKey.CASCADE
        ),
        indices = { @Index("idCliente") }
)
public class Reserva {

    @PrimaryKey(autoGenerate = true)
    private int idReserva;
    private int idCliente;
    private String fechaReserva;
    private String estado = "Pendiente";
    private double totalReserva;

    public Reserva(int idCliente, String fechaReserva, double totalReserva) {
        this.idCliente = idCliente;
        this.fechaReserva = fechaReserva;
        this.totalReserva = totalReserva;
    }

    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(String fechaReserva) { this.fechaReserva = fechaReserva; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getTotalReserva() { return totalReserva; }
    public void setTotalReserva(double totalReserva) { this.totalReserva = totalReserva; }
}