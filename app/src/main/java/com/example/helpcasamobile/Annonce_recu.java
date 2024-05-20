package com.example.helpcasamobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private TextView tpB,tpann,gov,adr,supr,prx,nbch,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce_recu);

        intent = getIntent();
        if (intent != null) {
            // Retrieve the data from Intent extras
            annonceId = intent.getStringExtra("annonceId");
            Annonce annonce = intent.getParcelableExtra("annonce");

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
    }
}
