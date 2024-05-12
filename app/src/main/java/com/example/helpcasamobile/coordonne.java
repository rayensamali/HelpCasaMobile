package com.example.helpcasamobile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;


public class coordonne extends AppCompatActivity {
    private Spinner spinner, Countries,States;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordonne);
        // affichage de liste de civilité
        spinner = findViewById(R.id.civilité);
        String[] options = {"Mr", "Melle", "Mm"};
        String[] cities = {
                "Ariana",
                "Béja",
                "Ben Arous",
                "Bizerte",
                "Gabès",
                "Gafsa",
                "Jendouba",
                "Kairouan",
                "Kasserine",
                "Kebili",
                "Kef",
                "Mahdia",
                "Manouba",
                "Medenine",
                "Monastir",
                "Nabeul",
                "Sfax",
                "Sidi Bouzid",
                "Siliana",
                "Sousse",
                "Tataouine",
                "Tozeur",
                "Tunis",
                "Zaghouan"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, options);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spinner.setAdapter(adapter);
        Countries = findViewById(R.id.spinner_gouvernorates);
        new CountryNameFetcher() {
            @Override
            protected void onPostExecute(ArrayList<String> countryNames) {
                if (countryNames != null) {
                    // Log the number of countries
                    Collections.sort(countryNames);
                    // Set the adapter for the Countries spinner
                    ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(coordonne.this, R.layout.spinner_item_layout, countryNames);
                    countryAdapter.setDropDownViewResource(R.layout.spinner_item_layout);
                    Countries.setAdapter(countryAdapter);
                    // Do something with the country names ArrayList
                } else {
                    // Handle error
                    Toast.makeText(coordonne.this, "Probleme de connectiob", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
        Countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCountry = adapterView.getItemAtPosition(i).toString();
                if (!selectedCountry.equals("Tunisia")) {
                    showOnlyTunisiaAlert();
                    spinner.setSelection(0); // Reset spinner to first item (Tunisia)
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        States = findViewById(R.id.spinner_states);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item_layout, cities);
        adapter1.setDropDownViewResource(R.layout.spinner_item_layout);
        States.setAdapter(adapter1);


    }

    private void showOnlyTunisiaAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seule la Tunisie est disponible");
        builder.setMessage("Désolé, seule la Tunisie est disponible pour le moment.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Countries.setSelection(getIndex(Countries, "Tunisia"));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int getIndex(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                return i;
            }

        }
        return 0;
    }
}

