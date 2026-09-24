package com.example.reservacanchasapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.reservacanchasapp.data.entity.Reserva;
import com.example.reservacanchasapp.data.entity.ReservaDetalle;
import com.example.reservacanchasapp.data.dao.ReservaConDetalles;
import com.example.reservacanchasapp.data.repository.ReservaRepository;

import java.util.List;

public class ReservaViewModel extends AndroidViewModel {
    private final ReservaRepository repository;
    public final MutableLiveData<Long> reservaCreadaId = new MutableLiveData<>();
    public ReservaViewModel(@NonNull Application application) {
        super(application);
        repository = new ReservaRepository(application);
    }
    public LiveData<List<Reserva>> obtenerReservasPorCliente(int idCliente) {
        return repository.obtenerReservasPorCliente(idCliente);
    }
    public LiveData<List<ReservaConDetalles>> obtenerHistorial(int idCliente) {
        return repository.obtenerHistorial(idCliente);
    }
    public void registrarPago(int idReserva, ReservaDetalle detalle, String metodo, String fecha) {
        repository.registrarPago(idReserva, detalle, metodo, fecha);
    }
    public void crearReserva(Reserva reserva, List<ReservaDetalle> detalles) {
        repository.crearReserva(reserva, detalles, idReserva -> {
// postValue() actualiza LiveData desde 2do plano
            reservaCreadaId.postValue(idReserva);
        });

    }
}
