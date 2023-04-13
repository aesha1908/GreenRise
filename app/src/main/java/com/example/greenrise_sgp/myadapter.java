package com.example.greenrise_sgp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;


public class myadapter extends FirebaseRecyclerAdapter<Model,myadapter.myViewHolder> {

    public myadapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Model model) {
        holder.ib11.setVisibility(View.GONE);
        holder.ib12.setVisibility(View.GONE);
        holder.quantityInCart.setVisibility(View.GONE);
        holder.nametext.setText(model.getName());
        holder.price.setText("Rs."+String.valueOf(model.getPrice()));
        if(Integer.parseInt(model.getQuantity())>0) {
            holder.quantity.setText("Available");
            holder.quantity.setTextColor(Color.GREEN);
        }
        else {
            holder.quantity.setText("Not Available");
            holder.quantity.setTextColor(Color.RED);
        }
        Glide.with(holder.img1.getContext()).load(model.getImage()).into(holder.img1);
        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new descFragmant(model.getAbout(),model.getImage(),model.getName(),model.getPrice(),model.getQuantity(),model.getKey())).addToBackStack(null).commit();

            }
        });
        holder.lb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()==true)
                {

                }
                else
                {

                }
            }
        });
        holder.ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                holder.ib11.setVisibility(View.VISIBLE);
                holder.ib12.setVisibility(View.VISIBLE);
                holder.quantityInCart.setText("1");
                holder.quantityInCart.setVisibility(View.VISIBLE);
            }
        });
        holder.ib11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.ib12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img1,ib1,ib11,ib12;
        TextView  nametext,price,quantity,quantityInCart;
        CheckBox lb;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img1=itemView.findViewById(R.id.img1);
            nametext=itemView.findViewById(R.id.nametext);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);
            lb=itemView.findViewById(R.id.wish_Adder);
            ib1=itemView.findViewById(R.id.add);
            ib11=itemView.findViewById(R.id.addInside);
            ib12=itemView.findViewById(R.id.subInside);
            quantityInCart=itemView.findViewById(R.id.quantityText);
        }
    }
}
