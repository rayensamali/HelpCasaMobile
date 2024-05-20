package com.example.helpcasamobile;

import android.content.Intent;
import android.net.LinkAddress;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Annonce_recu extends AppCompatActivity {

    private Intent intent;

    // Strings to store data
    private String annonceId;
    private String adresse;
    private String superficie;
    private String price;
    private String numChambres;
    private String description;
    private String bien;
    private String ann;
    private String gouv;
    private List<Uri> imageUrls;
    private TextView tpB,tpann,gov,adr,supr,prx,nbch,desc,accept;

    private RecyclerView imgRecyclerView;
    private FirebaseFirestore db;


    private Annonce annonce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce_recu);
    imgRecyclerView = findViewById(R.id.imgRecyclerView);

        db = FirebaseFirestore.getInstance();

    accept = findViewById(R.id.accepter);
    accept.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            accepterann();
        }
    });
        intent = getIntent();
        if (intent != null) {
            // Retrieve the data from Intent extras
            annonceId = intent.getStringExtra("annonceId");
            annonce = intent.getParcelableExtra("annonce");
            // Check if data is not null
            if (annonce != null) {
                // Now you have all the data of the Annonce object
                adresse = annonce.getAdresse();
                superficie = annonce.getSuperficie();
                price = annonce.getPrice();
                numChambres = annonce.getNumChambres();
                description = annonce.getDescription();
                bien = annonce.getBien();
                ann = annonce.getAnn();
                gouv = annonce.getGouv();
                imageUrls = annonce.getImageUrls();
            }
        }


        if (imageUrls != null) {
            for (Uri uri : imageUrls) {
                Log.d("Image URL", uri.toString());
            }
        }

        tpB = findViewById(R.id.typeBien);
        tpB.setText(bien);
        tpann = findViewById(R.id.tyAnn);
        tpann.setText(ann);
        gov = findViewById(R.id.gouv);
        gov.setText(gouv);
        adr = findViewById(R.id.addresse);
        adr.setText(adresse);
        supr = findViewById(R.id.supficie);
        supr.setText(superficie);
        prx = findViewById(R.id.prix);
        prx.setText(price);
        nbch = findViewById(R.id.nbCh);
        nbch.setText(numChambres);
        desc = findViewById(R.id.Descrip);
        desc.setText(description);

        List<Uri> imageUris = new ArrayList<>();
        imageUris = annonce.getImageUrls();
        ImagerecuAdapter imageAdapter = new ImagerecuAdapter(imageUris,Annonce_recu.this);
        imgRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        imgRecyclerView.setAdapter(imageAdapter);



    }

    private void accepterann(){
        if (annonceId != null) {
            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean annonceFound = false;

                                for (DocumentSnapshot userDocument : task.getResult()) {
                                    String userId = userDocument.getId();

                                    db.collection("users")
                                            .document(userId)
                                            .collection("annonces")
                                            .document(annonceId)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            DocumentReference annonceRef = document.getReference();
                                                            annonceRef.update("valid", true)
                                                                    .addOnSuccessListener(aVoid -> {
                                                                        Toast.makeText(Annonce_recu.this, "Annonce accepted!", Toast.LENGTH_SHORT).show();
                                                                    })
                                                                    .addOnFailureListener(e -> {
                                                                        Log.e("accepterAnn", "Error updating document", e);
                                                                        Toast.makeText(Annonce_recu.this, "Error accepting annonce", Toast.LENGTH_SHORT).show();
                                                                    });
                                                        }
                                                    } else {
                                                        Log.e("accepterAnn", "Error getting documents: ", task.getException());
                                                        Toast.makeText(Annonce_recu.this, "Error finding annonce", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Log.e("accepterAnn", "Error getting users: ", task.getException());
                                Toast.makeText(Annonce_recu.this, "Error finding users", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

