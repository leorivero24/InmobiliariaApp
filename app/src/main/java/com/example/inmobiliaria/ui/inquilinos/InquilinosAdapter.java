package com.example.inmobiliaria.ui.inquilinos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Contrato;
import com.example.inmobiliaria.request.ApiClient;

import java.util.List;

public class InquilinosAdapter extends RecyclerView.Adapter<InquilinosAdapter.ViewHolder> {

    private Context context;
    private List<Contrato> contratos;
    private OnVerClickListener listener;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_inquilinos_card, parent, false);
        return new ViewHolder(view);
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

        ImageView ivImagen;
        TextView tvDireccion,tvTipo,tvValor;
        Button btnVer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.imgInmueble);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvValor = itemView.findViewById(R.id.tvValor);
            btnVer = itemView.findViewById(R.id.btnVer);
        }

        public void bind(Contrato contrato, OnVerClickListener listener) {
            tvDireccion.setText(contrato.getInmueble().getDireccion());
            tvTipo.setText("Tipo: " + contrato.getInmueble().getTipo());
            tvValor.setText("Valor: $" + contrato.getInmueble().getValor());


            String urlImagen = ApiClient.BASE_URL + contrato.getInmueble().getImagen().replace("\\", "/");
            Glide.with(itemView.getContext())
                    .load(urlImagen)
                    .placeholder(R.drawable.house_placeholder)
                    .centerCrop()
                    .into(ivImagen);

            btnVer.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVerClick(contrato);
                }
            });
        }
    }
}
