package com.example.helpcasamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class homeAgent extends AppCompatActivity {

    private ImageView userph;
    private TextView annrec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_agent);

        annrec = findViewById(R.id.nouvellesannonces);
        annrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(homeAgent.this, annrecu.class));

            }
        });



    }
}