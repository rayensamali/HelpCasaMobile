package com.example.helpcasamobile;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Annonce implements Parcelable {
    private String id;
    private String adresse;
    private String superficie;
    private String price;
    private String numChambres;
    private String description;
    private String bien;
    private String ann;
    private String gouv;
    private List<Uri> imageUrls; // List of image URLs
    private Long valid; // Validation state

    // Constructor
    public Annonce(String id, String adresse, String superficie, String price, String numChambres,
                   String description, String bien, String ann, String gouv, List<Uri> imageUrls, Long valid) {
        this.id = id;
        this.adresse = adresse;
        this.superficie = superficie;
        this.price = price;
        this.numChambres = numChambres;
        this.description = description;
        this.bien = bien;
        this.ann = ann;
        this.gouv = gouv;
        this.imageUrls = imageUrls;
        this.valid = valid;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getSuperficie() {
        return superficie;
    }

    public String getPrice() {
        return price;
    }

    public String getNumChambres() {
        return numChambres;
    }

    public String getDescription() {
        return description;
    }

    public String getBien() {
        return bien;
    }

    public String getAnn() {
        return ann;
    }

    public String getGouv() {
        return gouv;
    }

    public List<Uri> getImageUrls() {
        return imageUrls;
    }

    public Long getValid() {
        return valid;
    }

    public void setValid(Long valid) {
        this.valid = valid;
    }

    // Parcelable implementation
    protected Annonce(Parcel in) {
        id = in.readString();
        adresse = in.readString();
        superficie = in.readString();
        price = in.readString();
        numChambres = in.readString();
        description = in.readString();
        bien = in.readString();
        ann = in.readString();
        gouv = in.readString();
        imageUrls = new ArrayList<>();
        in.readTypedList(imageUrls, Uri.CREATOR);
        if (in.readByte() == 0) {
            valid = null;
        } else {
            valid = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(adresse);
        dest.writeString(superficie);
        dest.writeString(price);
        dest.writeString(numChambres);
        dest.writeString(description);
        dest.writeString(bien);
        dest.writeString(ann);
        dest.writeString(gouv);
        dest.writeTypedList(imageUrls);
        if (valid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(valid);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Annonce> CREATOR = new Creator<Annonce>() {
        @Override
        public Annonce createFromParcel(Parcel in) {
            return new Annonce(in);
        }

        @Override
        public Annonce[] newArray(int size) {
            return new Annonce[size];
        }
    };
}
