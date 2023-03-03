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
}
