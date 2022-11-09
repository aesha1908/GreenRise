package com.example.greenrise_sgp;

public class SellerOrders {
    String name,totalprice,totalquantity,image;

    public SellerOrders(String name, String totalprice, String totalquantity, String image) {
        this.name = name;
        this.totalprice = totalprice;
        this.totalquantity = totalquantity;
        this.image = image;
    }

    public SellerOrders() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getTotalquantity() {
        return totalquantity;
    }

    public void setTotalquantity(String totalquantity) {
        this.totalquantity = totalquantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
