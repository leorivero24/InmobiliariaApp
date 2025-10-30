package com.example.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.ui.detalle_inmueble.DetalleInmuebleFragment;

import java.util.ArrayList;

public class InmueblesFragment extends Fragment {

    private RecyclerView recyclerView;
    private InmueblesAdapter adapter;
    private ProgressBar progressBar;
    private Button btnAgregarInmueble;
    private InmueblesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inmuebles, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewInmuebles);
        progressBar = view.findViewById(R.id.progressBar);
        btnAgregarInmueble = view.findViewById(R.id.btnAgregarInmueble);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new InmueblesAdapter(requireContext(), new ArrayList<>(), inmueble -> {
            DetalleInmuebleFragment detalleFragment = DetalleInmuebleFragment.newInstance(inmueble);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, detalleFragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);

        // 🔹 Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);

        // 🔹 Observadores LiveData
        viewModel.getInmueblesLiveData().observe(getViewLifecycleOwner(), inmuebles -> adapter.setInmuebles(inmuebles));
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));
        viewModel.getError().observe(getViewLifecycleOwner(), mensaje -> Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show());

        // 🔹 Acción del botón "Agregar nuevo inmueble"
        btnAgregarInmueble.setOnClickListener(v -> {
            CargarInmuebleFragment nuevoFragment = new CargarInmuebleFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, nuevoFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // 🔹 Obtener token y cargar inmuebles
        String token = requireActivity().getSharedPreferences("inmobiliaria", 0).getString("token", "");
        viewModel.cargarInmuebles(token);

        return view;
    }
}
