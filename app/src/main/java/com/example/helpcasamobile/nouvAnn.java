package com.example.helpcasamobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class nouvAnn extends Fragment {

    private Spinner typbien, typann, Gouvernorat;
    private String bien, ann, gouv;

    private EditText adr, sup, prix, nbch, desc;
    private int valide = 0;
    private TextView ajoutph, envoyer;
    private RecyclerView imgRecyclerView;

    private static final int PICK_IMAGE_REQUEST = 111;
    private ArrayList<Uri> imageUris;
    private ImageAdapter imageAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final int MAX_PHOTOS = 5;

    public nouvAnn() {
        // Required empty public constructor
    }

    public static nouvAnn newInstance(String param1, String param2) {
        nouvAnn fragment = new nouvAnn();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adr = view.findViewById(R.id.adresse);
        sup = view.findViewById(R.id.superficie);
        prix = view.findViewById(R.id.prix);
        nbch = view.findViewById(R.id.nmbChamb);
        desc = view.findViewById(R.id.dscrip);

        ajoutph = view.findViewById(R.id.ajouter);
        envoyer = view.findViewById(R.id.envoyer);
        imgRecyclerView = view.findViewById(R.id.imgRecyclerView);

        imageUris = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageUris);

        imgRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        imgRecyclerView.setAdapter(imageAdapter);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setTypbien(view);
        setGouvernorat(view);
        setTypann(view);

        ajoutph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAnnonce();
            }
        });
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Pictures"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count && imageUris.size() < MAX_PHOTOS; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                } else if (data.getData() != null && imageUris.size() < MAX_PHOTOS) {
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                }
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setTypbien(View view) {
        typbien = view.findViewById(R.id.type_bien);
        String[] options = {"Appartement", "Villa", "Bureau", "Terrain"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_layout, options);
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

    private void setTypann(View view) {
        typann = view.findViewById(R.id.type_annonce);
        String[] options = {"Vente", "Location"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_layout, options);
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

    private void setGouvernorat(View view) {
        Gouvernorat = view.findViewById(R.id.gouver);
        String[] cities = {
                "Ariana", "Béja", "Ben Arous", "Bizerte", "Gabès", "Gafsa", "Jendouba",
                "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine",
                "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse",
                "Tataouine", "Tozeur", "Tunis", "Zaghouan"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_layout, cities);
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

    private void sendAnnonce() {
        String adresse = adr.getText().toString();
        String superficie = sup.getText().toString();
        String price = prix.getText().toString();
        String numChambres = nbch.getText().toString();
        String description = desc.getText().toString();

        if (adresse.isEmpty() || superficie.isEmpty() || price.isEmpty() || numChambres.isEmpty() || description.isEmpty() || imageUris.isEmpty()) {
            Toast.makeText(getActivity(), "Veuillez remplir tous les champs et ajouter au moins une photo", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String annonceId = UUID.randomUUID().toString();
            Map<String, Object> annonce = new HashMap<>();
            annonce.put("adresse", adresse);
            annonce.put("superficie", superficie);
            annonce.put("price", price);
            annonce.put("numChambres", numChambres);
            annonce.put("description", description);
            annonce.put("bien", bien);
            annonce.put("ann", ann);
            annonce.put("gouv", gouv);
            annonce.put("valid", valide);

            // Upload images to Firebase Storage
            for (int i = 0; i < imageUris.size(); i++) {
                Uri imageUri = imageUris.get(i);
                StorageReference imageRef = FirebaseStorage.getInstance().getReference()
                        .child("users")
                        .child(userId)
                        .child(annonceId)
                        .child("image" + i);

                imageRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Store image URLs in Firestore or Firebase database
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Erreur lors de l'upload de l'image", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            db.collection("annonces")
                    .document(annonceId)
                    .set(annonce)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Annonce envoyée avec succès", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Erreur lors de l'envoi de l'annonce", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
