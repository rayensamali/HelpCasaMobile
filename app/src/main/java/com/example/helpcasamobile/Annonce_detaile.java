package com.example.helpcasamobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Annonce_detaile extends AppCompatActivity {

    private static final int MAX_IMAGES_PER_ANNOUNCEMENT = 5;

    private TextView valid;

    private String annonceId;
    private Annonce annonce;
    private RecyclerView imgRecyclerView;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce_detaile);
        valid = findViewById(R.id.reserv);

        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Annonce_detaile.this, payement.class);
                i.putExtra("prix",annonce.getPrice());
                i.putExtra("typ",annonce.getAnn());
                startActivity(i);
            }
        });
        imgRecyclerView = findViewById(R.id.imgsDuBien);
        imgRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Intent intent = getIntent();
        annonceId = intent.getStringExtra("annonceId");
        annonce = intent.getParcelableExtra("annonce");

        storage = FirebaseStorage.getInstance();

        if (annonce != null) {
            populateAnnonceDetails();
            fetchImagesForAnnonce();
        }
    }

    private void populateAnnonceDetails() {
        TextView tpB = findViewById(R.id.typbien);
        TextView tpAnn = findViewById(R.id.tyTrans);
        TextView gov = findViewById(R.id.gouvernorat);
        TextView prx = findViewById(R.id.prix);
        TextView desc = findViewById(R.id.boxDes);

        tpB.setText(annonce.getBien());
        tpAnn.setText(annonce.getAnn());
        gov.setText(annonce.getGouv());
        prx.setText(annonce.getPrice());
        desc.setText(annonce.getDescription());
    }

    private void fetchImagesForAnnonce() {
        List<Uri> imageUris = new ArrayList<>();
        StorageReference usersRef = storage.getReference().child("users");

        usersRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference userRef : listResult.getPrefixes()) {
                StorageReference annonceRef = userRef.child(annonceId);
                annonceRef.listAll().addOnSuccessListener(annonceListResult -> {
                    for (StorageReference imageRef : annonceListResult.getItems()) {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            imageUris.add(uri);
                            if (imageUris.size() == MAX_IMAGES_PER_ANNOUNCEMENT || imageUris.size() == annonceListResult.getItems().size()) {
                                setImageAdapter(imageUris);
                            }
                        }).addOnFailureListener(e -> {
                            Log.e("Fetch Image", "Failed to fetch image for annonce: " + annonceId, e);
                            if (imageUris.size() == MAX_IMAGES_PER_ANNOUNCEMENT || imageUris.size() == annonceListResult.getItems().size()) {
                                setImageAdapter(imageUris);
                            }
                        });
                    }
                }).addOnFailureListener(e -> Log.e("Fetch Annonce", "Failed to fetch annonce folder for annonce: " + annonceId, e));
            }
        }).addOnFailureListener(e -> Log.e("Fetch Users", "Failed to fetch users folders", e));
    }

    private void setImageAdapter(List<Uri> imageUris) {
        ImagerecuAdapter imageAdapter = new ImagerecuAdapter(imageUris, this);
        imgRecyclerView.setAdapter(imageAdapter);
    }
}
