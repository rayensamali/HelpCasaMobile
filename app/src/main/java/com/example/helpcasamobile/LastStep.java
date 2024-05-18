package com.example.helpcasamobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LastStep extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 111;

    private TextView ignorer, ajouter;
    private ImageView image;

    private SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_step);

        ajouter = findViewById(R.id.ajouter);
        ignorer = findViewById(R.id.ignore);
        image = findViewById(R.id.userPhotoImageView);
        sh = getSharedPreferences("userType", Context.MODE_PRIVATE);

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        ignorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
            }
        });
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            Uri selectedImageUri = data != null ? data.getData() : null;
            if (selectedImageUri != null) {
                image.setImageURI(selectedImageUri);
                showProgressAndNavigate();
            }
        }
    }

    private void showProgressAndNavigate() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Finalisation");
        progressDialog.setTitle("Creation de votre compte");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                navigateToHome();
            }
        }, 5000);
    }

    private void navigateToHome() {
        String userType = sh.getString("type", "");
        if ("agent".equals(userType)) {
            startActivity(new Intent(LastStep.this, homeAgent.class));
        } else {
            Toast.makeText(LastStep.this, "otheruser", Toast.LENGTH_LONG).show();
        }
    }
}