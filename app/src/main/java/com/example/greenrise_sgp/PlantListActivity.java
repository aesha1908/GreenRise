package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.Model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PlantListActivity extends AppCompatActivity {
    GridView recyclerView;
    ArrayList<Plant> list;
    PlantAdapterTrial adapter;
    DatabaseReference reference;
    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list2);
        bnv = findViewById(R.id.bottomnav);
        recyclerView = findViewById(R.id.gv);
//        recyclerView.setHasFixedSize(true);
      //  reference = FirebaseDatabase.getInstance().getReference("Plant");
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
      //  list = new ArrayList<>();
        adapter = new PlantAdapterTrial(this,DBQuery.p_list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
//        FirebaseRecyclerOptions<Plant> options =
//                new FirebaseRecyclerOptions.Builder<Plant>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Plant"),Plant.class)
//                        .build();
        //adapter = new PlantAdapter(options);
//        recyclerView.setAdapter(adapter);
//        reference.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    try{
//                        Plant plant = dataSnapshot.getValue(Plant.class);
//                        list.add(plant);
//                    }
//                    catch(DatabaseException e){
//                        //Log the exception and the key
//                        dataSnapshot.getKey();
//                    }
//
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

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
                        Intent intent1 = new Intent(PlantListActivity.this,SellerProfileActivity.class);
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
//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
}