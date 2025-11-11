package com.example.inmobiliaria.ui.inquilinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Contrato;
import com.example.inmobiliaria.modelo.Inquilino;

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



        //2- Recibe el clic desde el adapter y se lo pasa al ViewModel
        adapter = new InquilinosAdapter(getContext(), new ArrayList<>(), contrato -> {
            // Avisamos al ViewModel qué contrato fue seleccionado
            viewModel.seleccionarContrato(contrato);
        });

        recyclerView.setAdapter(adapter);

        // Observa el estado de carga
        viewModel.getLoading().observe(getViewLifecycleOwner(),
                isLoading -> progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE)
        );


        // Observa los contratos para actualizar el RecyclerView
        viewModel.getContratosLiveData().observe(getViewLifecycleOwner(),
                contratos -> adapter.setContratos(contratos)
        );



        // 4-  Cuando el ViewModel emite un inquilino, navega al detalle:
        viewModel.getInquilinoSeleccionado().observe(getViewLifecycleOwner(), inquilino -> {
            if (inquilino != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("inquilino", inquilino);

                DetalleInquilinoFragment detalleFragment = new DetalleInquilinoFragment();
                detalleFragment.setArguments(bundle);

                FragmentActivity activity = requireActivity();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, detalleFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Token y carga inicial
        String token = requireActivity()
                .getSharedPreferences("inmobiliaria", 0)
                .getString("token", "");
        viewModel.cargarContratos(token);

        return view;
    }
}
