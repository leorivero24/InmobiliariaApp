package com.example.inmobiliaria.ui.inquilinos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.ItemInquilinosCardBinding;
import com.example.inmobiliaria.modelo.Contrato;
import com.example.inmobiliaria.request.ApiClient;

import java.util.List;

public class InquilinosAdapter extends RecyclerView.Adapter<InquilinosAdapter.ViewHolder> {

    private final Context context;
    private List<Contrato> contratos;
    private final OnVerClickListener listener;

    public interface OnVerClickListener {
        void onVerClick(Contrato contrato);
    }

    public InquilinosAdapter(Context context, List<Contrato> contratos, OnVerClickListener listener) {
        this.context = context;
        this.contratos = contratos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemInquilinosCardBinding binding = ItemInquilinosCardBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contrato contrato = contratos.get(position);
        holder.bind(contrato, listener);
    }

    @Override
    public int getItemCount() {
        return contratos != null ? contratos.size() : 0;
    }

    public void setContratos(List<Contrato> lista) {
        this.contratos = lista;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemInquilinosCardBinding binding;

        public ViewHolder(@NonNull ItemInquilinosCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Contrato contrato, OnVerClickListener listener) {
            binding.tvDireccion.setText(contrato.getInmueble().getDireccion());
            binding.tvTipo.setText("Tipo: " + contrato.getInmueble().getTipo());
            binding.tvValor.setText("Valor: $" + contrato.getInmueble().getValor());

            String urlImagen = ApiClient.BASE_URL + contrato.getInmueble().getImagen().replace("\\", "/");
            Glide.with(binding.getRoot().getContext())
                    .load(urlImagen)
                    .placeholder(R.drawable.house_placeholder)
                    .centerCrop()
                    .into(binding.imgInmueble);

            // 1- Usuario toca "Ver" en un inquilino, Cuando el usuario toca el botón Ver, se avisa al listener:
            binding.btnVer.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVerClick(contrato);
                }
            });
        }
    }
}
