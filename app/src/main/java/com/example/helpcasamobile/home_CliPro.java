package com.example.helpcasamobile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class home_CliPro extends AppCompatActivity implements View.OnClickListener {

    ImageButton settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cli_pro);

        // Initialize ImageButtons
        findViewById(R.id.poster).setOnClickListener(this);  // Button to post announcement
        findViewById(R.id.consulter).setOnClickListener(this);  // Button to consult announcements
        findViewById(R.id.hist).setOnClickListener(this);  // Button to view history

        // Initialize settings button and set click listener
        settings = findViewById(R.id.param);  // Assuming you have a button with this ID
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();  // Show logout dialog when settings is clicked
            }
        });
    }

    @Override
    public void onClick(View view) {
        // Handle clicks on different buttons
        if (view.getId() == R.id.poster) {
            // Navigate to Nouvelle Annonce (New Announcement) screen
            startActivity(new Intent(home_CliPro.this, nouvelleAnnonce.class));
        }

        if (view.getId() == R.id.consulter) {
            // Navigate to Consultation screen (for announcements)
            startActivity(new Intent(home_CliPro.this, annonce_cli_prp.class));
        }

        if (view.getId() == R.id.hist) {
            // Navigate to History screen
            startActivity(new Intent(home_CliPro.this, historique.class));
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Handle back press: finish all activities and exit the app
        finishAffinity();  // This will close all activities and exit the app
    }

    // Method to show the logout confirmation dialog
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setCancelable(false)  // Disables closing the dialog by tapping outside
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Log the user out from Firebase
                        FirebaseAuth.getInstance().signOut();

                        // Redirect to login screen or main activity after logout
                        startActivity(new Intent(home_CliPro.this, MainActivity.class));
                        finish();  // Close the current activity
                    }
                })
                .setNegativeButton("No", null);  // Close the dialog if "No" is clicked

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
