//MVVM

package com.example.inmobiliaria.ui.pagos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Pago;
import java.util.List;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.ViewHolder> {

    private List<Pago> pagos;

    public PagosAdapter(List<Pago> pagos) {
        this.pagos = pagos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pago, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pago pago = pagos.get(position);
        holder.tvFecha.setText("Fecha: " + pago.getFechaPago());
        holder.tvMonto.setText("Monto: $" + pago.getMonto());
        holder.tvDetalle.setText("Detalle: " + pago.getDetalle());
        holder.tvEstado.setText("Estado: " + (pago.isEstado() ? "Pagado" : "Pendiente"));
    }

    @Override
    public int getItemCount() {
        return pagos != null ? pagos.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvMonto, tvDetalle, tvEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFechaPago);
            tvMonto = itemView.findViewById(R.id.tvMontoPago);
            tvDetalle = itemView.findViewById(R.id.tvDetallePago);
            tvEstado = itemView.findViewById(R.id.tvEstadoPago);
        }
    }
}


