package com.example.greenrise_sgp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PlantAdapterTrial extends BaseAdapter {
    Context context;
    List<Plant> list;

    public PlantAdapterTrial(Context context, List<Plant> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myvie;
        if(view==null)
        {
            myvie = LayoutInflater.from(context).inflate(R.layout.plantentry,viewGroup,false);
        }
        else {
            myvie = view;
        }
        myvie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBQuery.g_selectedcatindex = i;
                Intent intent =new Intent(view.getContext(),PlantListActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        ImageView iv;
        TextView name,desc,price,quantity;
        iv = myvie.findViewById(R.id.plantimage);
        name = myvie.findViewById(R.id.plantname);
        desc = myvie.findViewById(R.id.plantabout);
        price = myvie.findViewById(R.id.plantprice);
        quantity = myvie.findViewById(R.id.plantquantity);
        Glide.with(iv.getContext()).load(list.get(i).getImage()).into(iv);
        name.setText(list.get(i).getName());
        desc.setText(list.get(i).getAbout());
        price.setText(list.get(i).getPrice());
        quantity.setText(list.get(i).getQuantity());
        return myvie;
    }
}
