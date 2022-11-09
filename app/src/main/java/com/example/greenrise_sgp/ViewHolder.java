package com.example.greenrise_sgp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ViewHolder extends RecyclerView.ViewHolder {
    TextView namev,aboutv,pricev,quantv;
    ImageView imagev;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        namev = itemView.findViewById(R.id.plantname);
        aboutv = itemView.findViewById(R.id.plantabout);
        pricev = itemView.findViewById(R.id.plantprice);
        quantv = itemView.findViewById(R.id.plantquantity);
        imagev = itemView.findViewById(R.id.plantimage);
    }


}
