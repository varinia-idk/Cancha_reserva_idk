package com.example.reservacanchasapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.reservacanchasapp.data.entity.Cancha;

import java.util.List;

@Dao
public interface CanchaDao {

    @Insert
    long insertar(Cancha canchas);

    @Query("SELECT * FROM canchas WHERE estado = 'Activa' ORDER BY nombre")
    LiveData<List<Cancha>> obtenerCanchas();

    @Query("SELECT * FROM canchas WHERE idCancha = :id")
    Cancha obtenerPorId(int id);

    @Query("SELECT * FROM canchas WHERE estado = 'Activa' ORDER BY nombre")
    List<Cancha> obtenerActivasAhora();

    @Query("SELECT COUNT(*) FROM canchas")
    int contarTodas();
}
