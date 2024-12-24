package com.example.helpcasamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView identifier, creecompte, apropos;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        identifier = findViewById(R.id.identif);
        creecompte = findViewById(R.id.createacc);
        apropos = findViewById(R.id.Aprop);

        // Redirection si l'utilisateur est connecté
        checkIfLoggedIn();

        identifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, sidentifier.class));
            }
        });

        creecompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, createacc.class));
            }
        });

        apropos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.seloger.com/"));
                startActivity(intent);
            }
        });
    }

    private void checkIfLoggedIn() {
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Si l'utilisateur est connecté, récupérez son type pour le rediriger vers la bonne activité
            String userType = getSharedPreferences("userType", Context.MODE_PRIVATE).getString("type", "");
            if ("agent".equals(userType)) {
                startActivity(new Intent(MainActivity.this, homeAgent.class));
            } else if ("PROPRIETAIRE".equals(userType)) {
                startActivity(new Intent(MainActivity.this, home_CliPro.class));
            }
            finish(); // Terminer MainActivity pour éviter de revenir en arrière
        }
    }
}


