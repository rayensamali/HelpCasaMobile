package com.example.helpcasamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class welcome extends AppCompatActivity {
    private TextView userName,suiv;
    private Intent i;
    private String usname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        suiv = findViewById(R.id.suiv);
        userName = findViewById(R.id.nomuti);

        i = getIntent();
        if(i.hasExtra("username")){
            usname = i.getStringExtra("username");
            userName.setText("Votre nom d'utilisatuer est \n"+usname);
        }
        suiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(welcome.this, LastStep.class));
                finish();
            }
        });



    }
}