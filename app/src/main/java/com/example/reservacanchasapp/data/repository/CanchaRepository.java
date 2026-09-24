package com.example.reservacanchasapp.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

import com.example.reservacanchasapp.data.AppDatabase;
import com.example.reservacanchasapp.data.dao.CanchaDao;
import com.example.reservacanchasapp.data.entity.Cancha;

public class CanchaRepository {

    private final CanchaDao canchaDao;

    public CanchaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.canchaDao = db.canchaDao();
    }

    public LiveData<List<Cancha>> obtenerTodasLasCanchas() {
        return canchaDao.obtenerCanchas();
    }

    public void insertar(Cancha cancha) {
        AppDatabase.databaseWriteExecutor.execute(() -> canchaDao.insertar(cancha));
    }
}