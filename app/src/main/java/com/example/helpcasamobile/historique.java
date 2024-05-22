package com.example.helpcasamobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class historique extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AnnonceAdapter annonceAdapter;
    private List<Annonce> annonceList;
    private Set<String> annonceIds;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private String userId;
    private TextView noAnnoncesTextView;
    private FirebaseStorage storage;
    private static final int MAX_IMAGES_PER_ANNOUNCEMENT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Get current user
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            userId = mUser.getUid();
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.ryclHisto);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize list and adapter
        annonceList = new ArrayList<>();
        annonceIds = new HashSet<>();
        annonceAdapter = new AnnonceAdapter(annonceList, this, true); // Pass true for historique
        recyclerView.setAdapter(annonceAdapter);

        // Initialize TextView for no annonces
        noAnnoncesTextView = findViewById(R.id.noAnnoncesTextView);

        // Fetch annonces
        fetchAnnonces();
    }

    private void fetchAnnonces() {
        if (userId != null) {
            db.collection("users").document(userId).collection("annonces")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                noAnnoncesTextView.setVisibility(View.VISIBLE);
                            } else {
                                noAnnoncesTextView.setVisibility(View.GONE);
                                for (DocumentSnapshot document : task.getResult()) {
                                    String id = document.getId();
                                    if (!annonceIds.contains(id)) {
                                        String adresse = document.getString("adresse");
                                        String superficie = document.getString("superficie");
                                        String price = document.getString("price");
                                        String numChambres = document.getString("numChambres");
                                        String description = document.getString("description");
                                        String bien = document.getString("bien");
                                        String ann = document.getString("ann");
                                        String gouv = document.getString("gouv");
                                        Long valid = document.getLong("valid");

                                        fetchImagesForAnnonce(userId, id, adresse, superficie, price, numChambres, description, bien, ann, gouv, valid); // Pass the etat parameter
                                    }
                                }
                            }
                        } else {
                            Log.e("Fetch Annonces", "Error getting documents: ", task.getException());
                        }
                    });
        }
    }

    private void fetchImagesForAnnonce(String userId, String annonceId, String adresse, String superficie, String price, String numChambres, String description, String bien, String ann, String gouv,Long valid) {
        List<Uri> imageUris = new ArrayList<>();
        StorageReference imagesRef = storage.getReference().child("users").child(userId).child(annonceId);

        AtomicInteger count = new AtomicInteger(0);

        imagesRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference imageRef : listResult.getItems()) {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUris.add(uri);
                    int currentCount = count.incrementAndGet();
                    if (currentCount == MAX_IMAGES_PER_ANNOUNCEMENT || currentCount == listResult.getItems().size()) {
                        Annonce annonce = new Annonce(annonceId, adresse, superficie, price, numChambres, description, bien, ann, gouv, imageUris,valid);
                        annonceList.add(annonce);
                        annonceIds.add(annonceId);
                        annonceAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Fetch Images", "Failed to fetch image for annonce: " + annonceId, e);
                    int currentCount = count.incrementAndGet();
                    if (currentCount == MAX_IMAGES_PER_ANNOUNCEMENT || currentCount == listResult.getItems().size()) {
                        Annonce annonce = new Annonce(annonceId, adresse, superficie, price, numChambres, description, bien, ann, gouv, imageUris,valid);
                        annonceList.add(annonce);
                        annonceIds.add(annonceId);
                        annonceAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).addOnFailureListener(e -> {
            Log.e("Fetch Images", "Failed to fetch images for annonce: " + annonceId, e);
        });
    }


}
