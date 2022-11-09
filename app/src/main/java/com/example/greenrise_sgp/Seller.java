package com.example.greenrise_sgp;

import android.widget.Spinner;

public class Seller {

    public String name,email,phone,pass,confpass,selltype,upiid;

    public Seller() {
    }

    public Seller(String name, String email, String phone, String pass, String confpass, String selltype,String upiid) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
        this.confpass = confpass;
        this.selltype = selltype;
        this.upiid = upiid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getConfpass() {
        return confpass;
    }

    public void setConfpass(String confpass) {
        this.confpass = confpass;
    }

    public String getSelltype() {
        return selltype;
    }

    public void setSelltype(String selltype) {
        this.selltype = selltype;
    }

    public String getUpiid() {
        return upiid;
    }

    public void setUpiid(String upiid) {
        this.upiid = upiid;
    }
}
