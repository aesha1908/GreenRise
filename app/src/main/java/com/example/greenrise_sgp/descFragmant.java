package com.example.greenrise_sgp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class descFragmant extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String about,image,name,price,quantity,parent;
    SimpleDateFormat currentTime;
    SimpleDateFormat currentDate;
 //     int quantityincart;
    private static final  String h1="THE_C";
    int k;
    int m=0;
    static int i=1;
    static int h=1;
    public descFragmant() {

    }
    public descFragmant(String about,String image,String name,String price,String quantity,String parent) {
        this.about=about;
        this.image=image;
        this.name=name;
        this.price=price;
        this.quantity=quantity;
        this.parent=parent;
    }

    public static descFragmant newInstance(String param1, String param2) {
        descFragmant fragment = new descFragmant();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_desc_fragmant, container, false);
        ImageView imageholder=view.findViewById(R.id.imagegholder);
        TextView nameholder=view.findViewById(R.id.nameholder);
        TextView aboutholder=view.findViewById(R.id.aboutholder);
        TextView priceholder=view.findViewById(R.id.priceholder);
        TextView quantityholder=view.findViewById(R.id.quantityholder);
        Button btn=view.findViewById(R.id.button);
//        Button btn1=view.findViewById(R.id.button3);
        Button btn2=view.findViewById(R.id.button4);
        nameholder.setText(name);
        aboutholder.setText(about);
        priceholder.setText(price);
        if(!quantity.equals(0)) {
            quantityholder.setText("Available");
            quantityholder.setTextColor(Color.GREEN);
        }
        else
        {
            quantityholder.setText("Not Available");
            quantityholder.setTextColor(Color.RED);
        }
        Glide.with(getContext()).load(image).into(imageholder);
        if(Integer.parseInt(quantity)==0) {
            btn.setEnabled(false);
//            btn1.setEnabled(false);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k=0;
                btn.setEnabled(false);
                FirebaseAuth fa = FirebaseAuth.getInstance();
                currentDate = new SimpleDateFormat("dd-MM-yyyy");
                currentTime = new SimpleDateFormat("HH:mm:ss");
                final String t = String.valueOf(currentDate.format(Calendar.getInstance().getTime()));
                final String d = String.valueOf(currentTime.format(Calendar.getInstance().getTime()));
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference cart = db.getReference("Cart");
                cart.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                         //   Log.i("The_C", s);
                            if (snapshot1.child("name").getValue().toString().equals(name)&&snapshot1.child("uuid").getValue().toString().equals(uniqueUser.getEmail())) {
                                k=1;
                                String  s = snapshot1.child("totalquantity").getValue().toString();
                                if(Integer.parseInt(s)==Integer.parseInt(quantity))
                                {
                                    Toast.makeText(view.getContext(),"Please order this many items first",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                int q=Integer.parseInt(s)+1;
                                String up=snapshot1.child("unitprice").getValue().toString();
                         //       Log.i("The_C", "h1");
                                HashMap updateq=new HashMap();
                                updateq.put("totalquantity",String.valueOf(q));
                                updateq.put("totalprice",String.valueOf(Integer.parseInt(up)*q));
                                cart.child(snapshot1.child("parent").getValue().toString()).updateChildren(updateq);
                            }
                        }
                     //   System.out.println(k);
                        if (k==0) {
                            cartModel cm = new cartModel(name, price, t, d, "1", price,uniqueUser.getEmail(),String.valueOf(1),parent,image);
                            cart.child(parent).setValue(cm);
                            i++;
                        }

                        btn.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btn1.setEnabled(false);
//                FirebaseDatabase db = FirebaseDatabase.getInstance();
//                DatabaseReference cart = db.getReference("Cart");
//                cart.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot snapshot1:snapshot.getChildren())
//                        {
//                            if(snapshot1.child("name").getValue().toString().equals(name)&&snapshot1.child("uuid").equals(uniqueUser.getEmail()))
//                            {
//                                cart.child(snapshot1.child("parent").getValue().toString()).removeValue();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                btn1.setEnabled(true);
//            }
//        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentDate = new SimpleDateFormat("dd-MM-yyyy");
                currentTime = new SimpleDateFormat("HH:mm:ss");
                final String t = String.valueOf(currentDate.format(Calendar.getInstance().getTime()));
                final String d = String.valueOf(currentTime.format(Calendar.getInstance().getTime()));
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference wishlist = db.getReference("Wishlist");
                wishlist.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        m=0;
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                            if(snapshot1.child("name").getValue().toString().equals(name)&&snapshot1.child("uuid").equals(uniqueUser.getEmail()))
                            {
                                m=1;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(m==0) {
                    wishModel wm = new wishModel(name, price, t, d, uniqueUser.getEmail(), String.valueOf(1), parent, image);
                    wishlist.child(parent).setValue(wm);
                    h++;
                }
                }

        });

        return view;
    }


    public void onBackPressed()
    {
        AppCompatActivity activity = (AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new HomeFragment()).addToBackStack(null).commit();

    }
}