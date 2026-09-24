package com.example.reservacanchasapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.reservacanchasapp.data.entity.DetalleConNombres;
import com.example.reservacanchasapp.data.entity.Reserva;
import com.example.reservacanchasapp.data.entity.ReservaDetalle;

import java.util.List;

@Dao
public interface ReservaDao {

    @Insert
    long insertarReserva(Reserva reserva);

    @Insert
    long insertarDetalle(ReservaDetalle detalle);

    @Query("SELECT * FROM reservas WHERE idCliente = :idCliente " +
            "ORDER BY fechaReserva DESC")
    LiveData<List<Reserva>> obtenerPorCliente(int idCliente);

    @Transaction
    @Query("SELECT * FROM reservas WHERE idReserva = :idReserva")
    ReservaConDetalles obtenerConDetalles(int idReserva);

    @Query("SELECT rd.*, c.nombre AS nombreCancha, " +
            "d.nombre AS nombreDisciplina " +
            "FROM reserva_detalle rd " +
            "INNER JOIN canchas c ON rd.idCancha = c.idCancha " +
            "INNER JOIN disciplinas d ON rd.idDisciplina = d.idDisciplina " +
            "WHERE rd.idReserva = :idReserva")
    List<DetalleConNombres> obtenerDetalleConNombres(int idReserva);

    @Query("UPDATE reservas SET estado = :nuevoEstado WHERE idReserva = :id")
    void cambiarEstado(int id, String nuevoEstado);
}