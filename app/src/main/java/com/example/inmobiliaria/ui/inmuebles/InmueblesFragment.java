


package com.example.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.request.ApiService;
import com.example.inmobiliaria.ui.detalle_inmueble.DetalleInmuebleFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesFragment extends Fragment {

    private RecyclerView recyclerView;
    private InmueblesAdapter adapter;
    private ProgressBar progressBar;
    private Button btnAgregarInmueble;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inmuebles, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewInmuebles);
        progressBar = view.findViewById(R.id.progressBar);
        btnAgregarInmueble = view.findViewById(R.id.btnAgregarInmueble);

        // 🔹 Acción del botón "Agregar nuevo inmueble"
        btnAgregarInmueble.setOnClickListener(v -> {
            CargarInmuebleFragment nuevoFragment = new CargarInmuebleFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, nuevoFragment) // asegúrate que content_frame sea tu contenedor principal
                    .addToBackStack(null)
                    .commit();
        });

        // 🔹 Configurar el RecyclerView con 2 columnas
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

        // 🔹 Obtener token guardado
        String token = requireActivity().getSharedPreferences("inmobiliaria", 0)
                .getString("token", "");
        Log.d("InmueblesFragment", "🔑 Token: " + token);

        cargarInmuebles(token);

        return view;
    }

    private void cargarInmuebles(String token) {
        progressBar.setVisibility(View.VISIBLE);

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<List<Inmueble>> call = api.obtenerInmuebles("Bearer " + token);

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setInmuebles(response.body());
                } else {
                    Toast.makeText(getContext(), "Error al cargar inmuebles: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
