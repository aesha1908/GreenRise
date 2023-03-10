package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class PlantListActivity extends AppCompatActivity {
    GridView recyclerView;
    ArrayList<Plant> list;
    PlantAdapter adapter;
    DatabaseReference reference;
    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list2);
        bnv = findViewById(R.id.bottomnav);
        recyclerView = findViewById(R.id.gv);
        adapter = new PlantAdapter(this,DBQuery.p_list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.homei:
                        Intent intent = new Intent(PlantListActivity.this,SellerHomeActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.profilei:
                        Intent intent1 = new Intent(PlantListActivity.this,SellerInformationActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.carti:
                        Intent intent2 = new Intent(PlantListActivity.this,SellerOrdersActivity.class);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });
    }
}