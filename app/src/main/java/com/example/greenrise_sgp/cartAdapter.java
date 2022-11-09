package com.example.greenrise_sgp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class cartAdapter extends FirebaseRecyclerAdapter< cartModel ,cartAdapter.myViewHolder>{

    public cartAdapter(@NonNull FirebaseRecyclerOptions<cartModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull cartModel model) {
            holder.nametext.setText(model.getName());
            holder.price.setText(model.getUnitprice());
            holder.quantity.setText(model.getTotalquantity());
            holder.totalPrice.setText(model.getTotalprice());
            Glide.with(holder.img1.getContext()).load(model.getImage()).into(holder.img1);

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference cart = db.getReference("Cart");
        DatabaseReference cart1 = db.getReference("CartPresentUser");
            holder.imgb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cart.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                String s = snapshot1.child("totalquantity").getValue().toString();
                                int q = Integer.parseInt(s) + 1;
                                String up = snapshot1.child("unitprice").getValue().toString();
                                if (snapshot1.child("name").getValue().toString().equals(holder.nametext.getText())&&snapshot1.child("uuid").getValue().toString().equals(uniqueUser.getEmail())) {
                                    HashMap updateq = new HashMap();
                                    updateq.put("totalquantity",String.valueOf(q));
                                    updateq.put("totalprice",String.valueOf(Integer.parseInt(up) * q));
                                    cart.child(snapshot1.child("parent").getValue().toString()).updateChildren(updateq);
                                    if(snapshot1.child("uuid").getValue().toString().equals(uniqueUser.getEmail()))
                                    cart1.child(snapshot1.child("parent").getValue().toString()).updateChildren(updateq);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            holder.imgb3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cart.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                String s = snapshot1.child("totalquantity").getValue().toString();
                                int q = Integer.parseInt(s) - 1;
                                String up = snapshot1.child("unitprice").getValue().toString();
                                if (snapshot1.child("name").getValue().toString().equals(holder.nametext.getText())&&snapshot1.child("uuid").getValue().toString().equals(uniqueUser.getEmail())) {
                                    HashMap updateq = new HashMap();
                                    updateq.put("totalquantity",String.valueOf(q));
                                    updateq.put("totalprice",String.valueOf(Integer.parseInt(up) * q));
                                    cart.child(snapshot1.child("parent").getValue().toString()).updateChildren(updateq);
                                    if(snapshot1.child("uuid").getValue().toString().equals(uniqueUser.getEmail()))
                                        cart1.child(snapshot1.child("parent").getValue().toString()).updateChildren(updateq);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            holder.imgb2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cart.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                if (snapshot1.child("name").getValue().toString().equals(holder.nametext.getText())&&snapshot1.child("uuid").getValue().toString().equals(uniqueUser.getEmail())) {
                                    cart.child(snapshot1.child("parent").getValue().toString()).removeValue();
                                    if(snapshot1.child("uuid").getValue().toString().equals(uniqueUser.getEmail()))
                                        cart1.child(snapshot1.child("parent").getValue().toString()).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });


    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowcartdesign, parent, false);
            return new myViewHolder(view);
    }


    public class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton imgb1,imgb2,imgb3;
        TextView nametext,price,quantity,totalPrice;
        ImageView img1;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imgb1=itemView.findViewById(R.id.imageButton);
            imgb2=itemView.findViewById(R.id.imageButton2);
            imgb3=itemView.findViewById(R.id.imageButton3);
            nametext=itemView.findViewById(R.id.nametext);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);
            totalPrice=itemView.findViewById(R.id.totalPrice);
            img1=itemView.findViewById(R.id.img1);
        }
    }

}
