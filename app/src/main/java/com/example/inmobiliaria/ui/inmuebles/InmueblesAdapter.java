


package com.example.inmobiliaria.ui.inmuebles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import java.util.List;

public class InmueblesAdapter extends RecyclerView.Adapter<InmueblesAdapter.ViewHolder> {

    private Context context;
    private List<Inmueble> inmuebles;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Inmueble inmueble);
    }

    public InmueblesAdapter(Context context, List<Inmueble> inmuebles, OnItemClickListener listener) {
        this.context = context;
        this.inmuebles = inmuebles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inmueble_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = inmuebles.get(position);
        holder.bind(inmueble, listener);
    }

    @Override
    public int getItemCount() {
        return inmuebles.size();
    }

    public void setInmuebles(List<Inmueble> lista) {
        this.inmuebles = lista;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImagen;
        TextView tvDireccion, tvValor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.imgInmueble);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvValor = itemView.findViewById(R.id.tvPrecio);
        }

        public void bind(Inmueble inmueble, OnItemClickListener listener) {
            tvDireccion.setText(inmueble.getDireccion());
            tvValor.setText("$" + inmueble.getValor());

            // URL de la imagen
            String urlImagen = ApiClient.BASE_URL + inmueble.getImagen().replace("\\", "/");

            Glide.with(itemView.getContext())
                    .load(urlImagen)
                    .placeholder(R.drawable.house_placeholder)
                    .centerCrop()
                    .into(ivImagen);

            itemView.setOnClickListener(v -> listener.onItemClick(inmueble));
        }
    }
}

//
//package com.example.inmobiliaria.ui.inmuebles;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.inmobiliaria.R;
//import com.example.inmobiliaria.modelo.Inmueble;
//
//import java.util.List;
//
//public class InmueblesAdapter extends RecyclerView.Adapter<InmueblesAdapter.ViewHolder> {
//
//    private Context context;
//    private List<Inmueble> inmuebles;
//    private OnItemClickListener listener;
//
//    public interface OnItemClickListener {
//        void onItemClick(Inmueble inmueble);
//    }
//
//    public InmueblesAdapter(Context context, List<Inmueble> inmuebles, OnItemClickListener listener) {
//        this.context = context;
//        this.inmuebles = inmuebles;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_inmueble_card, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.bind(inmuebles.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return inmuebles != null ? inmuebles.size() : 0;
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tvDireccion, tvUso;
//        ImageView imgInmueble;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvDireccion = itemView.findViewById(R.id.tvDireccion);
//            tvUso = itemView.findViewById(R.id.tvPrecio);
//            imgInmueble = itemView.findViewById(R.id.imgInmueble);
//        }
//
//        void bind(final Inmueble inmueble) {
//            // Validaciones contra null
//            tvDireccion.setText(inmueble.getDireccion() != null ? inmueble.getDireccion() : "Sin dirección");
//            tvUso.setText(inmueble.getUso() != null ? inmueble.getUso() : "Sin uso");
//
//            // Imagen
//            String imagenUrl = (inmueble.getImagen() != null) ? inmueble.getImagen().replace("\\", "/") : "";
//            Glide.with(context)
//                    .load(imagenUrl.isEmpty() ? R.drawable.house_placeholder
//                            : "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/" + imagenUrl)
//                    .placeholder(R.drawable.house_placeholder)
//                    .into(imgInmueble);
//
//
//            // Click
//            itemView.setOnClickListener(v -> {
//                if (listener != null) {
//                    listener.onItemClick(inmueble);
//                }
//            });
//        }
//    }
//}

