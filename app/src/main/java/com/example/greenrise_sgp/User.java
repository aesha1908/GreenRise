package com.example.greenrise_sgp;

public class User {
    String name, email, phone, pass, confpass;

    public User() {

    }

    public User(String name, String email, String phone, String pass, String confpass) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
        this.confpass = confpass;
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
}
