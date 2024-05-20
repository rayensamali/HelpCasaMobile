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

    public AnnonceAdapter(List<Annonce> annonces, Context context) {
        this.annonces = annonces;
        this.context = context;
        this.sh = context.getSharedPreferences("userType", Context.MODE_PRIVATE); // Initialize SharedPreferences here
    }

    @NonNull
    @Override
    public AnnonceAdapter.AnnonceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.annonce_item, parent, false);
        return new AnnonceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnonceAdapter.AnnonceViewHolder holder, int position) {
        Annonce annonce = annonces.get(position);
        holder.nomAnn.setText(annonce.getBien());
        holder.prix.setText("Prix: " + annonce.getPrice());
        holder.typeBien.setText("Type de bien: " + annonce.getBien());
        holder.gouvernorat.setText("Gouvernorat: " + annonce.getGouv());
        holder.proprietaire.setText("Propriétaire: " + annonce.getAnn());
        Picasso.get().load(annonce.getImageUrls().get(0)).into(holder.img);

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            if (sh.getString("type","").equals("agent")) {
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
        TextView nomAnn, prix, typeBien, gouvernorat, proprietaire;
        ImageView img;

        public AnnonceViewHolder(@NonNull View itemView) {
            super(itemView);
            nomAnn = itemView.findViewById(R.id.nomAnn);
            prix = itemView.findViewById(R.id.prix);
            typeBien = itemView.findViewById(R.id.typbien);
            gouvernorat = itemView.findViewById(R.id.gouvernorat);
            proprietaire = itemView.findViewById(R.id.prop);
            img = itemView.findViewById(R.id.img);
        }
    }
}
