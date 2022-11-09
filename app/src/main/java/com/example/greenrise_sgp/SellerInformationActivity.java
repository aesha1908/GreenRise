package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerInformationActivity extends AppCompatActivity {
    Button Sorder, sellerinfo, switchtobuyer, logout;
    BottomNavigationView bnv;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_information);
        Sorder = findViewById(R.id.Sorders);
        sellerinfo = findViewById(R.id.SInfo);
        switchtobuyer = findViewById(R.id.Sstb);
        FirebaseDatabase database;
        DatabaseReference reference;
        logout = findViewById(R.id.Slog);
        bnv = findViewById(R.id.bottomnav);
        tv = findViewById(R.id.Sellname);
        switchtobuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerInformationActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });
        sellerinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerInformationActivity.this, SellerProfileActivity.class);
                startActivity(intent);
            }
        });
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Sellers");
        tv.setText(CurrentSeller.currentSeller.getName());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerInformationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });
        Sorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerInformationActivity.this, SellerOrdersActivity.class);
                startActivity(intent);
            }
        });
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.homei:
                        Intent intent1 = new Intent(SellerInformationActivity.this,SellerHomeActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.profilei:
                        Intent intent3 = new Intent(SellerInformationActivity.this,SellerProfileActivity.class);
                        startActivity(intent3);
                    case R.id.carti:
                        Intent intent2 = new Intent(SellerInformationActivity.this,SellerOrdersActivity.class);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });
    }
}