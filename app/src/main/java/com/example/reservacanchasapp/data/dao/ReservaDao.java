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
    @Query("SELECT * FROM reservas WHERE idCliente = :idCliente ORDER BY fechaReserva DESC, idReserva DESC")
    LiveData<List<ReservaConDetalles>> obtenerHistorial(int idCliente);

    @Transaction
    @Query("SELECT * FROM reservas WHERE idReserva = :idReserva")
    ReservaConDetalles obtenerConDetalles(int idReserva);

    @Query("SELECT rd.*, c.nombre AS nombreCancha, " +
            "d.nombre AS nombreDisciplina, " +
            "(SELECT COUNT(*) FROM pagos p WHERE p.idReservaDetalle = rd.idReservaDetalle AND p.estado = 'Pagado') AS pagosPagados " +
            "FROM reserva_detalle rd " +
            "INNER JOIN canchas c ON rd.idCancha = c.idCancha " +
            "INNER JOIN disciplinas d ON rd.idDisciplina = d.idDisciplina " +
            "WHERE rd.idReserva = :idReserva ORDER BY rd.idReservaDetalle")
    List<DetalleConNombres> obtenerDetalleConNombres(int idReserva);

    @Query("UPDATE reservas SET estado = :nuevoEstado WHERE idReserva = :id")
    void cambiarEstado(int id, String nuevoEstado);

    @Query("SELECT * FROM reserva_detalle WHERE idReserva = :idReserva")
    List<ReservaDetalle> obtenerDetalles(int idReserva);

    @Query("SELECT * FROM reservas WHERE idReserva = :id")
    Reserva obtenerPorId(int id);

    @Query("SELECT COUNT(*) FROM reserva_detalle WHERE idReserva = :idReserva")
    int contarDetalles(int idReserva);

    @Query("SELECT COUNT(DISTINCT rd.idReservaDetalle) FROM reserva_detalle rd INNER JOIN pagos p ON p.idReservaDetalle = rd.idReservaDetalle WHERE rd.idReserva = :idReserva AND p.estado = 'Pagado'")
    int contarDetallesPagados(int idReserva);

    @Query("SELECT COUNT(*) FROM reserva_detalle rd INNER JOIN reservas r ON r.idReserva = rd.idReserva " +
            "WHERE rd.idCancha = :idCancha AND rd.fechaJuego = :fecha " +
            "AND r.estado != 'Cancelada' AND rd.horaInicio < :horaFin AND rd.horaFin > :horaInicio")
    int contarConflictosHorario(int idCancha, String fecha, String horaInicio, String horaFin);
}
