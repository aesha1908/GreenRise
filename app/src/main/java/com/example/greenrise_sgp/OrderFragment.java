package com.example.greenrise_sgp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class OrderFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView rv;
   UserOrderAdapter adapter;
    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order, container, false);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference order = db.getReference("Orders");
        DatabaseReference orderForPresentUser = db.getReference("OrderPresentUser");
        order.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        orderForPresentUser.child(String.valueOf(parent)).setValue(cm);
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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("OrderPresentUser"),cartModel.class)
                        .build();
        adapter=new UserOrderAdapter(options);
        rv.setAdapter(adapter);
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