package com.example.reservacanchasapp.data.dao;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.reservacanchasapp.data.entity.Reserva;
import com.example.reservacanchasapp.data.entity.ReservaDetalle;

import java.util.List;

public class ReservaConDetalles {
    @Embedded
    public Reserva reserva;

    @Relation(
            parentColumn = "idReserva",
            entityColumn = "idReserva"
    )
    public List<ReservaDetalle> detalles;
}