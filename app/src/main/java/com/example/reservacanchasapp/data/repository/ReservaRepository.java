package com.example.reservacanchasapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import com.example.reservacanchasapp.data.AppDatabase;
import com.example.reservacanchasapp.data.dao.ReservaDao;
import com.example.reservacanchasapp.data.entity.Reserva;
import com.example.reservacanchasapp.data.entity.ReservaDetalle;

public class ReservaRepository {

    private final ReservaDao reservaDao;

    public ReservaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.reservaDao = db.reservaDao();
    }

    public LiveData<List<Reserva>> obtenerReservasPorCliente(int idCliente) {
        return reservaDao.obtenerPorCliente(idCliente);
    }

    // Callback para devolver el ID generado
    public interface OnReservaCreadaListener {
        void onCreada(long idReserva);
    }

    public void crearReserva(Reserva reserva, List<ReservaDetalle> detalles,
                             OnReservaCreadaListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long idReserva = reservaDao.insertarReserva(reserva);
            for (ReservaDetalle detalle : detalles) {
                detalle.setIdReserva((int) idReserva);
                reservaDao.insertarDetalle(detalle);
            }
            listener.onCreada(idReserva);
        });
    }
}