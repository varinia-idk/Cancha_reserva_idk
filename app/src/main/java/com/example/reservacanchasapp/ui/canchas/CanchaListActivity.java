package com.example.reservacanchasapp.ui.canchas;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import com.example.reservacanchasapp.databinding.ActivityCanchaListBinding;
import com.example.reservacanchasapp.viewmodel.CanchaViewModel;
import com.example.reservacanchasapp.ui.reservas.NuevaReservaActivity;

public class CanchaListActivity extends AppCompatActivity {

    private ActivityCanchaListBinding binding;
    private CanchaViewModel viewModel;
    private CanchaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCanchaListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(CanchaViewModel.class);

        adapter = new CanchaAdapter(new ArrayList<>(), cancha -> {
            int idCliente = getIntent().getIntExtra("idCliente", -1);
            if (idCliente < 0) {
                Intent elegirCliente = new Intent(this, com.example.reservacanchasapp.ui.clientes.ClienteListActivity.class);
                elegirCliente.putExtra("seleccionarCliente", true);
                startActivity(elegirCliente);
                return;
            }
            Intent intent = new Intent(this, NuevaReservaActivity.class);
            intent.putExtra("idCancha", cancha.getIdCancha());
            intent.putExtra("idCliente", idCliente);
            startActivity(intent);
        });

        binding.rvCanchas.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCanchas.setAdapter(adapter);

        viewModel.obtenerTodasLasCanchas().observe(this,
                lista -> {
                    adapter.actualizarLista(lista);
                    if (lista == null || lista.isEmpty()) {
                        android.widget.Toast.makeText(this, "No hay canchas activas disponibles.", android.widget.Toast.LENGTH_LONG).show();
                    }
                });
    }
}
