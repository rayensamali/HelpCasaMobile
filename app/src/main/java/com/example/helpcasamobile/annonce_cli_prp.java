package com.example.helpcasamobile;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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

    private static final int MAX_IMAGES_PER_ANNOUNCEMENT = 5; // Adjust as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce_cli_prp);

        recyclerView = findViewById(R.id.rcy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        annonceList = new ArrayList<>();
        annonceAdapter = new AnnonceAdapter(annonceList, this);
        recyclerView.setAdapter(annonceAdapter);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        fetchAllAnnonces();
    }

    private void fetchAllAnnonces() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot userDocument : task.getResult()) {
                                String userId = userDocument.getId();
                                fetchAnnoncesForUser(userId);
                            }
                        } else {
                            Log.e("Fetch Users", "Error getting users: ", task.getException());
                        }
                    }
                });
    }

    private void fetchAnnoncesForUser(String userId) {
        db.collection("users").document(userId).collection("annonces")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Boolean isValid = document.getBoolean("valid");
                                if (isValid != null && isValid == true) {
                                    String id = document.getId();
                                    String adresse = document.getString("adresse");
                                    String superficie = document.getString("superficie");
                                    String price = document.getString("price");
                                    String numChambres = document.getString("numChambres");
                                    String description = document.getString("description");
                                    String bien = document.getString("bien");
                                    String ann = document.getString("ann");
                                    String gouv = document.getString("gouv");

                                    // Fetch images for this announcement
                                    fetchImagesForAnnonce(userId, id, adresse, superficie, price, numChambres, description, bien, ann, gouv);
                                }
                            }
                        } else {
                            Log.e("Fetch Annonce", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void fetchImagesForAnnonce(String userId, String annonceId, String adresse, String superficie, String price, String numChambres, String description, String bien, String ann, String gouv) {
        List<Uri> imageUris = new ArrayList<>();
        StorageReference imagesRef = storage.getReference().child("users").child(userId).child(annonceId);

        for (int i = 0; i < MAX_IMAGES_PER_ANNOUNCEMENT; i++) {
            StorageReference imageRef = imagesRef.child("image" + i);
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                imageUris.add(uri);
                if (imageUris.size() == MAX_IMAGES_PER_ANNOUNCEMENT) {
                    Annonce annonce = new Annonce(annonceId, adresse, superficie, price, numChambres, description, bien, ann, gouv, imageUris);
                    annonceList.add(annonce);
                    annonceAdapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(e -> {
                Log.e("Fetch Image", "Failed to fetch image for annonce: " + annonceId, e);
                // Optionally handle if some images are not found
            });
        }
    }
}
