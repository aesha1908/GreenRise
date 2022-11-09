package com.example.greenrise_sgp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class profileFragment extends Fragment {
    Button switchtoseller,orderuser,wishlistuser,infouser,logout;
    TextView name;
    ImageView uimage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        switchtoseller=view.findViewById(R.id.Usts);
        orderuser = view.findViewById(R.id.Uorders);
        wishlistuser = view.findViewById(R.id.Uwish);
        infouser = view.findViewById(R.id.UInfo);
        logout = view.findViewById(R.id.Ulog);
        name = view.findViewById(R.id.Uname);
        uimage = view.findViewById(R.id.imageView);
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        switchtoseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SellerLoginActivity.class);
                startActivity(intent);
            }
        });
        orderuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new OrderFragment()).addToBackStack(null).commit();

            }
        });
        infouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.frameLayout,new MyInfoFragment()).
                        addToBackStack(null).commit();
            }
        });
        wishlistuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new WishFragment()).addToBackStack(null).commit();
            }
        });
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference cart = db.getReference("CartPresentUser");
        DatabaseReference wish = db.getReference("WishlistPresentUser");
        DatabaseReference order = db.getReference("OrderPresentUser");
        name.setText(CurrentUser.currentUser.getName());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                                cart.child(snapshot1.child("parent").getValue().toString()).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                wish.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                            wish.child(snapshot1.child("parent").getValue().toString()).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                order.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                            order.child(snapshot1.child("parent").getValue().toString()).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(getActivity(),UserLoginActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}