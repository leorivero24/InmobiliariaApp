
package com.example.inmobiliaria.ui.inmuebles;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.ui.detalle_inmueble.DetalleInmuebleFragment;

import java.util.List;

public class InmueblesAdapter extends RecyclerView.Adapter<InmueblesAdapter.InmuebleViewHolder> {

    private List<Inmueble> lista;
    private Context context;

    public InmueblesAdapter(List<Inmueble> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inmueble_card, parent, false);
        return new InmuebleViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {
        Inmueble inmueble = lista.get(position);

        holder.tvDireccion.setText(inmueble.getDireccion());
        holder.tvValor.setText("$" + inmueble.getValor());

        //  Cargar imagen con Glide (manejo de errores y placeholder)
        String urlImagen = ApiClient.BASE_URL + inmueble.getImagen().replace("\\", "/");
        Glide.with(context)
                .load(urlImagen)
                .placeholder(R.drawable.house_placeholder) // usa un ícono que tengas
                .error(R.drawable.house_placeholder)        // mismo si falla
                .centerCrop()
                .into(holder.imgInmueble);

        //  Click para abrir DetalleInmuebleFragment (navegación manual)
        holder.cardView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("inmueble", inmueble);

            Fragment detalleFragment = new DetalleInmuebleFragment();
            detalleFragment.setArguments(bundle);

            FragmentActivity activity = (FragmentActivity) v.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, detalleFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setInmuebles(List<Inmueble> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    public static class InmuebleViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDireccion;
        private final TextView tvValor;
        private final ImageView imgInmueble;
        private final CardView cardView;

        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvValor = itemView.findViewById(R.id.tvPrecio);
            imgInmueble = itemView.findViewById(R.id.imgInmueble);
            cardView = itemView.findViewById(R.id.idCard); // asegurate que este id exista en el layout
        }
    }
}
