package com.example.greenrise_sgp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserWishAdapter extends FirebaseRecyclerAdapter<wishModel,UserWishAdapter.myViewHolder>{
    int k=0;
    static int i=1;
    SimpleDateFormat currentTime;
    SimpleDateFormat currentDate;

    public UserWishAdapter(@NonNull FirebaseRecyclerOptions<wishModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserWishAdapter.myViewHolder holder, int position, @NonNull wishModel model) {
        holder.nametext.setText(model.getName());
        holder.price.setText(String.valueOf(model.getUnitprice()));
        Glide.with(holder.img1.getContext()).load(model.getImage()).into(holder.img1);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference wishList = db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("WishList");
        DatabaseReference cart =db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Cart");
        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        k=0;
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                            String s = snapshot1.child("totalquantity").getValue().toString();
                            int q = Integer.parseInt(s) + 1;
                            String up = snapshot1.child("unitprice").getValue().toString();
                            if(snapshot1.child("name").getValue().toString().equals(holder.nametext.getText())&&snapshot1.child("parent").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()+snapshot1.child("name").getValue().toString()))
                            {
                                HashMap updateq = new HashMap();
                                updateq.put("totalquantity",String.valueOf(q));
                                updateq.put("totalprice",String.valueOf(Integer.parseInt(up) * q));
                                cart.child(snapshot1.child("parent").getValue().toString()).updateChildren(updateq);
                                wishList.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+holder.nametext.getText().toString()).removeValue();
                                k=1;
                            }
                        }
                        if(k==0)
                        {
                            currentDate = new SimpleDateFormat("dd-MM-yyyy");
                            currentTime = new SimpleDateFormat("HH:mm:ss");
                            final String t = String.valueOf(currentDate.format(Calendar.getInstance().getTime()));
                            final String d = String.valueOf(currentTime.format(Calendar.getInstance().getTime()));
                            cartModel cm = new cartModel(holder.nametext.getText().toString(),Integer.parseInt(holder.price.getText().toString()), t, d, "1", holder.price.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(),String.valueOf(1),FirebaseAuth.getInstance().getCurrentUser().getUid()+holder.nametext.getText().toString(),holder.img1.toString());
                            cart.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+holder.nametext.getText().toString()).setValue(cm);
                            wishList.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+holder.nametext.getText().toString()).removeValue();
                            i++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        holder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishList.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                            if(snapshot1.child("name").getValue().toString().equals(holder.nametext.getText().toString())&&snapshot1.child("parent").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()+snapshot1.child("name").getValue().toString()))
                            {
                                wishList.child(snapshot1.child("parent").getValue().toString()).removeValue();
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
    public UserWishAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowwishdesign, parent, false);
        return new UserWishAdapter.myViewHolder(view);
    }
    public class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView nametext,price;
        Button b1,b2;
        ImageView img1;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            nametext=itemView.findViewById(R.id.nametext);
            price=itemView.findViewById(R.id.price);
            img1=itemView.findViewById(R.id.wimg);
            b1=itemView.findViewById(R.id.addtocartw);
            b2=itemView.findViewById(R.id.removefromcartw);
        }
    }
}
