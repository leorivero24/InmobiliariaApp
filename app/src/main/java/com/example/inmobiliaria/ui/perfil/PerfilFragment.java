package com.example.inmobiliaria.ui.perfil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Propietario;

public class PerfilFragment extends Fragment {

    private EditText etNombre, etApellido, etDni, etEmail, etTelefono, etClave, etClaveActual, etNuevaClave;
    private Button btnEditarGuardar;
    private PerfilViewModel viewModel;
    private boolean editMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Vincular EditTexts
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etDni = view.findViewById(R.id.etDNI);
        etEmail = view.findViewById(R.id.etEmail);
        etTelefono = view.findViewById(R.id.etTelefono);
        etClave = view.findViewById(R.id.etClave); // clave encriptada, solo lectura
        etClaveActual = view.findViewById(R.id.etClaveActual);
        etNuevaClave = view.findViewById(R.id.etNuevaClave);
        btnEditarGuardar = view.findViewById(R.id.btnEditarGuardar);

        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        // Inicialmente los campos de contraseña actual y nueva están deshabilitados
        etClaveActual.setEnabled(false);
        etNuevaClave.setEnabled(false);

        // Observar los datos del propietario
        viewModel.getPropietarioLiveData().observe(getViewLifecycleOwner(), propietario -> {
            if (propietario != null) {
                etNombre.setText(propietario.getNombre());
                etApellido.setText(propietario.getApellido());
                etDni.setText(propietario.getDni());
                etEmail.setText(propietario.getEmail());
                etTelefono.setText(propietario.getTelefono());
                etClave.setText(propietario.getClave()); // mostrar clave encriptada
                etClave.setEnabled(false); // solo lectura
            }
        });

        String token = requireActivity().getSharedPreferences("inmobiliaria", 0)
                .getString("token", "");
        Log.d("PerfilFragment", "🔑 Token obtenido: " + token);

        viewModel.cargarDatosPropietario(token);

        // Configurar botón editar/guardar
        btnEditarGuardar.setOnClickListener(v -> {
            if (!editMode) {
                editMode = true;
                setEditTextsEnabled(true); // habilita todos los campos editables
                btnEditarGuardar.setText("Guardar");
            } else {
                // Guardar cambios generales
                String nombre = etNombre.getText().toString().trim();
                String apellido = etApellido.getText().toString().trim();
                String dni = etDni.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String telefono = etTelefono.getText().toString().trim();

                viewModel.actualizarPropietario(token, nombre, apellido, dni, email, telefono, new PerfilViewModel.PerfilCallback() {
                    @Override
                    public void onSuccess() {
                        // Verificar si cambiar contraseña
                        String actualClave = etClaveActual.getText().toString().trim();
                        String nuevaClave = etNuevaClave.getText().toString().trim();

                        if (!actualClave.isEmpty() && !nuevaClave.isEmpty()) {
                            viewModel.cambiarPassword(token, actualClave, nuevaClave, new PerfilViewModel.PerfilCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getContext(), "Datos y contraseña actualizados", Toast.LENGTH_SHORT).show();
                                    limpiarCamposClave();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(getContext(), "Datos guardados, pero fallo al cambiar contraseña: " + error, Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_SHORT).show();
                        }

                        editMode = false;
                        setEditTextsEnabled(false);
                        btnEditarGuardar.setText("Editar");
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    private void setEditTextsEnabled(boolean enabled) {
        etNombre.setEnabled(enabled);
        etApellido.setEnabled(enabled);
        etDni.setEnabled(enabled);
        etEmail.setEnabled(enabled);
        etTelefono.setEnabled(enabled);
        etClaveActual.setEnabled(enabled);
        etNuevaClave.setEnabled(enabled);
        // etClave siempre deshabilitado (solo lectura)
    }

    private void limpiarCamposClave() {
        etClaveActual.setText("");
        etNuevaClave.setText("");
    }
}
