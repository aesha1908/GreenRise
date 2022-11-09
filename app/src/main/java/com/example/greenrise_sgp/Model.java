package com.example.greenrise_sgp;

public class Model {
    String about,image,name,price,quantity,key;

    public Model() {
    }



    public Model(String about, String image, String name, String price, String quantity, String key) {
        this.about = about;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
