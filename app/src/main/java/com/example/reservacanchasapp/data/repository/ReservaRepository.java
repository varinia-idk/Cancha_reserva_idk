package com.example.reservacanchasapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import com.example.reservacanchasapp.data.AppDatabase;
import com.example.reservacanchasapp.data.dao.ReservaDao;
import com.example.reservacanchasapp.data.entity.Reserva;
import com.example.reservacanchasapp.data.entity.ReservaDetalle;
import com.example.reservacanchasapp.data.entity.Pago;
import com.example.reservacanchasapp.data.dao.ReservaConDetalles;

public class ReservaRepository {

    private final ReservaDao reservaDao;
    private final AppDatabase database;

    public ReservaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.database = db;
        this.reservaDao = db.reservaDao();
    }

    public LiveData<List<Reserva>> obtenerReservasPorCliente(int idCliente) {
        return reservaDao.obtenerPorCliente(idCliente);
    }

    public LiveData<List<ReservaConDetalles>> obtenerHistorial(int idCliente) {
        return reservaDao.obtenerHistorial(idCliente);
    }

    // Callback para devolver el ID generado
    public interface OnReservaCreadaListener {
        void onCreada(long idReserva);
    }

    public void crearReserva(Reserva reserva, List<ReservaDetalle> detalles,
                             OnReservaCreadaListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            final long[] resultado = {-1};
            database.runInTransaction(() -> {
                for (ReservaDetalle detalle : detalles) {
                    if (reservaDao.contarConflictosHorario(detalle.getIdCancha(), detalle.getFechaJuego(),
                            detalle.getHoraInicio(), detalle.getHoraFin()) > 0) return;
                }
                long idReserva = reservaDao.insertarReserva(reserva);
                for (ReservaDetalle detalle : detalles) {
                    detalle.setIdReserva((int) idReserva);
                    detalle.setIdReservaDetalle((int) reservaDao.insertarDetalle(detalle));
                }
                resultado[0] = idReserva;
            });
            listener.onCreada(resultado[0]);
        });
    }

    public void registrarPago(int idReserva, ReservaDetalle detalle, String metodo, String fechaPago) {
        AppDatabase.databaseWriteExecutor.execute(() -> database.runInTransaction(() -> {
            Pago pago = new Pago(detalle.getIdReservaDetalle(), detalle.getSubtotal(), metodo, fechaPago);
            long idPago = database.pagoDao().insertarPago(pago);
            database.pagoDao().cambiarEstado((int) idPago, "Pagado");
            int totalDetalles = reservaDao.contarDetalles(idReserva);
            int detallesPagados = reservaDao.contarDetallesPagados(idReserva);
            if (totalDetalles > 0 && totalDetalles == detallesPagados) reservaDao.cambiarEstado(idReserva, "Confirmada");
            else reservaDao.cambiarEstado(idReserva, "Pendiente");
        }));
    }
}
