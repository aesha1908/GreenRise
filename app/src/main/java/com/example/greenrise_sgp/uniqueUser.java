package com.example.greenrise_sgp;

public class uniqueUser {
    static  String email;
    uniqueUser(String email)
    {
        this.email=email;
    }

    public uniqueUser() {
    }

    public static String getEmail() {
        return email;
    }

}
