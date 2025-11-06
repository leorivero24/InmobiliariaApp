// MVVM - ContratosFragment
package com.example.inmobiliaria.ui.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;

import java.util.ArrayList;

public class ContratosFragment extends Fragment {

    private ContratosViewModel viewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ContratosAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contratos, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewContratos);
        progressBar = view.findViewById(R.id.progressBarContratos);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContratosAdapter(requireContext(), new ArrayList<>(), contrato ->
                viewModel.onContratoSeleccionado(contrato)  // Solo notificamos al ViewModel
        );
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ContratosViewModel.class);

        //  Observadores LiveData
        viewModel.getContratosLiveData().observe(getViewLifecycleOwner(), adapter::setContratos);
        viewModel.getLoading().observe(getViewLifecycleOwner(),
                isLoading -> progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());
        viewModel.getMostrarDetalle().observe(getViewLifecycleOwner(), contrato -> {
                DetalleContratoFragment detalleFragment = DetalleContratoFragment.newInstance(contrato);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, detalleFragment)
                        .addToBackStack(null)
                        .commit();

        });

        String token = requireActivity()
                .getSharedPreferences("inmobiliaria", 0)
                .getString("token", "");
        viewModel.cargarContratos(token);

        return view;
    }
}
