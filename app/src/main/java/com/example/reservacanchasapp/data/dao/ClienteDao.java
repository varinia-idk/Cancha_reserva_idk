package com.example.reservacanchasapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.reservacanchasapp.data.entity.Cliente;

import java.util.List;

@Dao
public interface ClienteDao {

    @Insert
    long insertar(Cliente cliente);

    @Update
    void actualizar(Cliente cliente);

    @Delete
    void eliminar(Cliente cliente);

    @Query("SELECT * FROM clientes ORDER BY apellido ASC")
    LiveData<List<Cliente>> obtenerTodos();

    @Query("SELECT * FROM clientes WHERE idCliente = :id")
    Cliente obtenerPorId(int id);

    @Query("SELECT * FROM clientes WHERE email = :email LIMIT 1")
    Cliente buscarPorEmail(String email);
}