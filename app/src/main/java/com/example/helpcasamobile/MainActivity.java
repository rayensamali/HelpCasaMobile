package com.example.helpcasamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView identifier,creecompte,apropos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        creecompte = findViewById(R.id.createacc);
        identifier = findViewById(R.id.identif);
        apropos = findViewById(R.id.Aprop);
        identifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,sidentifier.class));
            }
        });
        creecompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,createacc.class));
            }
        });
        apropos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.seloger.com/"));
                startActivity(intent);
            }
        });
    }
}