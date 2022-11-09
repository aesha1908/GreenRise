package com.example.greenrise_sgp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class BuyFragment<paymentsClient> extends Fragment{

    RecyclerView rv;
    cartAdapter adapter;
    Button btn;
    int tP=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy, container, false);
        btn = view.findViewById(R.id.button2);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference cart = db.getReference("Cart");
        DatabaseReference cartForPresentUser = db.getReference("CartPresentUser");
        cart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(dataSnapshot.child("uuid").getValue().toString().equals(uniqueUser.getEmail())) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String unitprice = dataSnapshot.child("unitprice").getValue().toString();
                        String currentdate = dataSnapshot.child("currentdate").getValue().toString();
                        String currenttime = dataSnapshot.child("currenttime").getValue().toString();
                        String totalquantity = dataSnapshot.child("totalquantity").getValue().toString();
                        String totalprice = dataSnapshot.child("totalprice").getValue().toString();
                        String UUID = dataSnapshot.child("uuid").getValue().toString();
                        String SUID = dataSnapshot.child("suid").getValue().toString();
                        String parent = dataSnapshot.child("parent").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();
                        cartModel cm = new cartModel(name, unitprice, currentdate, currenttime, totalquantity, totalprice, UUID, SUID,parent, image);
                        cartForPresentUser.child(parent).setValue(cm);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rv=(RecyclerView) view.findViewById(R.id.conView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<cartModel> options =
                new FirebaseRecyclerOptions.Builder<cartModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("CartPresentUser"),cartModel.class)
                        .build();
        adapter=new cartAdapter(options);
        rv.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartForPresentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tP=0;
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                            tP+=Integer.parseInt(snapshot1.child("totalprice").getValue().toString());
                        }

                        AppCompatActivity activity = (AppCompatActivity)view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new checkoutFragment(String.valueOf(tP))).addToBackStack(null).commit();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}