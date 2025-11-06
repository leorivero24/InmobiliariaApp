////MVVM
//package com.example.inmobiliaria.ui.perfil;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.inmobiliaria.R;
//import com.example.inmobiliaria.modelo.Propietario;
//
//public class PerfilFragment extends Fragment {
//
//    private EditText etNombre, etApellido, etDni, etEmail, etTelefono, etClave, etClaveActual, etNuevaClave;
//    private Button btnEditarGuardar;
//    private PerfilViewModel viewModel;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
//
//        // 🔹 Vincular vistas
//        etNombre = view.findViewById(R.id.etNombre);
//        etApellido = view.findViewById(R.id.etApellido);
//        etDni = view.findViewById(R.id.etDNI);
//        etEmail = view.findViewById(R.id.etEmail);
//        etTelefono = view.findViewById(R.id.etTelefono);
//        etClave = view.findViewById(R.id.etClave);
//        etClaveActual = view.findViewById(R.id.etClaveActual);
//        etNuevaClave = view.findViewById(R.id.etNuevaClave);
//        btnEditarGuardar = view.findViewById(R.id.btnEditarGuardar);
//
//        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
//
//        // 🔹 Observar los datos del propietario
//        viewModel.getPropietarioLiveData().observe(getViewLifecycleOwner(), propietario -> {
//                etNombre.setText(propietario.getNombre());
//                etApellido.setText(propietario.getApellido());
//                etDni.setText(propietario.getDni());
//                etEmail.setText(propietario.getEmail());
//                etTelefono.setText(propietario.getTelefono());
//                etClave.setText(propietario.getClave());
//
//        });
//
//        // 🔹 Observar cambios de estado (modo edición, mensajes, etc.)
//        viewModel.getEditMode().observe(getViewLifecycleOwner(), editMode -> {
//            boolean enabled = editMode;
//            setEditTextsEnabled(enabled);
//            btnEditarGuardar.setText(enabled ? "Guardar" : "Editar");
//        });
//
//        viewModel.getMensaje().observe(getViewLifecycleOwner(),
//                msg -> Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());
//
//        // 🔹 Cargar datos desde ViewModel
//        String token = requireActivity().getSharedPreferences("inmobiliaria", 0)
//                .getString("token", "");
//        viewModel.cargarDatosPropietario(token);
//
//        // 🔹 Acción del botón, delegada al ViewModel
//        btnEditarGuardar.setOnClickListener(v -> {
//            viewModel.onEditarGuardarClicked(
//                    token,
//                    etNombre.getText().toString().trim(),
//                    etApellido.getText().toString().trim(),
//                    etDni.getText().toString().trim(),
//                    etEmail.getText().toString().trim(),
//                    etTelefono.getText().toString().trim(),
//                    etClaveActual.getText().toString().trim(),
//                    etNuevaClave.getText().toString().trim()
//            );
//        });
//
//        return view;
//    }
//
//    private void setEditTextsEnabled(boolean enabled) {
//        etNombre.setEnabled(enabled);
//        etApellido.setEnabled(enabled);
//        etDni.setEnabled(enabled);
//        etEmail.setEnabled(enabled);
//        etTelefono.setEnabled(enabled);
//        etClaveActual.setEnabled(enabled);
//        etNuevaClave.setEnabled(enabled);
//        etClave.setEnabled(false);
//    }
//}

package com.example.inmobiliaria.ui.perfil;

import android.os.Bundle;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        // 🔹 Observar datos del propietario
        viewModel.getPropietarioLiveData().observe(getViewLifecycleOwner(), propietario -> {
            binding.etNombre.setText(propietario.getNombre());
            binding.etApellido.setText(propietario.getApellido());
            binding.etDNI.setText(propietario.getDni());
            binding.etEmail.setText(propietario.getEmail());
            binding.etTelefono.setText(propietario.getTelefono());
            binding.etClave.setText(propietario.getClave());
        });

        // 🔹 Observar modo edición
        viewModel.getEditMode().observe(getViewLifecycleOwner(), enabled -> {
            binding.etNombre.setEnabled(enabled);
            binding.etApellido.setEnabled(enabled);
            binding.etDNI.setEnabled(enabled);
            binding.etEmail.setEnabled(enabled);
            binding.etTelefono.setEnabled(enabled);
            binding.etClaveActual.setEnabled(enabled);
            binding.etNuevaClave.setEnabled(enabled);
            binding.etClave.setEnabled(false);
            binding.btnEditarGuardar.setText(enabled ? "Guardar" : "Editar");
        });

        // 🔹 Observar mensajes
        viewModel.getMensaje().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());

        // 🔹 Inicializar ViewModel con token
        String token = requireActivity().getSharedPreferences("inmobiliaria", 0)
                .getString("token", "");
        viewModel.inicializar(token);

        // 🔹 Acción del botón Editar/Guardar
        binding.btnEditarGuardar.setOnClickListener(v -> viewModel.onEditarGuardarClicked(
                binding.etNombre.getText().toString().trim(),
                binding.etApellido.getText().toString().trim(),
                binding.etDNI.getText().toString().trim(),
                binding.etEmail.getText().toString().trim(),
                binding.etTelefono.getText().toString().trim(),
                binding.etClaveActual.getText().toString().trim(),
                binding.etNuevaClave.getText().toString().trim()
        ));

        return binding.getRoot();
    }
}
