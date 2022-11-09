package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellerOrdersActivity extends AppCompatActivity {
    BottomNavigationView bnv;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    OrderAdapter orderAdapter;
    ArrayList<SellerOrders> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_orders);
        recyclerView = findViewById(R.id.recyclerViewOrder);
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bnv = findViewById(R.id.bottomnav);
        bnv.setSelectedItemId(R.id.carti);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.homei:
                        Intent intent = new Intent(SellerOrdersActivity.this,SellerHomeActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.profilei:
                        Intent intent1 = new Intent(SellerOrdersActivity.this,SellerInformationActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.carti:
                        return true;
                }
                return false;
            }
        });
        arrayList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, arrayList);
        recyclerView.setAdapter(orderAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    try {
                        SellerOrders orders = dataSnapshot.getValue(SellerOrders.class);
                        arrayList.add(orders);
                    }
                    catch (DatabaseException e)
                    {
                        dataSnapshot.getKey();
                    }
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}