package com.example.inmobiliaria.ui.contratos;

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

public class ContratosAdapter extends RecyclerView.Adapter<ContratosAdapter.ViewHolder> {

    public interface OnVerClickListener {
        void onVerClick(Contrato contrato);
    }

    private Context context;
    private List<Contrato> contratos;
    private OnVerClickListener listener;

    public ContratosAdapter(Context context, List<Contrato> contratos, OnVerClickListener listener) {
        this.context = context;
        this.contratos = contratos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contrato_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(contratos.get(position), listener);
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

        ImageView ivInmueble;
        TextView tvDireccion, tvTipo, tvValor;
        Button btnVer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivInmueble = itemView.findViewById(R.id.imgInmuebleContrato);
            tvDireccion = itemView.findViewById(R.id.tvDireccionContrato);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvValor = itemView.findViewById(R.id.tvValor);
            btnVer = itemView.findViewById(R.id.btnVerContrato);
        }

        public void bind(Contrato contrato, OnVerClickListener listener) {
            tvDireccion.setText(contrato.getInmueble().getDireccion());
            tvTipo.setText("Tipo: " + contrato.getInmueble().getTipo());
            tvValor.setText("Valor: $" + contrato.getInmueble().getValor());

            String url = ApiClient.BASE_URL + contrato.getInmueble().getImagen().replace("\\", "/");
            Glide.with(itemView.getContext())
                    .load(url)
                    .placeholder(R.drawable.house_placeholder)
                    .into(ivInmueble);

            btnVer.setOnClickListener(v -> {
                if (listener != null) listener.onVerClick(contrato);
            });
        }
    }
}
