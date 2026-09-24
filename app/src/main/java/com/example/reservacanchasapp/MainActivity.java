package com.example.reservacanchasapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reservacanchasapp.ui.canchas.CanchaListActivity;
import com.example.reservacanchasapp.ui.clientes.ClienteListActivity;
import com.example.reservacanchasapp.ui.reservas.NuevaReservaActivity;
import com.example.reservacanchasapp.ui.reservas.ReservaListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCanchas = findViewById(R.id.btnCanchas);
        Button btnClientes = findViewById(R.id.btnClientes);
        Button btnReservas = findViewById(R.id.btnReservas);
        Button btnNuevaReserva = findViewById(R.id.btnNuevaReserva);

        btnCanchas.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CanchaListActivity.class))
        );

        btnClientes.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ClienteListActivity.class))
        );

        btnReservas.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ReservaListActivity.class))
        );

        btnNuevaReserva.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, NuevaReservaActivity.class))
        );
    }
}