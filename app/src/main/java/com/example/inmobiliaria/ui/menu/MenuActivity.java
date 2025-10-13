package com.example.inmobiliaria.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Vincular header
        TextView tvUserName = navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
        TextView tvUserEmail = navigationView.getHeaderView(0).findViewById(R.id.tvUserEmail);

        // Observar los datos del ViewModel
        viewModel.getNombre().observe(this, tvUserName::setText);
        viewModel.getEmail().observe(this, tvUserEmail::setText);

        viewModel.cargarDatosPropietario();

        // Manejar clicks del menú
        navigationView.setNavigationItemSelectedListener(this::onMenuItemSelected);
    }

    private boolean onMenuItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            Toast.makeText(this, "Inicio", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_perfil) {
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_inmuebles) {
            Toast.makeText(this, "Inmuebles", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_inquilinos) {
            Toast.makeText(this, "Inquilinos", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_contratos) {
            Toast.makeText(this, "Contratos", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            viewModel.cerrarSesion();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
