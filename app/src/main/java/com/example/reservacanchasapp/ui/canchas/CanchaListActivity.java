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
            Intent intent = new Intent(this, NuevaReservaActivity.class);
            intent.putExtra("idCancha", cancha.getIdCancha());
            startActivity(intent);
        });

        binding.rvCanchas.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCanchas.setAdapter(adapter);

        viewModel.obtenerTodasLasCanchas().observe(this,
                lista -> adapter.actualizarLista(lista));
    }
}