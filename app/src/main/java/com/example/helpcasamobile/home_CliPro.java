package com.example.helpcasamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class home_CliPro extends AppCompatActivity  implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cli_pro);


        findViewById(R.id.poster).setOnClickListener(this);
        findViewById(R.id.consulter).setOnClickListener(this);
        findViewById(R.id.hist).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.poster){
            startActivity(new Intent(home_CliPro.this, nouvelleAnnonce.class));
            finish();
        }
        if(view.getId() == R.id.consulter){
            //startActivity(new Intent(home_CliPro.this, nouvelleAnnonce.class));
            //finish();
        }

        if(view.getId() == R.id.hist){
            //startActivity(new Intent(home_CliPro.this, nouvelleAnnonce.class));
            //finish();
        }
    }
}