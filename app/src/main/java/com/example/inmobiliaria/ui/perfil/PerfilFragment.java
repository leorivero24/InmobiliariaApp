//package com.example.inmobiliaria.ui.perfil;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.inmobiliaria.databinding.FragmentPerfilBinding;
//
//public class PerfilFragment extends Fragment {
//
//    private FragmentPerfilBinding binding;
//    private PerfilViewModel viewModel;
//    private boolean editMode = false;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentPerfilBinding.inflate(inflater, container, false);
//        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
//
//        // Inicialmente los campos de contraseña actual y nueva están deshabilitados
//        binding.etClaveActual.setEnabled(false);
//        binding.etNuevaClave.setEnabled(false);
//        binding.etClave.setEnabled(false); // clave encriptada, solo lectura
//
//        // Observar los datos del propietario
//        viewModel.getPropietarioLiveData().observe(getViewLifecycleOwner(), propietario -> {
//            if (propietario != null) {
//                binding.etNombre.setText(propietario.getNombre());
//                binding.etApellido.setText(propietario.getApellido());
//                binding.etDNI.setText(propietario.getDni());
//                binding.etEmail.setText(propietario.getEmail());
//                binding.etTelefono.setText(propietario.getTelefono());
//                binding.etClave.setText(propietario.getClave());
//            }
//        });
//
//        // Obtener token
//        String token = requireActivity().getSharedPreferences("inmobiliaria", 0)
//                .getString("token", "");
//        Log.d("PerfilFragment", "🔑 Token obtenido: " + token);
//
//        // Cargar datos del propietario
//        viewModel.cargarDatosPropietario(token);
//
//        // Configurar botón Editar/Guardar
//        binding.btnEditarGuardar.setOnClickListener(v -> {
//            if (!editMode) {
//                editMode = true;
//                setEditTextsEnabled(true);
//                binding.btnEditarGuardar.setText("Guardar");
//            } else {
//                // Guardar cambios generales
//                String nombre = binding.etNombre.getText().toString().trim();
//                String apellido = binding.etApellido.getText().toString().trim();
//                String dni = binding.etDNI.getText().toString().trim();
//                String email = binding.etEmail.getText().toString().trim();
//                String telefono = binding.etTelefono.getText().toString().trim();
//
//                viewModel.actualizarPropietario(token, nombre, apellido, dni, email, telefono, new PerfilViewModel.PerfilCallback() {
//                    @Override
//                    public void onSuccess() {
//                        // Verificar si cambiar contraseña
//                        String actualClave = binding.etClaveActual.getText().toString().trim();
//                        String nuevaClave = binding.etNuevaClave.getText().toString().trim();
//                        if (!actualClave.isEmpty() && !nuevaClave.isEmpty()) {
//                            viewModel.cambiarPassword(token, actualClave, nuevaClave, new PerfilViewModel.PerfilCallback() {
//                                @Override
//                                public void onSuccess() {
//                                    Toast.makeText(getContext(), "Datos y contraseña actualizados", Toast.LENGTH_SHORT).show();
//                                    limpiarCamposClave();
//                                }
//
//                                @Override
//                                public void onError(String error) {
//                                    Toast.makeText(getContext(), "Datos guardados, pero fallo al cambiar contraseña: " + error, Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        } else {
//                            Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_SHORT).show();
//                        }
//
//                        editMode = false;
//                        setEditTextsEnabled(false);
//                        binding.btnEditarGuardar.setText("Editar");
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//
//        return binding.getRoot();
//    }
//
//    private void setEditTextsEnabled(boolean enabled) {
//        binding.etNombre.setEnabled(enabled);
//        binding.etApellido.setEnabled(enabled);
//        binding.etDNI.setEnabled(enabled);
//        binding.etEmail.setEnabled(enabled);
//        binding.etTelefono.setEnabled(enabled);
//        binding.etClaveActual.setEnabled(enabled);
//        binding.etNuevaClave.setEnabled(enabled);
//        // binding.etClave siempre deshabilitado (solo lectura)
//    }
//
//    private void limpiarCamposClave() {
//        binding.etClaveActual.setText("");
//        binding.etNuevaClave.setText("");
//    }
//}


package com.example.inmobiliaria.ui.perfil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        // Inicialmente los campos de contraseña y clave encriptada deshabilitados
        binding.etClave.setEnabled(false);
        binding.etClaveActual.setEnabled(false);
        binding.etNuevaClave.setEnabled(false);

        // Obtener token
        String token = requireActivity().getSharedPreferences("inmobiliaria", 0)
                .getString("token", "");
        Log.d("PerfilFragment", "🔑 Token obtenido: " + token);

        // Cargar datos del propietario
        viewModel.cargarDatosPropietario(token);

        // Observadores
        viewModel.getPropietarioLiveData().observe(getViewLifecycleOwner(), propietario -> {
            if (propietario != null) {
                binding.etNombre.setText(propietario.getNombre());
                binding.etApellido.setText(propietario.getApellido());
                binding.etDNI.setText(propietario.getDni());
                binding.etEmail.setText(propietario.getEmail());
                binding.etTelefono.setText(propietario.getTelefono());
                binding.etClave.setText(propietario.getClave());
            }
        });

        viewModel.getEditMode().observe(getViewLifecycleOwner(), enabled -> {
            setEditTextsEnabled(enabled);
            binding.btnEditarGuardar.setText(enabled ? "Guardar" : "Editar");
        });

        viewModel.getMensaje().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        // Botón Editar/Guardar
        binding.btnEditarGuardar.setOnClickListener(v -> {
            String nombre = binding.etNombre.getText().toString().trim();
            String apellido = binding.etApellido.getText().toString().trim();
            String dni = binding.etDNI.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String telefono = binding.etTelefono.getText().toString().trim();
            String claveActual = binding.etClaveActual.getText().toString().trim();
            String nuevaClave = binding.etNuevaClave.getText().toString().trim();

            viewModel.onEditarGuardarClicked(
                    token,         // 🔹 IMPORTANTE: token como primer parámetro
                    nombre,
                    apellido,
                    dni,
                    email,
                    telefono,
                    claveActual,
                    nuevaClave
            );
        });


        return binding.getRoot();
    }

    private void setEditTextsEnabled(boolean enabled) {
        binding.etNombre.setEnabled(enabled);
        binding.etApellido.setEnabled(enabled);
        binding.etDNI.setEnabled(enabled);
        binding.etEmail.setEnabled(enabled);
        binding.etTelefono.setEnabled(enabled);
        binding.etClaveActual.setEnabled(enabled);
        binding.etNuevaClave.setEnabled(enabled);
        // binding.etClave siempre deshabilitado (solo lectura)
    }


}
