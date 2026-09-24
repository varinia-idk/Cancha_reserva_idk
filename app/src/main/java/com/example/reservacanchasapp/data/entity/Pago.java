package com.example.reservacanchasapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "pagos",
        foreignKeys = @ForeignKey(
                entity = ReservaDetalle.class,
                parentColumns = "idReservaDetalle",
                childColumns = "idReservaDetalle",
                onDelete = ForeignKey.CASCADE
        ),
        indices = { @Index("idReservaDetalle") }
)
public class Pago {

    @PrimaryKey(autoGenerate = true)
    private int idPago;
    private int idReservaDetalle;
    private double monto;
    private String metodoPago;
    private String estado = "Pendiente";
    private String fechaPago;

    public Pago(int idReservaDetalle, double monto,
                String metodoPago, String fechaPago) {
        this.idReservaDetalle = idReservaDetalle;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fechaPago = fechaPago;
    }

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }
    public int getIdReservaDetalle() { return idReservaDetalle; }
    public double getMonto() { return monto; }
    public String getMetodoPago() { return metodoPago; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFechaPago() { return fechaPago; }
}