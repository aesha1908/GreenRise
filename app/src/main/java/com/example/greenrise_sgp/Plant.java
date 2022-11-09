package com.example.greenrise_sgp;

import java.io.Serializable;
import java.util.Map;
import java.util.PrimitiveIterator;

public class Plant implements Serializable {
    private String name;
    private String about;
    private String price;
    private String quantity;
    private String image;
    private String key;
    public Plant(){

    }



    public Plant(String name, String about, String price, String quantity, String image, String key) {
        this.name = name;
        this.about = about;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.key = key;
        //this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //    public Map getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Map timestamp) {
//        this.timestamp = timestamp;
//    }
}