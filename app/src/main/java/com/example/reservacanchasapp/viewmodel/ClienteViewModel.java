package com.example.reservacanchasapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import com.example.reservacanchasapp.data.entity.Cliente;
import com.example.reservacanchasapp.data.repository.ClienteRepository;

public class ClienteViewModel extends AndroidViewModel {

    private final ClienteRepository repository;
    private final LiveData<List<Cliente>> todosLosClientes;

    public ClienteViewModel(@NonNull Application application) {
        super(application);
        repository = new ClienteRepository(application);
        todosLosClientes = repository.obtenerTodosLosClientes();
    }

    public LiveData<List<Cliente>> obtenerTodosLosClientes() {
        return todosLosClientes;
    }

    public void insertar(Cliente cliente) {
        repository.insertar(cliente);
    }
}