package com.example.reservacanchasapp.ui.reservas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.reservacanchasapp.data.AppDatabase;
import com.example.reservacanchasapp.data.dao.ReservaConDetalles;
import com.example.reservacanchasapp.data.entity.DetalleConNombres;
import com.example.reservacanchasapp.data.entity.Reserva;
import com.example.reservacanchasapp.data.entity.ReservaDetalle;
import com.example.reservacanchasapp.viewmodel.ReservaViewModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservaListActivity extends AppCompatActivity {
    private LinearLayout lista;
    private ReservaViewModel viewModel;
    private AppDatabase database;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setTitle("Historial de reservas");
        int idCliente = getIntent().getIntExtra("idCliente", -1);
        if (idCliente < 0) { finish(); return; }
        ScrollView scroll = new ScrollView(this);
        lista = new LinearLayout(this); lista.setOrientation(LinearLayout.VERTICAL); lista.setPadding(18, 12, 18, 12);
        scroll.addView(lista); setContentView(scroll);
        database = AppDatabase.getDatabase(getApplicationContext());
        viewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        viewModel.obtenerHistorial(idCliente).observe(this, reservas -> {
            lista.removeAllViews();
            if (reservas == null || reservas.isEmpty()) {
                TextView vacio = new TextView(this); vacio.setText("Este cliente todavía no tiene reservas."); vacio.setTextSize(17); vacio.setPadding(8, 20, 8, 20); lista.addView(vacio); return;
            }
            for (ReservaConDetalles reserva : reservas) mostrarReserva(reserva);
        });
    }

    private void mostrarReserva(ReservaConDetalles item) {
        Reserva reserva = item.reserva;
        LinearLayout card = new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setPadding(14, 12, 14, 12);
        TextView encabezado = new TextView(this);
        encabezado.setText("Reserva #" + reserva.getIdReserva() + " · " + reserva.getFechaReserva() + "\nEstado: " + reserva.getEstado() + " · Total: Bs. " + String.format(Locale.getDefault(), "%.2f", reserva.getTotalReserva()));
        encabezado.setTextSize(17); encabezado.setTextIsSelectable(true); card.addView(encabezado);
        TextView cargando = new TextView(this); cargando.setText("Cargando detalles…"); card.addView(cargando);
        if ("Pendiente".equals(reserva.getEstado())) {
            Button cancelar = new Button(this); cancelar.setText("Cancelar reserva");
            cancelar.setOnClickListener(v -> new AlertDialog.Builder(this).setMessage("¿Cancelar esta reserva?")
                    .setNegativeButton("Volver", null).setPositiveButton("Cancelar reserva", (dialog, which) ->
                            AppDatabase.databaseWriteExecutor.execute(() -> database.reservaDao().cambiarEstado(reserva.getIdReserva(), "Cancelada"))).show());
            card.addView(cancelar);
        }
        lista.addView(card);
        View divider = new View(this); divider.setBackgroundColor(0xFFCCCCCC); lista.addView(divider, new LinearLayout.LayoutParams(-1, 1));
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<DetalleConNombres> nombres = database.reservaDao().obtenerDetalleConNombres(reserva.getIdReserva());
            runOnUiThread(() -> {
                card.removeView(cargando);
                if (nombres.isEmpty()) { TextView vacio = new TextView(this); vacio.setText("Sin detalles"); card.addView(vacio); return; }
                for (DetalleConNombres detalle : nombres) {
                    ReservaDetalle registro = null;
                    for (ReservaDetalle candidato : item.detalles) if (candidato.getIdReservaDetalle() == detalle.idReservaDetalle) { registro = candidato; break; }
                    LinearLayout fila = new LinearLayout(this); fila.setOrientation(LinearLayout.VERTICAL); fila.setPadding(6, 10, 6, 10);
                    TextView info = new TextView(this);
                    info.setText(detalle.nombreCancha + " · " + detalle.nombreDisciplina + "\n" + detalle.fechaJuego + "  " + detalle.horaInicio + "–" + detalle.horaFin + "\nSubtotal: Bs. " + String.format(Locale.getDefault(), "%.2f", detalle.subtotal));
                    fila.addView(info);
                    if (registro != null && "Pendiente".equals(reserva.getEstado()) && detalle.pagosPagados == 0) {
                        ReservaDetalle pagarDetalle = registro;
                        Button pagar = new Button(this); pagar.setText("Registrar pago");
                        pagar.setOnClickListener(v -> new AlertDialog.Builder(this).setTitle("Método de pago")
                                .setItems(new String[]{"Efectivo", "QR", "Tarjeta"}, (dialog, which) -> {
                                    String[] metodos = {"Efectivo", "QR", "Tarjeta"};
                                    String fechaPago = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
                                    viewModel.registrarPago(reserva.getIdReserva(), pagarDetalle, metodos[which], fechaPago);
                                }).show());
                        fila.addView(pagar);
                    } else if (detalle.pagosPagados > 0) {
                        TextView pagado = new TextView(this); pagado.setText("Pago registrado"); fila.addView(pagado);
                    }
                    card.addView(fila);
                }
            });
        });
    }
}
