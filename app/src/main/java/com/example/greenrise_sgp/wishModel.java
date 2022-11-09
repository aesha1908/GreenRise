package com.example.greenrise_sgp;

public class wishModel {
    String name,unitprice,currentdate,currenttime,UUID,SUID,parent,image;

    public wishModel() {
    }

    public wishModel(String name, String unitprice, String currentdate, String currenttime, String UUID, String SUID, String parent, String image) {
        this.name = name;
        this.unitprice = unitprice;
        this.currentdate = currentdate;
        this.currenttime = currenttime;
        this.UUID = UUID;
        this.SUID = SUID;
        this.parent = parent;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
