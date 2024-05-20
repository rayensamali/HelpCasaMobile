package com.example.helpcasamobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Annonce_detaile extends AppCompatActivity {

    private Intent intent;
    private String annonceId;
    private Annonce annonce;
    private String adresse;
    private String superficie;
    private String price;
    private String numChambres;
    private String description;
    private String bien;
    private String ann;
    private String gouv;
    private List<Uri> imageUrls;
    private TextView tpB, tpAnn, gov, prx, desc;
    private RecyclerView imgRecyclerView;

    private static final int MAX_IMAGES_PER_ANNOUNCEMENT = 5; // Example value, adjust as needed

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce_detaile);
        imgRecyclerView = findViewById(R.id.imgsDuBien);

        intent = getIntent();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        annonceId = intent.getStringExtra("annonceId");
        annonce = intent.getParcelableExtra("annonce");

        adresse = annonce.getAdresse();
        superficie = annonce.getSuperficie();
        price = annonce.getPrice();
        numChambres = annonce.getNumChambres();
        description = annonce.getDescription();
        bien = annonce.getBien();
        ann = annonce.getAnn();
        gouv = annonce.getGouv();
        imageUrls = annonce.getImageUrls();

        tpB = findViewById(R.id.typbien);
        tpB.setText(bien);
        tpAnn = findViewById(R.id.tyTrans);
        tpAnn.setText(ann);
        gov = findViewById(R.id.gouvernorat);
        gov.setText(gouv);
        prx = findViewById(R.id.prix);
        prx.setText(price);
        desc = findViewById(R.id.boxDes);
        desc.setText(description);

        fetchImagesForAnnonce();
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
                            if (imageUris.size() == MAX_IMAGES_PER_ANNOUNCEMENT) {
                                setImageAdapter(imageUris);
                            }
                        }).addOnFailureListener(e -> {
                            Log.e("Fetch Image", "Failed to fetch image for annonce: " + annonceId, e);
                            if (imageUris.size() == MAX_IMAGES_PER_ANNOUNCEMENT) {
                                setImageAdapter(imageUris);
                            }
                        });
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Fetch Annonce", "Failed to fetch annonce folder for annonce: " + annonceId, e);
                });
            }
        }).addOnFailureListener(e -> {
            Log.e("Fetch Users", "Failed to fetch users folders", e);
        });
    }


    private void setImageAdapter(List<Uri> imageUris) {
        ImagerecuAdapter imageAdapter = new ImagerecuAdapter(imageUris, this);
        imgRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imgRecyclerView.setAdapter(imageAdapter);
    }
}
