package com.example.helpcasamobile;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class annrecu extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnnonceAdapter annonceAdapter;
    private List<Annonce> annonceList;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Set<String> annonceIds;

    private static final int MAX_IMAGES_PER_ANNOUNCEMENT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annrecu);

        recyclerView = findViewById(R.id.annrecu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        annonceList = new ArrayList<>();
        annonceAdapter = new AnnonceAdapter(annonceList, this, false);
        recyclerView.setAdapter(annonceAdapter);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        annonceIds = new HashSet<>();

        fetchUsersWithAnnonces();
    }

    private void fetchUsersWithAnnonces() {
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
                .whereEqualTo("valid", 0) // Only fetch announcements with valid == 0
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
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

                                annonceIds.add(id);
                                fetchImagesForAnnonce(userId, id, adresse, superficie, price, numChambres, description, bien, ann, gouv);
                            }
                        }
                    } else {
                        Log.e("Fetch Annonce", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void fetchImagesForAnnonce(String userId, String annonceId, String adresse, String superficie, String price, String numChambres, String description, String bien, String ann, String gouv) {
        List<Uri> imageUris = new ArrayList<>();
        StorageReference imagesRef = storage.getReference().child("users").child(userId).child(annonceId);

        List<Task<Uri>> tasks = new ArrayList<>();
        for (int i = 0; i < MAX_IMAGES_PER_ANNOUNCEMENT; i++) {
            StorageReference imageRef = imagesRef.child("image" + i);
            tasks.add(imageRef.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    imageUris.add(task.getResult());
                } else {
                    Exception e = task.getException();
                    if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        Log.e("Fetch Images", "Image not found: image" );
                    } else {
                        Log.e("Fetch Images", "Failed to fetch image: image" , e);
                    }
                }
            }));
        }

        Tasks.whenAllComplete(tasks).addOnCompleteListener(task -> {
            Annonce annonce = new Annonce(annonceId, adresse, superficie, price, numChambres, description, bien, ann, gouv, imageUris, 0L);
            annonceList.add(annonce);
            annonceAdapter.notifyDataSetChanged();
        });
    }
}
