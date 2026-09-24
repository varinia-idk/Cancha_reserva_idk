package com.example.reservacanchasapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.reservacanchasapp.data.entity.Disciplina;
import java.util.List;

@Dao
public interface DisciplinaDao {
    @Insert long insertar(Disciplina disciplina);
    @Query("SELECT * FROM disciplinas ORDER BY nombre") LiveData<List<Disciplina>> obtenerTodas();
    @Query("SELECT * FROM disciplinas ORDER BY nombre") List<Disciplina> obtenerTodasAhora();
    @Query("SELECT COUNT(*) FROM disciplinas") int contarTodas();
}
