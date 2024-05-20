package com.example.helpcasamobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class Annonce_recu extends AppCompatActivity {

    private Intent i;
    private String annonceId;
    private String adresse;
    private String superficie;
    private String price;
    private String numChambres;
    private String description;
    private String bien;
    private String ann;
    private String gouv;


    private TextView tpB,tpann,gov,adr,supr,prx,nbch,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce_recu);
        i = getIntent();
        if(i != null){
            annonceId = i.getStringExtra("annonceId");
            Annonce annonce = i.getParcelableExtra("annonce");
            if(annonce != null){
                adresse = annonce.getAdresse();
                superficie = annonce.getSuperficie();
                price = annonce.getPrice();
                numChambres = annonce.getNumChambres();
                description = annonce.getDescription();
                bien = annonce.getBien();
                ann = annonce.getAnn();
                gouv = annonce.getGouv();
                List<String> imageUrls = annonce.getImageUrls();
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