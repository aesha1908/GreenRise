package com.example.greenrise_sgp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<Model,myadapter.myViewHolder> {

    public myadapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Model model) {
        holder.nametext.setText(model.getName());
        holder.price.setText("Price:"+model.getPrice());
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
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        TextView  nametext,price,quantity;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img1=itemView.findViewById(R.id.img1);
            nametext=itemView.findViewById(R.id.nametext);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);
        }
    }
}
