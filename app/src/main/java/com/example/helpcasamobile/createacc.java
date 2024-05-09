package com.example.helpcasamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
public class createacc extends AppCompatActivity {
    private RadioGroup rdg;
    private TextView button;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);
        rdg = findViewById(R.id.radg);
        button = findViewById(R.id.suivant);
        i = new Intent(createacc.this,);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(rdg.getCheckedRadioButtonId()==R.id.agentimmobilier){

               }
            }
        });

    }
}