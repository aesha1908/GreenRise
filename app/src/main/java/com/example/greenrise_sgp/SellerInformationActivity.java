package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.UserWriteRecord;

import java.util.List;

public class SellerInformationActivity extends AppCompatActivity {
    Button Sorder, sellerinfo, switchtobuyer, logout;
    BottomNavigationView bnv;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
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
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        List<? extends UserInfo> userInfos = firebaseUser.getProviderData();
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
        String uid = mAuth.getCurrentUser().getUid();
        for (UserInfo userInfo : userInfos) {
            String providerId = userInfo.getProviderId();
            if (providerId.equals(PhoneAuthProvider.PROVIDER_ID)) {
                String phone = userInfo.getPhoneNumber();
                tv.setText(phone);
            }
            reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Seller seller = snapshot.getValue(Seller.class);
                    if (seller != null) {
                        String name = seller.name;
                        tv.setText("Welcome " + name + " !");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
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
                    switch (item.getItemId()) {
                        case R.id.homei:
                            Intent intent = new Intent(SellerInformationActivity.this, SellerHomeActivity.class);
                            startActivity(intent);
                            return true;
                        case R.id.profilei:
                            return true;
                        case R.id.carti:
                            Intent intent1 = new Intent(SellerInformationActivity.this, SellerOrdersActivity.class);
                            startActivity(intent1);
                            return true;
                    }
                    return false;
                }
            });
        }
    }
}
