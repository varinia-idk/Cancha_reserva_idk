package com.example.reservacanchasapp.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

    @Entity(
            tableName = "reserva_detalle",
            foreignKeys = {
                    @ForeignKey(entity = Reserva.class, parentColumns = "idReserva",
                            childColumns = "idReserva", onDelete = ForeignKey.CASCADE),
                    @ForeignKey(entity = Cancha.class, parentColumns = "idCancha",
                            childColumns = "idCancha"),
                    @ForeignKey(entity = Disciplina.class, parentColumns = "idDisciplina",
                            childColumns = "idDisciplina")
            },
            indices = { @Index("idReserva"), @Index("idCancha"),
                    @Index("idDisciplina") }
    )
    public class ReservaDetalle {

        @PrimaryKey(autoGenerate = true)
        private int idReservaDetalle;
        private int idReserva;
        private int idCancha;
        private int idDisciplina;
        private String fechaJuego;
        private String horaInicio;
        private String horaFin;
        private double precioUnitario;
        private double subtotal;

        public ReservaDetalle(int idReserva, int idCancha, int idDisciplina,
                              String fechaJuego, String horaInicio, String horaFin,
                              double precioUnitario, double subtotal) {
            this.idReserva = idReserva;
            this.idCancha = idCancha;
            this.idDisciplina = idDisciplina;
            this.fechaJuego = fechaJuego;
            this.horaInicio = horaInicio;
            this.horaFin = horaFin;
            this.precioUnitario = precioUnitario;
            this.subtotal = subtotal;
        }

        public int getIdReservaDetalle() { return idReservaDetalle; }
        public void setIdReservaDetalle(int id) { this.idReservaDetalle = id; }
        public int getIdReserva() { return idReserva; }
        public void setIdReserva(int idReserva) { this.idReserva = idReserva; }
        public int getIdCancha() { return idCancha; }
        public int getIdDisciplina() { return idDisciplina; }
        public String getFechaJuego() { return fechaJuego; }
        public String getHoraInicio() { return horaInicio; }
        public String getHoraFin() { return horaFin; }
        public double getPrecioUnitario() { return precioUnitario; }
        public double getSubtotal() { return subtotal; }

}
