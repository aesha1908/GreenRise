package com.example.greenrise_sgp;

public class cartModel {
    String name,currentdate,currenttime,totalquantity,totalprice,UUID,SUID,parent,image;
    int unitprice;



    public cartModel() {
    }

    public cartModel(String name, int unitprice, String currentdate, String currenttime, String totalquantity, String totalprice, String UUID, String SUID, String parent,String image) {
        this.name = name;
        this.unitprice = unitprice;
        this.currentdate = currentdate;
        this.currenttime = currenttime;
        this.totalquantity = totalquantity;
        this.totalprice = totalprice;
        this.UUID = UUID;
        this.SUID = SUID;
        this.parent = parent;
        this.image=image;
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

    public int getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(int unitprice) {
        this.unitprice = unitprice;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getTotalquantity() {
        return totalquantity;
    }

    public void setTotalquantity(String totalquantity) {
        this.totalquantity = totalquantity;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getSUID() {
        return SUID;
    }

    public void setSUID(String SUID) {
        this.SUID = SUID;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}


