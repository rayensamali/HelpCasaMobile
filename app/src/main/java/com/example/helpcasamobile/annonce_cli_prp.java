package com.example.helpcasamobile;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class annonce_cli_prp extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnnonceAdapter annonceAdapter;
    private List<Annonce> annonceList;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ImageButton settings;

    private static final int MAX_IMAGES_PER_ANNOUNCEMENT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce_cli_prp);

        recyclerView = findViewById(R.id.rcy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        annonceList = new ArrayList<>();
        annonceAdapter = new AnnonceAdapter(annonceList, this,false);
        recyclerView.setAdapter(annonceAdapter);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        fetchAllUsersWithAnnonces();
    }

    private void fetchAllUsersWithAnnonces() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot userDocument : task.getResult()) {
                            String userId = userDocument.getId();
                            checkUserHasAnnonces(userId);
                        }
                    } else {
                        Log.e("Fetch Users", "Error getting users: ", task.getException());
                    }
                });
    }

    private void checkUserHasAnnonces(String userId) {
        db.collection("users").document(userId).collection("annonces")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        fetchAnnoncesForUser(userId);
                    }
                });
    }

    private void fetchAnnoncesForUser(String userId) {
        db.collection("users").document(userId).collection("annonces")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Long isValid = document.getLong("valid");
                            if (isValid != null && isValid == 1) {
                                String id = document.getId();
                                String adresse = document.getString("adresse");
                                String superficie = document.getString("superficie");
                                String price = document.getString("price");
                                String numChambres = document.getString("numChambres");
                                String description = document.getString("description");
                                String bien = document.getString("bien");
                                String ann = document.getString("ann");
                                String gouv = document.getString("gouv");

                                fetchImagesForAnnonce(userId, id, adresse, superficie, price, numChambres, description, bien, ann, gouv,isValid);
                            }
                        }
                    } else {
                        Log.e("Fetch Annonce", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void fetchImagesForAnnonce(String userId, String annonceId, String adresse, String superficie, String price, String numChambres, String description, String bien, String ann, String gouv, Long isValid) {
        List<Uri> imageUris = new ArrayList<>();
        StorageReference imagesRef = storage.getReference().child("users").child(userId).child(annonceId);

        imagesRef.listAll().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<StorageReference> imageRefs = task.getResult().getItems();
                int totalImages = Math.min(imageRefs.size(), MAX_IMAGES_PER_ANNOUNCEMENT);

                if (totalImages == 0) {
                    Annonce annonce = new Annonce(annonceId, adresse, superficie, price, numChambres, description, bien, ann, gouv, imageUris,isValid);
                    annonceList.add(annonce);
                    annonceAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < totalImages; i++) {
                        StorageReference imageRef = imageRefs.get(i);
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            imageUris.add(uri);
                            if (imageUris.size() == totalImages) {
                                Annonce annonce = new Annonce(annonceId, adresse, superficie, price, numChambres, description, bien, ann, gouv, imageUris,isValid);
                                annonceList.add(annonce);
                                annonceAdapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(e -> {
                            Log.e("Fetch Image", "Failed to fetch image for annonce: " + annonceId, e);
                            if (imageUris.size() == totalImages) {
                                Annonce annonce = new Annonce(annonceId, adresse, superficie, price, numChambres, description, bien, ann, gouv, imageUris,isValid);
                                annonceList.add(annonce);
                                annonceAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            } else {
                Log.e("Fetch Images", "Failed to list images for annonce: " + annonceId, task.getException());
            }
        });
    }
}
