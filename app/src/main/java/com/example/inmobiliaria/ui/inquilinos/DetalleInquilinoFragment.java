//MVVM

package com.example.inmobiliaria.ui.inquilinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.R;

public class DetalleInquilinoFragment extends Fragment {

    private TextView tvNombre, tvApellido, tvDni, tvTelefono, tvEmail;
    private DetalleInquilinoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_inquilino, container, false);

        tvNombre = view.findViewById(R.id.tvNombre);
        tvApellido = view.findViewById(R.id.tvApellido);
        tvDni = view.findViewById(R.id.tvDni);
        tvTelefono = view.findViewById(R.id.tvTelefono);
        tvEmail = view.findViewById(R.id.tvEmail);

        viewModel = new ViewModelProvider(this).get(DetalleInquilinoViewModel.class);

        // 🔹 Solo observa los datos
        viewModel.getInquilino().observe(getViewLifecycleOwner(), inquilino -> {
            tvNombre.setText(inquilino.getNombre());
            tvApellido.setText(inquilino.getApellido());
            tvDni.setText(inquilino.getDni());
            tvTelefono.setText(inquilino.getTelefono());
            tvEmail.setText(inquilino.getEmail());
        });

        // 🔹 Delegamos completamente al ViewModel la carga desde argumentos
        viewModel.cargarDesdeBundle(getArguments());

        return view;
    }
}
