package com.example.reservacanchasapp.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

import com.example.reservacanchasapp.data.AppDatabase;
import com.example.reservacanchasapp.data.dao.ClienteDao;
import com.example.reservacanchasapp.data.entity.Cliente;

public class ClienteRepository {

    private final ClienteDao clienteDao;
    private final LiveData<List<Cliente>> todosLosClientes;

    public ClienteRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.clienteDao = db.clienteDao();
        this.todosLosClientes = clienteDao.obtenerTodos();
    }

    public LiveData<List<Cliente>> obtenerTodosLosClientes() {
        return todosLosClientes;
    }

    public void insertar(Cliente cliente) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            clienteDao.insertar(cliente);
        });
    }
}