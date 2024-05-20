package com.example.helpcasamobile;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImagerecuAdapter  extends RecyclerView.Adapter<ImagerecuAdapter.ViewHolder>{

    private ArrayList<Uri> imageUris;
    private Context context;

    public ImagerecuAdapter(List<Uri> imageUris, Context context) {
        this.imageUris= (ArrayList<Uri>) imageUris;
        this.context = context;
    }
    @NonNull
    @Override
    public ImagerecuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagerecuAdapter.ViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        Picasso.get().load(imageUri).into(holder.imageView);
    }



    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
