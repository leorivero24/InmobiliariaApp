package com.example.inmobiliaria.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.ui.inicio.InicioFragment;
import com.example.inmobiliaria.ui.inmuebles.InmueblesFragment;
import com.example.inmobiliaria.ui.login.LoginActivity;
import com.example.inmobiliaria.ui.perfil.PerfilFragment;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private MenuViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Vincular header (nombre y correo)
        TextView tvUserName = navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
        TextView tvUserEmail = navigationView.getHeaderView(0).findViewById(R.id.tvUserEmail);

        viewModel.getNombre().observe(this, tvUserName::setText);
        viewModel.getEmail().observe(this, tvUserEmail::setText);

        viewModel.cargarDatosPropietario();

        // Manejar clicks del menú
        navigationView.setNavigationItemSelectedListener(this::onMenuItemSelected);

        // ✅ Mostrar "Inicio" automáticamente al abrir la app
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_inicio); // marca el item activo
            loadFragment(new InicioFragment());
        }
    }

    private boolean onMenuItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment selectedFragment = null;
        String title = getString(R.string.app_name); // valor por defecto

        if (id == R.id.nav_inicio) {
            selectedFragment = new InicioFragment();
            title = "Ubicación";
            Toast.makeText(this, "Ubicacion", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_perfil) {
            selectedFragment = new PerfilFragment();
            title = "Perfil";
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_inmuebles) {
            selectedFragment = new InmueblesFragment();
            title = "Inmuebles";
            Toast.makeText(this, "Inmuebles", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_inquilinos) {
            Toast.makeText(this, "Inquilinos", Toast.LENGTH_SHORT).show();
            title = "Inquilinos";
        } else if (id == R.id.nav_contratos) {
            Toast.makeText(this, "Contratos", Toast.LENGTH_SHORT).show();
            title = "Contratos";
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Estás seguro que deseas cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        // Acción de cerrar sesión
                        viewModel.cerrarSesion();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Solo cierra el diálogo
                        dialog.dismiss();
                    })
                    .setCancelable(false) // evita cerrar con toque fuera del diálogo
                    .show();

            return true;
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            getSupportActionBar().setTitle(title); //
        }

        drawerLayout.closeDrawers();
        return true;
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
