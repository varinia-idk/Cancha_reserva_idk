package com.example.reservacanchasapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import com.example.reservacanchasapp.data.entity.Cancha;
import com.example.reservacanchasapp.data.repository.CanchaRepository;

public class CanchaViewModel extends AndroidViewModel {

    private final CanchaRepository repository;
    private final LiveData<List<Cancha>> todasLasCanchas;

    public CanchaViewModel(@NonNull Application application) {
        super(application);
        repository = new CanchaRepository(application);
        todasLasCanchas = repository.obtenerTodasLasCanchas();
    }

    public LiveData<List<Cancha>> obtenerTodasLasCanchas() {
        return todasLasCanchas;
    }

    public void insertar(Cancha cancha) {
        repository.insertar(cancha);
    }
}