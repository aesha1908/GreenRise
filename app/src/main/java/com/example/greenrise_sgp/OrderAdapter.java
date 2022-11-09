package com.example.greenrise_sgp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.myViewHolder> {
    Context context;
    ArrayList<SellerOrders> arrayList;

    public OrderAdapter(Context context, ArrayList<SellerOrders> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orderentry,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        SellerOrders sellerOrders = arrayList.get(position);
        holder.namev.setText(sellerOrders.getName());
        holder.pricev.setText(sellerOrders.getTotalprice());
        holder.quantityv.setText(sellerOrders.getTotalquantity());
        Glide.with(context).load(sellerOrders.getImage()).into(holder.orderimg);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView namev,pricev,quantityv;
        ImageView orderimg;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            namev = itemView.findViewById(R.id.ordername);
            pricev = itemView.findViewById(R.id.orderprice);
            quantityv = itemView.findViewById(R.id.orderquantity);
            orderimg = itemView.findViewById(R.id.orderimage);
        }
    }
}
