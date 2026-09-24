package com.example.reservacanchasapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.reservacanchasapp.data.dao.ClienteDao;
import com.example.reservacanchasapp.data.dao.CanchaDao;
import com.example.reservacanchasapp.data.dao.PagoDao;
import com.example.reservacanchasapp.data.dao.ReservaDao;
import com.example.reservacanchasapp.data.entity.Cliente;
import com.example.reservacanchasapp.data.entity.Cancha;
import com.example.reservacanchasapp.data.entity.Disciplina;
import com.example.reservacanchasapp.data.entity.Reserva;
import com.example.reservacanchasapp.data.entity.ReservaDetalle;
import com.example.reservacanchasapp.data.entity.Pago;

@Database(
        entities = {Cliente.class, Cancha.class, Disciplina.class,
                Reserva.class, ReservaDetalle.class, Pago.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ClienteDao clienteDao();
    public abstract CanchaDao canchaDao();
    public abstract ReservaDao reservaDao();
    public abstract PagoDao pagoDao();

    private static volatile AppDatabase INSTANCE;

    // Pool de hilos compartido para acceso a datos
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "reserva_canchas_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}