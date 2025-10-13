package com.example.inmobiliaria.ui.menu;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.inmobiliaria.R;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            handleMenuItem(item);
            return true;
        });
    }

    private void handleMenuItem(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_inmuebles) {
            Toast.makeText(this, "Mis Inmuebles", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_contratos) {
            Toast.makeText(this, "Contratos y Pagos", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Cerrar sesión", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawers();
    }
}
