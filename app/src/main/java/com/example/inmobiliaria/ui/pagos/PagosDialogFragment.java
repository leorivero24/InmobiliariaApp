
//MVVM
package com.example.inmobiliaria.ui.pagos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;

public class PagosDialogFragment extends DialogFragment {

    private static final String ARG_ID_CONTRATO = "id_contrato";
    private int idContrato;
    private PagosViewModel viewModel;

    public static PagosDialogFragment newInstance(int idContrato) {
        PagosDialogFragment fragment = new PagosDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_CONTRATO, idContrato);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idContrato = getArguments().getInt(ARG_ID_CONTRATO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pagos, container, false);

        RecyclerView rvPagos = view.findViewById(R.id.rvPagos);
        Button btnCerrar = view.findViewById(R.id.btnCerrarPagos);
        rvPagos.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(PagosViewModel.class);

        // 🔹 Observa los pagos cargados
        viewModel.getPagosLiveData().observe(getViewLifecycleOwner(), pagos -> {
            if (pagos != null) {
                rvPagos.setAdapter(new PagosAdapter(pagos));
            }
        });

        // 🔹 Observa mensajes de error u otros
        viewModel.getMensaje().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());

        // 🔹 Obtener token y solicitar los pagos
        String token = requireActivity().getSharedPreferences("inmobiliaria", 0)
                .getString("token", "");
        viewModel.cargarPagos(token, idContrato);

        btnCerrar.setOnClickListener(v -> dismiss());

        return view;
    }
}
