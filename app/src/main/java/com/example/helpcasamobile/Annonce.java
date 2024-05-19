
package com.example.helpcasamobile;

public class Annonce {
    private String id;
    private String adresse;
    private String superficie;
    private String price;
    private String numChambres;
    private String description;
    private String bien;
    private String ann;
    private String gouv;
    private String imageUrl; // URL of the image

    // Default constructor


    // Parameterized constructor
    public Annonce(String id, String adresse, String superficie, String price, String numChambres, String description, String bien, String ann, String gouv, String imageUrl) {
        this.id = id;
        this.adresse = adresse;
        this.superficie = superficie;
        this.price = price;
        this.numChambres = numChambres;
        this.description = description;
        this.bien = bien;
        this.ann = ann;
        this.gouv = gouv;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSuperficie() {
        return superficie;
    }

    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumChambres() {
        return numChambres;
    }

    public void setNumChambres(String numChambres) {
        this.numChambres = numChambres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBien() {
        return bien;
    }

    public void setBien(String bien) {
        this.bien = bien;
    }

    public String getAnn() {
        return ann;
    }

    public void setAnn(String ann) {
        this.ann = ann;
    }

    public String getGouv() {
        return gouv;
    }

    public void setGouv(String gouv) {
        this.gouv = gouv;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
