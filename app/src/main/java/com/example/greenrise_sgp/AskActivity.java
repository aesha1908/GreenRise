package com.example.greenrise_sgp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class AskActivity extends AppCompatActivity {
    ImageView ivbuy,ivsell;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        ivbuy = findViewById(R.id.imageViewbuy);
        ivsell = findViewById(R.id.imageViewsell);
        mAuth = FirebaseAuth.getInstance();
        ivbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser()!=null)
                {
                    Intent intent = new Intent(AskActivity.this,homePage.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(AskActivity.this,UserLoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        ivsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser()!=null)
                {
                    Intent intent = new Intent(AskActivity.this,SellerHomeActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(AskActivity.this,SellerLoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}