package com.example.helpcasamobile;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminPanelActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        bottomNavigationView = findViewById(R.id.bottom_nav);

        // Set default fragment
        loadFragment(new HomeFragment());

        // Set up Bottom Navigation Item selection listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.nav_users) {
                    //selectedFragment = new UsersFragment();
                } else if (item.getItemId() == R.id.nav_agents) {
                    //selectedFragment = new AgentsFragment();
                } else if (item.getItemId() == R.id.nav_annonces) {
                 //   selectedFragment = new AnnouncementsFragment();
                }

                return loadFragment(selectedFragment);
            }
        });

    }

    // Method to replace the fragment
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
