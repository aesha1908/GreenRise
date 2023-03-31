package com.example.greenrise_sgp;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DBQuery {
    public static FirebaseDatabase g_db;
    public static DatabaseReference g_dbref;
    public static List<Plant> p_list = new ArrayList<>();
    public static List<Flower> f_list = new ArrayList<>();
    public static List<Fertilizer> fe_list = new ArrayList<>();
    public static List<Pebble> pe_list = new ArrayList<>();
    public static List<Pots> po_list = new ArrayList<>();
    public static int g_selectedcatindex = 0;
    public static void loadPlants(MyCompleteListner myCompleteListner)
    {
        p_list.clear();
        g_db = FirebaseDatabase.getInstance();
        g_dbref = g_db.getReference("Plant");
        g_dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    try{
                        Plant plant = dataSnapshot.getValue(Plant.class);
                        p_list.add(plant);
                    }
                    catch(DatabaseException e){
                        //Log the exception and the key
                        dataSnapshot.getKey();
                    }
                    myCompleteListner.onSuccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                myCompleteListner.onFailure();
            }
        });
    }
    public static void loadFlowers(MyCompleteListner myCompleteListner){
        f_list.clear();
        g_db = FirebaseDatabase.getInstance();
        g_dbref = g_db.getReference("Flowers");
        g_dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    try{
                        Flower plant = dataSnapshot.getValue(Flower.class);
                        f_list.add(plant);
                    }
                    catch(DatabaseException e){
                        //Log the exception and the key
                        dataSnapshot.getKey();
                    }
                    myCompleteListner.onSuccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                myCompleteListner.onFailure();
            }
        });
    }
    public static void loadFertilizers(MyCompleteListner myCompleteListner){
        fe_list.clear();
        g_db = FirebaseDatabase.getInstance();
        g_dbref = g_db.getReference("Fertilizers");
        g_dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    try{
                        Fertilizer plant = dataSnapshot.getValue(Fertilizer.class);
                        fe_list.add(plant);
                    }
                    catch(DatabaseException e){
                        //Log the exception and the key
                        dataSnapshot.getKey();
                    }
                    myCompleteListner.onSuccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                myCompleteListner.onFailure();
            }
        });
    }
    public static void loadPebbles(MyCompleteListner myCompleteListner) {
        pe_list.clear();
        g_db = FirebaseDatabase.getInstance();
        g_dbref = g_db.getReference("Pebbles");
        g_dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        Pebble plant = dataSnapshot.getValue(Pebble.class);
                        pe_list.add(plant);
                    } catch (DatabaseException e) {
                        //Log the exception and the key
                        dataSnapshot.getKey();
                    }
                    myCompleteListner.onSuccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                myCompleteListner.onFailure();
            }
        });
    }
    public static void loadPots(MyCompleteListner myCompleteListner) {
        po_list.clear();
        g_db = FirebaseDatabase.getInstance();
        g_dbref = g_db.getReference("Pots");
        g_dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        Pots plant = dataSnapshot.getValue(Pots.class);
                        po_list.add(plant);
                    } catch (DatabaseException e) {
                        //Log the exception and the key
                        dataSnapshot.getKey();
                    }
                    myCompleteListner.onSuccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                myCompleteListner.onFailure();
            }
        });
    }
}
