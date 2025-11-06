//MVVM

package com.example.inmobiliaria.ui.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Contrato;
import com.example.inmobiliaria.ui.pagos.PagosDialogFragment;

public class DetalleContratoFragment extends Fragment {

    private static final String ARG_CONTRATO = "contrato";
    private DetalleContratoViewModel viewModel;

    public static DetalleContratoFragment newInstance(Contrato contrato) {
        DetalleContratoFragment fragment = new DetalleContratoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTRATO, contrato);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_contrato, container, false);

        EditText etFechaInicio = view.findViewById(R.id.etFechaInicio);
        EditText etFechaFin = view.findViewById(R.id.etFechaFin);
        EditText etMonto = view.findViewById(R.id.etMonto);
        EditText etInquilino = view.findViewById(R.id.etInquilino);
        EditText etDireccion = view.findViewById(R.id.etDireccion);
        Button btnVerPagos = view.findViewById(R.id.btnVerPagos);

        viewModel = new ViewModelProvider(this).get(DetalleContratoViewModel.class);

        viewModel.setContrato((Contrato) getArguments().getSerializable(ARG_CONTRATO));


        // Observamos los cambios de los datos del contrato
        viewModel.getFechaInicio().observe(getViewLifecycleOwner(), etFechaInicio::setText);
        viewModel.getFechaFin().observe(getViewLifecycleOwner(), etFechaFin::setText);
        viewModel.getMonto().observe(getViewLifecycleOwner(), etMonto::setText);
        viewModel.getInquilinoNombre().observe(getViewLifecycleOwner(), etInquilino::setText);
        viewModel.getDireccion().observe(getViewLifecycleOwner(), etDireccion::setText);

        // Observamos evento para abrir pagos
        viewModel.getAbrirPagos().observe(getViewLifecycleOwner(), idContrato -> {
                PagosDialogFragment dialog = PagosDialogFragment.newInstance(idContrato);
                dialog.show(getParentFragmentManager(), "PagosDialog");

        });

        btnVerPagos.setOnClickListener(v -> viewModel.onVerPagosClicked());

        return view;
    }
}
