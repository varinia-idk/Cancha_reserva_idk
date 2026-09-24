package com.example.reservacanchasapp.ui.reservas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.reservacanchasapp.data.AppDatabase;
import com.example.reservacanchasapp.data.entity.Cancha;
import com.example.reservacanchasapp.data.entity.Cliente;
import com.example.reservacanchasapp.data.entity.Disciplina;
import com.example.reservacanchasapp.data.entity.Reserva;
import com.example.reservacanchasapp.data.entity.ReservaDetalle;
import com.example.reservacanchasapp.viewmodel.ReservaViewModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NuevaReservaActivity extends AppCompatActivity {
    private Spinner disciplinaSpinner;
    private TextView canchaLabel, fechaLabel, inicioLabel, finLabel, totalLabel;
    private final Calendar fecha = Calendar.getInstance();
    private String horaInicio, horaFin;
    private Cancha cancha;
    private Cliente cliente;
    private List<Disciplina> disciplinas = new ArrayList<>();
    private ReservaViewModel viewModel;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setTitle("Nueva reserva");
        int idCancha = getIntent().getIntExtra("idCancha", -1);
        int idCliente = getIntent().getIntExtra("idCliente", -1);
        if (idCancha < 0 || idCliente < 0) { finish(); return; }

        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(24, 20, 24, 20);
        ScrollView scroll = new ScrollView(this); scroll.addView(form); setContentView(scroll);
        canchaLabel = texto(form, "Cargando cancha…", 20);
        disciplinaSpinner = new Spinner(this); form.addView(disciplinaSpinner);
        Button elegirFecha = new Button(this); elegirFecha.setText("Elegir fecha"); form.addView(elegirFecha);
        fechaLabel = texto(form, "", 16);
        Button elegirInicio = new Button(this); elegirInicio.setText("Hora de inicio"); form.addView(elegirInicio);
        inicioLabel = texto(form, "Hora de inicio: —", 16);
        Button elegirFin = new Button(this); elegirFin.setText("Hora de fin"); form.addView(elegirFin);
        finLabel = texto(form, "Hora de fin: —", 16);
        totalLabel = texto(form, "Total: —", 18);
        Button guardar = new Button(this); guardar.setText("Guardar reserva"); form.addView(guardar);

        fecha.setTimeInMillis(System.currentTimeMillis());
        actualizarFecha();
        elegirFecha.setOnClickListener(v -> new DatePickerDialog(this, (picker, year, month, day) -> {
            fecha.set(year, month, day); actualizarFecha();
        }, fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH), fecha.get(Calendar.DAY_OF_MONTH)).show());
        elegirInicio.setOnClickListener(v -> elegirHora(true));
        elegirFin.setOnClickListener(v -> elegirHora(false));

        viewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        viewModel.reservaCreadaId.observe(this, id -> {
            if (id != null && id > 0) {
                Toast.makeText(this, "Reserva #" + id + " guardada", Toast.LENGTH_LONG).show();
                finish();
            } else if (id != null) Toast.makeText(this, "Ese horario ya está ocupado o no se pudo guardar la reserva.", Toast.LENGTH_LONG).show();
        });

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        db.disciplinaDao().obtenerTodas().observe(this, values -> {
            disciplinas = values == null ? new ArrayList<>() : values;
            List<String> nombres = new ArrayList<>();
            for (Disciplina d : disciplinas) nombres.add(d.getNombre());
            disciplinaSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nombres));
        });
        db.canchaDao().obtenerCanchas().observe(this, values -> {
            if (values != null) for (Cancha value : values) if (value.getIdCancha() == idCancha) cancha = value;
            if (cancha != null) { canchaLabel.setText(cancha.getNombre() + " · " + cancha.getTipo()); actualizarTotal(); }
        });
        db.clienteDao().obtenerTodos().observe(this, values -> {
            if (values != null) for (Cliente value : values) if (value.getIdCliente() == idCliente) cliente = value;
        });
        disciplinaSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) { actualizarTotal(); }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
        guardar.setOnClickListener(v -> guardarReserva());
    }

    private TextView texto(LinearLayout root, String value, float size) {
        TextView text = new TextView(this); text.setText(value); text.setTextSize(size); text.setPadding(4, 12, 4, 12); root.addView(text); return text;
    }

    private void actualizarFecha() {
        fechaLabel.setText("Fecha de juego: " + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(fecha.getTime()));
    }

    private void elegirHora(boolean inicio) {
        Calendar now = Calendar.getInstance();
        new TimePickerDialog(this, (picker, hour, minute) -> {
            String value = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
            if (inicio) { horaInicio = value; inicioLabel.setText("Hora de inicio: " + value); }
            else { horaFin = value; finLabel.setText("Hora de fin: " + value); }
            actualizarTotal();
        }, now.get(Calendar.HOUR_OF_DAY), 0, true).show();
    }

    private double horasSeleccionadas() {
        if (horaInicio == null || horaFin == null) return -1;
        String[] desde = horaInicio.split(":"); String[] hasta = horaFin.split(":");
        int minutos = (Integer.parseInt(hasta[0]) * 60 + Integer.parseInt(hasta[1])) - (Integer.parseInt(desde[0]) * 60 + Integer.parseInt(desde[1]));
        return minutos / 60.0;
    }

    private void actualizarTotal() {
        double horas = horasSeleccionadas();
        if (cancha != null && horas > 0) totalLabel.setText(String.format(Locale.getDefault(), "Total: Bs. %.2f (%.1f h)", cancha.getPrecioBase() * horas, horas));
        else totalLabel.setText("Total por hora: Bs. " + (cancha == null ? "—" : String.format(Locale.getDefault(), "%.2f", cancha.getPrecioBase())));
    }

    private void guardarReserva() {
        double horas = horasSeleccionadas();
        int posicion = disciplinaSpinner.getSelectedItemPosition();
        if (cliente == null || cancha == null || disciplinas.isEmpty() || posicion < 0 || horaInicio == null || horaFin == null || horas <= 0) {
            Toast.makeText(this, "Selecciona disciplina, fecha y un horario válido.", Toast.LENGTH_LONG).show(); return;
        }
        if (fecha.getTimeInMillis() < inicioDelDia(Calendar.getInstance()).getTimeInMillis()) {
            Toast.makeText(this, "La fecha debe ser hoy o posterior.", Toast.LENGTH_LONG).show(); return;
        }
        double unitario = cancha.getPrecioBase(); double subtotal = unitario * horas;
        String hoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String fechaJuego = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(fecha.getTime());
        Reserva reserva = new Reserva(cliente.getIdCliente(), hoy, subtotal);
        ReservaDetalle detalle = new ReservaDetalle(0, cancha.getIdCancha(), disciplinas.get(posicion).getIdDisciplina(), fechaJuego, horaInicio, horaFin, unitario, subtotal);
        List<ReservaDetalle> detalles = new ArrayList<>(); detalles.add(detalle);
        viewModel.crearReserva(reserva, detalles);
    }

    private Calendar inicioDelDia(Calendar source) {
        Calendar day = (Calendar) source.clone(); day.set(Calendar.HOUR_OF_DAY, 0); day.set(Calendar.MINUTE, 0); day.set(Calendar.SECOND, 0); day.set(Calendar.MILLISECOND, 0); return day;
    }
}
