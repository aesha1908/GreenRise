package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GroupActivity extends AppCompatActivity {
    GridLayout gridView;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        gridView = findViewById(R.id.gvgrp);
        bottomNavigationView = findViewById(R.id.bottomnav);
        imageView1 = findViewById(R.id.test_image);
        imageView2 = findViewById(R.id.test_image2);
        imageView3 = findViewById(R.id.test_image3);
        imageView4 = findViewById(R.id.test_image4);
        imageView5 = findViewById(R.id.test_image5);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBQuery.loadFlowers(new MyCompleteListner() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(GroupActivity.this, FlowerListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBQuery.loadPlants(new MyCompleteListner() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(GroupActivity.this, PlantListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(GroupActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBQuery.loadFertilizers(new MyCompleteListner() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(GroupActivity.this, FertilizerListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(GroupActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBQuery.loadPebbles(new MyCompleteListner() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(GroupActivity.this, PebblesListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(GroupActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBQuery.loadPots(new MyCompleteListner() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(GroupActivity.this, PotsListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(GroupActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.homei:
                        Intent intent = new Intent(GroupActivity.this,SellerHomeActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.profilei:
                        Intent intent1 = new Intent(GroupActivity.this,SellerInformationActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.carti:
                        Intent intent2 = new Intent(GroupActivity.this,SellerOrdersActivity.class);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });
    }
}