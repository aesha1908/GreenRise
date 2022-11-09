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

public class WishFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView rv;
    UserWishAdapter adapter;
    public WishFragment() {
        // Required empty public constructor
    }

    public static WishFragment newInstance(String param1, String param2) {
        WishFragment fragment = new WishFragment();
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
        DatabaseReference wishlist = db.getReference("Wishlist");
        DatabaseReference wishlistForPresentUser = db.getReference("WishlistPresentUser");
        wishlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(dataSnapshot.child("uuid").getValue().toString().equals(uniqueUser.getEmail())) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String unitprice = dataSnapshot.child("unitprice").getValue().toString();
                        String currentdate = dataSnapshot.child("currentdate").getValue().toString();
                        String currenttime = dataSnapshot.child("currenttime").getValue().toString();
                        String UUID = dataSnapshot.child("uuid").getValue().toString();
                        String SUID = dataSnapshot.child("suid").getValue().toString();
                        String parent = dataSnapshot.child("parent").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();
                        wishModel wm = new wishModel(name, unitprice, currentdate, currenttime, UUID, SUID,parent, image);
                        wishlistForPresentUser.child(String.valueOf(parent)).setValue(wm);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rv=(RecyclerView) view.findViewById(R.id.conView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<wishModel> options =
                new FirebaseRecyclerOptions.Builder<wishModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("WishlistPresentUser"),wishModel.class)
                        .build();
        adapter=new UserWishAdapter(options);
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