package com.example.greenrise_sgp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class UserOrderAdapter extends FirebaseRecyclerAdapter<cartModel,UserOrderAdapter.myViewHolder>{

    public UserOrderAdapter(@NonNull FirebaseRecyclerOptions<cartModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserOrderAdapter.myViewHolder holder, int position, @NonNull cartModel model) {
        holder.nametext.setText(model.getName());
        holder.price.setText(model.getUnitprice());
        holder.quantity.setText(model.getTotalquantity());
        holder.totalPrice.setText(model.getTotalprice());
        Glide.with(holder.img1.getContext()).load(model.getImage()).into(holder.img1);
    }

    @NonNull
    @Override
    public UserOrderAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleroworderdesign, parent, false);
        return new myViewHolder(view);
    }
    public class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView nametext,price,quantity,totalPrice;
        ImageView img1;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            nametext=itemView.findViewById(R.id.nametext);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);
            totalPrice=itemView.findViewById(R.id.totalPrice);
            img1=itemView.findViewById(R.id.img1);
        }
    }
}
