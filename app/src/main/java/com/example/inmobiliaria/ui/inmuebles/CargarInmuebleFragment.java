//MVVM
package com.example.inmobiliaria.ui.inmuebles;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.databinding.FragmentNuevoInmuebleBinding;

public class CargarInmuebleFragment extends Fragment {

    private CargarInmuebleViewModel mViewModel;
    private FragmentNuevoInmuebleBinding binding;
    private ActivityResultLauncher<Intent> arl;
    private Intent intent;

    public static CargarInmuebleFragment newInstance() {
        return new CargarInmuebleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNuevoInmuebleBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(CargarInmuebleViewModel.class);

        configurarGaleria();

        // Observa los cambios de imagen seleccionada
        mViewModel.getMuri().observe(getViewLifecycleOwner(), uri -> {

                binding.ivDetalleImagen.setImageURI(uri);

        });

        // Botón para seleccionar imagen
        binding.btnSeleccionarImagen.setOnClickListener(v -> arl.launch(intent));

        // Botón para guardar inmueble
        binding.btnGuardarInmueble.setOnClickListener(v -> {
            mViewModel.cargarInmueble(
                    binding.etDireccion.getText().toString(),
                    binding.etValor.getText().toString(),
                    binding.etTipo.getText().toString(),
                    binding.etUso.getText().toString(),
                    binding.etAmbientes.getText().toString(),
                    binding.etSuperficie.getText().toString(),
                    binding.switchDisponible.isChecked()
            );
        });

        return binding.getRoot();
    }

    private void configurarGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> mViewModel.recibirFoto(result));
    }
}
