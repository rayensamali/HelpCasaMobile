package com.example.helpcasamobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AnnonceAdapter extends RecyclerView.Adapter<AnnonceAdapter.AnnonceViewHolder> {
    private List<Annonce> annonces;
    private Context context;
    private SharedPreferences sh;
    private boolean isHistorique;

    public AnnonceAdapter(List<Annonce> annonces, Context context, boolean isHistorique) {
        this.annonces = annonces;
        this.context = context;
        this.isHistorique = isHistorique;
        this.sh = context.getSharedPreferences("userType", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public AnnonceAdapter.AnnonceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isHistorique) {
            view = LayoutInflater.from(context).inflate(R.layout.etat_annonce, parent, false); // Use the historique layout
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.annonce_item, parent, false); // Use the regular layout
        }
        return new AnnonceViewHolder(view, isHistorique);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnonceAdapter.AnnonceViewHolder holder, int position) {
        Annonce annonce = annonces.get(position);
        holder.nomAnn.setText(annonce.getBien());
        holder.prix.setText(annonce.getPrice());
        holder.typeBien.setText(annonce.getBien());
        holder.gouvernorat.setText(annonce.getGouv());
        holder.proprietaire.setText(annonce.getAnn());

        if (holder.isHistorique && holder.etat != null) {
            if(annonce.getValid()==0){
                holder.etat.setText("en cous de validation");
            } else if (annonce.getValid()==1) {
                holder.etat.setText("annonce validé");
            }else {
                holder.etat.setText("non validé");
            }
        }

        Picasso.get().load(annonce.getImageUrls().get(0)).into(holder.img);

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            if (sh.getString("type", "").equals("agent")) {
                intent = new Intent(context, Annonce_recu.class);
            } else {
                intent = new Intent(context, Annonce_detaile.class);
            }
            Log.d("annn", "clicked");
            intent.putExtra("annonceId", annonce.getId());
            intent.putExtra("annonce", annonce);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return annonces.size();
    }

    public static class AnnonceViewHolder extends RecyclerView.ViewHolder {
        TextView nomAnn, prix, typeBien, gouvernorat, proprietaire, etat;
        ImageView img;
        boolean isHistorique;

        public AnnonceViewHolder(@NonNull View itemView, boolean isHistorique) {
            super(itemView);
            this.isHistorique = isHistorique;
            nomAnn = itemView.findViewById(R.id.nomAnn);
            prix = itemView.findViewById(R.id.prix);
            typeBien = itemView.findViewById(R.id.typbien);
            gouvernorat = itemView.findViewById(R.id.gouvernorat);
            proprietaire = itemView.findViewById(R.id.prop);
            img = itemView.findViewById(R.id.img);

            if (isHistorique) {
                etat = itemView.findViewById(R.id.etat);
            }
        }
    }
}

