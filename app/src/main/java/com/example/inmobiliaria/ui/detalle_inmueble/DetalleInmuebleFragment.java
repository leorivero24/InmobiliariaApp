package com.example.inmobiliaria.ui.detalle_inmueble;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentDetalleInmuebleBinding;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

public class DetalleInmuebleFragment extends Fragment {

    private DetalleInmuebleViewModel mViewModel;
    private FragmentDetalleInmuebleBinding binding;

    public static DetalleInmuebleFragment newInstance(Inmueble inmueble) {
        DetalleInmuebleFragment fragment = new DetalleInmuebleFragment();
        Bundle args = new Bundle();
        args.putSerializable("inmueble", inmueble);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        // Recuperar Inmueble de argumentos
        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                mViewModel.setInmueble(inmueble);
            }
        }

        // Observamos LiveData para mostrar datos
        mViewModel.getInmueble().observe(getViewLifecycleOwner(), inmueble -> {
            binding.tvDetalleDireccion.setText("Dirección: " + inmueble.getDireccion());
            binding.tvDetalleUso.setText("Uso: " + inmueble.getUso());
            binding.tvDetalleTipo.setText("Tipo: " + inmueble.getTipo());
            binding.tvDetalleAmbientes.setText("Ambientes: " + inmueble.getAmbientes());
            binding.tvDetalleSuperficie.setText("Superficie: " + inmueble.getSuperficie() + " m²");
            binding.tvDetalleValor.setText("Valor: $" + inmueble.getValor());
            binding.tvDetalleDisponible.setText(inmueble.isDisponible() ? "Disponible" : "No disponible");

            if (inmueble.getDuenio() != null) {
                binding.tvDetalleDuenio.setText("Dueño: " + inmueble.getDuenio().getNombre());
                binding.tvDetalleEmail.setText("Email: " + inmueble.getDuenio().getEmail());
                binding.tvDetalleTelefono.setText("Teléfono: " + inmueble.getDuenio().getTelefono());
            }

            // Cargar imagen
            Glide.with(this)
                    .load(ApiClient.BASE_URL + inmueble.getImagen())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(binding.ivDetalleImagen);

            // Setear switch sin disparar evento inicial
            binding.switchDisponible.setOnCheckedChangeListener(null);
            binding.switchDisponible.setChecked(inmueble.isDisponible());

            // Listener para actualizar solo al cambiar
            binding.switchDisponible.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mViewModel.actualizarDisponibilidad(isChecked);

                // Cambiar texto según disponibilidad
                binding.switchDisponible.setText(isChecked ? "Inhabilitar inmueble" : "Habilitar inmueble");
            });

            // Agrega también el texto inicial al cargar el switch
            binding.switchDisponible.setText(inmueble.isDisponible() ? "Inhabilitar inmueble" : "Habilitar inmueble");

        });

        return binding.getRoot();
    }
}

