package com.example.reservacanchasapp.ui.canchas;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.reservacanchasapp.data.entity.Cancha;
import com.example.reservacanchasapp.databinding.ItemCanchaBinding;

public class CanchaAdapter
        extends RecyclerView.Adapter<CanchaAdapter.CanchaViewHolder> {

    public interface OnCanchaClickListener {
        void onClick(Cancha cancha);
    }

    private List<Cancha> canchas;
    private final OnCanchaClickListener listener;

    public CanchaAdapter(List<Cancha> canchas, OnCanchaClickListener listener) {
        this.canchas = canchas;
        this.listener = listener;
    }

    static class CanchaViewHolder extends RecyclerView.ViewHolder {
        ItemCanchaBinding binding;

        CanchaViewHolder(ItemCanchaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public CanchaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        ItemCanchaBinding binding = ItemCanchaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CanchaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CanchaViewHolder holder,
                                 int position) {
        Cancha cancha = canchas.get(position);
        holder.binding.tvNombreCancha.setText(cancha.getNombre());
        holder.binding.tvTipoCancha.setText(cancha.getTipo());
        holder.binding.tvPrecioCancha.setText("Bs. " + cancha.getPrecioBase());
        holder.itemView.setOnClickListener(v -> listener.onClick(cancha));
    }

    @Override
    public int getItemCount() {
        return canchas != null ? canchas.size() : 0;
    }

    public void actualizarLista(List<Cancha> nuevaLista) {
        this.canchas = nuevaLista;
        notifyDataSetChanged();
    }
}