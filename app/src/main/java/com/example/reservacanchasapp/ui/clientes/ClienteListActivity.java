package com.example.reservacanchasapp.ui.clientes;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.reservacanchasapp.data.AppDatabase;
import com.example.reservacanchasapp.data.entity.Cliente;
import com.example.reservacanchasapp.ui.canchas.CanchaListActivity;
import com.example.reservacanchasapp.ui.reservas.ReservaListActivity;
import com.example.reservacanchasapp.viewmodel.ClienteViewModel;

public class ClienteListActivity extends AppCompatActivity {
    private LinearLayout lista;
    private ClienteViewModel viewModel;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        boolean paraReserva = getIntent().getBooleanExtra("seleccionarCliente", false);
        boolean paraHistorial = getIntent().getBooleanExtra("seleccionarHistorial", false);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20, 16, 20, 12);
        TextView title = new TextView(this);
        title.setText(paraReserva ? "Elige un cliente para la reserva" : paraHistorial ? "Elige un cliente para ver sus reservas" : "Clientes");
        title.setTextSize(24);
        root.addView(title);
        Button agregar = new Button(this);
        agregar.setText("Registrar cliente");
        agregar.setOnClickListener(v -> mostrarFormulario(null));
        root.addView(agregar);
        ScrollView scroll = new ScrollView(this);
        lista = new LinearLayout(this);
        lista.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(lista);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);

        viewModel = new ViewModelProvider(this).get(ClienteViewModel.class);
        viewModel.obtenerTodosLosClientes().observe(this, clientes -> {
            lista.removeAllViews();
            if (clientes == null || clientes.isEmpty()) {
                TextView vacio = new TextView(this);
                vacio.setText("Todavía no hay clientes. Registra uno para crear una reserva.");
                vacio.setPadding(8, 24, 8, 24);
                lista.addView(vacio);
                return;
            }
            for (Cliente cliente : clientes) agregarFila(cliente, paraReserva, paraHistorial);
        });
    }

    private void agregarFila(Cliente cliente, boolean paraReserva, boolean paraHistorial) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(14, 10, 14, 10);
        TextView info = new TextView(this);
        info.setText(cliente.getNombre() + " " + cliente.getApellido() + "\n" + cliente.getTelefono() + " · " + cliente.getEmail());
        info.setTextSize(16);
        card.addView(info);
        LinearLayout acciones = new LinearLayout(this);
        if (paraReserva || paraHistorial) {
            Button elegir = new Button(this);
            elegir.setText(paraReserva ? "Elegir" : "Ver reservas");
            elegir.setOnClickListener(v -> {
                Intent intent = new Intent(this, paraReserva ? CanchaListActivity.class : ReservaListActivity.class);
                intent.putExtra("idCliente", cliente.getIdCliente());
                startActivity(intent);
            });
            acciones.addView(elegir, new LinearLayout.LayoutParams(0, -2, 1));
        } else {
            Button reservar = new Button(this);
            reservar.setText("Reservar");
            reservar.setOnClickListener(v -> {
                Intent intent = new Intent(this, CanchaListActivity.class);
                intent.putExtra("idCliente", cliente.getIdCliente());
                startActivity(intent);
            });
            acciones.addView(reservar, new LinearLayout.LayoutParams(0, -2, 1));
            Button historial = new Button(this);
            historial.setText("Historial");
            historial.setOnClickListener(v -> {
                Intent intent = new Intent(this, ReservaListActivity.class);
                intent.putExtra("idCliente", cliente.getIdCliente());
                startActivity(intent);
            });
            acciones.addView(historial, new LinearLayout.LayoutParams(0, -2, 1));
            Button editar = new Button(this);
            editar.setText("Editar");
            editar.setOnClickListener(v -> mostrarFormulario(cliente));
            acciones.addView(editar, new LinearLayout.LayoutParams(0, -2, 1));
            Button eliminar = new Button(this);
            eliminar.setText("Borrar");
            eliminar.setOnClickListener(v -> new AlertDialog.Builder(this)
                    .setTitle("Eliminar cliente")
                    .setMessage("¿Eliminar a " + cliente.getNombre() + " " + cliente.getApellido() + "? También se borrarán sus reservas.")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Eliminar", (dialog, which) -> AppDatabase.databaseWriteExecutor.execute(() -> AppDatabase.getDatabase(getApplicationContext()).clienteDao().eliminar(cliente)))
                    .show());
            acciones.addView(eliminar, new LinearLayout.LayoutParams(0, -2, 1));
        }
        card.addView(acciones);
        ViewGroup.LayoutParams divider = new ViewGroup.LayoutParams(-1, 1);
        lista.addView(card);
        android.view.View line = new android.view.View(this);
        line.setBackgroundColor(0xFFDDDDDD);
        lista.addView(line, divider);
    }

    private void mostrarFormulario(Cliente existente) {
        LinearLayout formulario = new LinearLayout(this);
        formulario.setOrientation(LinearLayout.VERTICAL);
        formulario.setPadding(20, 4, 20, 4);
        EditText nombre = campo("Nombre", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS, existente == null ? "" : existente.getNombre());
        EditText apellido = campo("Apellido", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS, existente == null ? "" : existente.getApellido());
        EditText email = campo("Correo electrónico", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, existente == null ? "" : existente.getEmail());
        EditText telefono = campo("Teléfono", InputType.TYPE_CLASS_PHONE, existente == null ? "" : existente.getTelefono());
        formulario.addView(nombre); formulario.addView(apellido); formulario.addView(email); formulario.addView(telefono);
        new AlertDialog.Builder(this).setTitle(existente == null ? "Nuevo cliente" : "Editar cliente")
                .setView(formulario).setNegativeButton("Cancelar", null)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String n = nombre.getText().toString().trim(), a = apellido.getText().toString().trim();
                    String e = email.getText().toString().trim(), t = telefono.getText().toString().trim();
                    if (n.isEmpty() || a.isEmpty() || e.isEmpty() || t.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                        Toast.makeText(this, "Completa todos los campos con un correo válido.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (existente == null) viewModel.insertar(new Cliente(n, a, e, t));
                    else {
                        existente.setNombre(n); existente.setApellido(a); existente.setEmail(e); existente.setTelefono(t);
                        AppDatabase.databaseWriteExecutor.execute(() -> AppDatabase.getDatabase(getApplicationContext()).clienteDao().actualizar(existente));
                    }
                }).show();
    }

    private EditText campo(String hint, int type, String valor) {
        EditText input = new EditText(this); input.setHint(hint); input.setInputType(type); input.setText(valor); return input;
    }
}
