package com.example.helpcasamobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class nouvelleAnnonce extends AppCompatActivity {

    private Spinner typbien, typann, Gouvernorat;
    private String bien, ann, gouv;

    private EditText adr, sup, prix, nbch, desc;
    private TextView ajoutph, envoyer;
    private RecyclerView imgRecyclerView;

    private static final int PICK_IMAGE_REQUEST = 111;
    private ArrayList<Uri> imageUris;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_annonce);

        adr = findViewById(R.id.adresse);
        sup = findViewById(R.id.superficie);
        prix = findViewById(R.id.prix);
        nbch = findViewById(R.id.nmbChamb);
        desc = findViewById(R.id.dscrip);

        ajoutph = findViewById(R.id.ajouter);
        envoyer = findViewById(R.id.envoyer);
        imgRecyclerView = findViewById(R.id.imgRecyclerView);

        imageUris = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageUris);

        imgRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imgRecyclerView.setAdapter(imageAdapter);

        setTypbien();
        setGouvernorat();
        setTypann();

        ajoutph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Pictures"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                } else if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                }
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setTypbien() {
        typbien = findViewById(R.id.type_bien);
        String[] options = {"Appartement", "Villa", "Bureau", "Terrain"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, options);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        typbien.setAdapter(adapter);

        typbien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bien = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setTypann() {
        typann = findViewById(R.id.type_annonce);
        String[] options = {"Vente", "Location"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, options);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        typann.setAdapter(adapter);
        typann.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ann = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setGouvernorat() {
        Gouvernorat = findViewById(R.id.gouver);
        String[] cities = {
                "Ariana", "Béja", "Ben Arous", "Bizerte", "Gabès", "Gafsa", "Jendouba",
                "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine",
                "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse",
                "Tataouine", "Tozeur", "Tunis", "Zaghouan"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, cities);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        Gouvernorat.setAdapter(adapter);

        Gouvernorat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gouv = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
