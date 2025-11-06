//MVVM
package com.example.inmobiliaria.ui.inquilinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Contrato;

import java.util.ArrayList;

public class InquilinosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private InquilinosAdapter adapter;
    private InquilinosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inquilinos, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewInquilinos);
        progressBar = view.findViewById(R.id.progressBarInquilinos);

        viewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapter = new InquilinosAdapter(getContext(), new ArrayList<>(), contrato -> {
            // 🔹 Ya no hay if aquí, solo avisamos al ViewModel
            viewModel.seleccionarContrato(contrato);
        });
        recyclerView.setAdapter(adapter);

        // Observa cambios
        viewModel.getLoading().observe(getViewLifecycleOwner(),
                isLoading -> progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE)
        );

        viewModel.getContratosLiveData().observe(getViewLifecycleOwner(),
                contratos -> adapter.setContratos(contratos)
        );

        // 🔹 Observa el mensaje que genera el ViewModel
        viewModel.getDialogoMensaje().observe(getViewLifecycleOwner(), mensaje -> {
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle("Datos del Inquilino")
                        .setMessage(mensaje)
                        .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                        .show();


        });

        // Token y carga inicial
        String token = requireActivity()
                .getSharedPreferences("inmobiliaria", 0)
                .getString("token", "");
        viewModel.cargarContratos(token);

        return view;
    }
}
