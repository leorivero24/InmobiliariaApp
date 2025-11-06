//MVVM

package com.example.inmobiliaria.ui.menu;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.inmobiliaria.ui.login.LoginActivity;
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

        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // 🔹 Vincular header (nombre y correo)
        TextView tvUserName = navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
        TextView tvUserEmail = navigationView.getHeaderView(0).findViewById(R.id.tvUserEmail);

        // 🔹 Observadores
        viewModel.getNombre().observe(this, tvUserName::setText);
        viewModel.getEmail().observe(this, tvUserEmail::setText);
        viewModel.getMensajeToast().observe(this, msg -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
        viewModel.getTitulo().observe(this, title -> getSupportActionBar().setTitle(title));

        viewModel.getFragmentSeleccionado().observe(this, fragment -> {
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
                drawerLayout.closeDrawers();
            }
        });

        viewModel.getMostrarDialogoLogout().observe(this, mostrar -> {
            if (mostrar != null && mostrar) {
                mostrarDialogoLogout();
            }
        });

        viewModel.getIrALogin().observe(this, ir -> {
            if (ir != null && ir) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });

        // Carga inicial
        viewModel.cargarDatosPropietario();

        navigationView.setNavigationItemSelectedListener(item -> {
            viewModel.seleccionarMenu(item.getItemId());
            return true;
        });

        // Mostrar Inicio por defecto
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_inicio);
            viewModel.seleccionarMenu(R.id.nav_inicio);
        }
    }

    private void mostrarDialogoLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> viewModel.cerrarSesion())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
