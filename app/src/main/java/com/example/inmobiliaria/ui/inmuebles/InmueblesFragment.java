package com.example.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentInmueblesBinding;
import com.example.inmobiliaria.modelo.Inmueble;

import java.util.List;

public class InmueblesFragment extends Fragment {

    private FragmentInmueblesBinding binding;
    private InmueblesViewModel vm;

    //Esta clase muestra los inmuebles en un RecyclerView

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(InmueblesViewModel.class);
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);

        // Acción del botón "Agregar nuevo inmueble"
        binding.btnAgregarInmueble.setOnClickListener(v -> {
            CargarInmuebleFragment nuevoFragment = new CargarInmuebleFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, nuevoFragment) // 👈 Asegúrate de usar el ID del contenedor real
                    .addToBackStack(null)
                    .commit();
        });

        //Esta clase muestra los inmuebles en un RecyclerView.
        vm.getmInmueble().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                InmueblesAdapter adapter = new InmueblesAdapter(inmuebles, getContext());
                GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
                RecyclerView rv = binding.recyclerViewInmuebles;
                rv.setAdapter(adapter);
                rv.setLayoutManager(glm);
            }
        });

        return binding.getRoot();

    }





    @Override
    public void onResume() {
        super.onResume();
        vm.leerInmuebles();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}

