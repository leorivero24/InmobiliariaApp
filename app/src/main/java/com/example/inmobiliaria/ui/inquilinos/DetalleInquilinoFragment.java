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
import com.example.inmobiliaria.databinding.FragmentDetalleInquilinoBinding;

public class DetalleInquilinoFragment extends Fragment {

    private TextView tvNombre, tvApellido, tvDni, tvTelefono, tvEmail;
    private DetalleInquilinoViewModel viewModel;

    private FragmentDetalleInquilinoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_inquilino, container, false);



        viewModel = new ViewModelProvider(this).get(DetalleInquilinoViewModel.class);

        // 6- Observa los datos y los muestra en pantalla:
        viewModel.getInquilino().observe(getViewLifecycleOwner(), inquilino -> {

            binding.tvNombre.setText(inquilino.getNombre());
            binding.tvApellido.setText(inquilino.getApellido());
            binding.tvDni.setText(inquilino.getDni());
            binding.tvTelefono.setText(inquilino.getTelefono());
            binding.tvEmail.setText(inquilino.getEmail());


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
