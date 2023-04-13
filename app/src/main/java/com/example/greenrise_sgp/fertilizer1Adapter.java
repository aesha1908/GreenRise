package com.example.greenrise_sgp;

import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class fertilizer1Adapter extends FirebaseRecyclerAdapter<Model, fertilizer1Adapter.myViewHolder> {
    private Translator translator,translator1;
    private boolean downloaded=false,downloaded1=false;
    static boolean gujarati=false,hindi=false;
    String ret,ret1,ans;
    com.example.greenrise_sgp.myadapter.myViewHolder holder;
    String about,image,name,quantity,parent;
    int price;
    SimpleDateFormat currentTime;
    SimpleDateFormat currentDate;
    int m=0,k=0;
    Model model;
    public fertilizer1Adapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Model model) {
//        holder.ib11.setVisibility(View.GONE);
//        holder.ib12.setVisibility(View.GONE);
//        holder.quantityInCart.setVisibility(View.GONE);
        holder.nametext.setText(model.getName());
        name=model.getName();
        about=model.getAbout();
        image=model.getImage();
        quantity=model.getQuantity();
        price=model.getPrice();
        String s=model.getName();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference plant =  db.getReference("Fertilizers");
        plant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    if(snapshot1.child("name").getValue().toString().equals(name)&&snapshot1.child("parent").equals(FirebaseAuth.getInstance().getCurrentUser().getUid()+snapshot1.child("name").getValue().toString()))
                    {
                        holder.lb.setChecked(true);
                    }
                    else
                    {
                        holder.lb.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(gujarati)
        {
            ans=setToGujarati(s);
            holder.nametext.setText(ans);
        }
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
                    currentDate = new SimpleDateFormat("dd-MM-yyyy");
                    currentTime = new SimpleDateFormat("HH:mm:ss");
                    final String t = String.valueOf(currentDate.format(Calendar.getInstance().getTime()));
                    final String d = String.valueOf(currentTime.format(Calendar.getInstance().getTime()));
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference wishlist =  db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("WishList");
                    wishlist.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            m=0;
                            for(DataSnapshot snapshot1:snapshot.getChildren())
                            {
                                if(snapshot1.child("name").getValue().toString().equals(name)&&snapshot1.child("parent").equals(FirebaseAuth.getInstance().getCurrentUser().getUid()+snapshot1.child("name").getValue().toString()))
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
                        wishModel wm = new wishModel(model.getName(), String.valueOf(price), t, d,FirebaseAuth.getInstance().getCurrentUser().getUid()+name, String.valueOf(1),FirebaseAuth.getInstance().getCurrentUser().getUid()+model.getName(), image);
                        wishlist.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+model.getName()).setValue(wm);
                    }

                }
                else
                {
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference wishList =  db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("WishList");
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
            }
        });

//        holder.ib1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                view.setVisibility(View.GONE);
//                holder.ib11.setVisibility(View.VISIBLE);
//                holder.ib12.setVisibility(View.VISIBLE);
//                holder.quantityInCart.setText("1");
//                holder.quantityInCart.setVisibility(View.VISIBLE);
//                k=0;
//                holder.ib11.setEnabled(false);
//                FirebaseAuth fa = FirebaseAuth.getInstance();
//                currentDate = new SimpleDateFormat("dd-MM-yyyy");
//                currentTime = new SimpleDateFormat("HH:mm:ss");
//                final String t = String.valueOf(currentDate.format(Calendar.getInstance().getTime()));
//                final String d = String.valueOf(currentTime.format(Calendar.getInstance().getTime()));
//                FirebaseDatabase db = FirebaseDatabase.getInstance();
//                DatabaseReference cart = db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Cart");
//                cart.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                            if (snapshot1.child("name").getValue().toString().equals(name)&&snapshot1.child("parent").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()+snapshot1.child("name").getValue().toString())) {
//                                k=1;
//                                String  s = snapshot1.child("totalquantity").getValue().toString();
//                                if(Integer.parseInt(s)==Integer.parseInt(quantity))
//                                {
//                                    Toast.makeText(view.getContext(),"Please order this many items first",Toast.LENGTH_SHORT).show();
//                                    break;
//                                }
//                                int q=Integer.parseInt(s)+1;
//                                String up=snapshot1.child("unitprice").getValue().toString();
//                                HashMap updateq=new HashMap();
//                                updateq.put("totalquantity",String.valueOf(q));
//                                updateq.put("totalprice",String.valueOf(Integer.parseInt(up)*q));
//                                cart.child(snapshot1.child("parent").getValue().toString()).updateChildren(updateq);
//                            }
//                        }
//                        if (k==0) {
//                            cartModel cm = new cartModel(model.getName(), price, t, d, "1", String.valueOf(price),FirebaseAuth.getInstance().getCurrentUser().getUid(),String.valueOf(1),FirebaseAuth.getInstance().getCurrentUser().getUid()+model.getName(),image);
//                            cart.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+model.getName()).setValue(cm);
//                        }
//                        holder.ib1.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
//        holder.ib11.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseDatabase db = FirebaseDatabase.getInstance();
//                DatabaseReference cart =  db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Cart");
//                cart.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                            String s = snapshot1.child("totalquantity").getValue().toString();
//                            int q = Integer.parseInt(s) + 1;
//                            if(q==0)
//                            {
//                                cart.child(snapshot1.child("parent").getValue().toString()).removeValue();
//                                break;
//
//                            }
//
//                            String up = snapshot1.child("unitprice").getValue().toString();
//                            if (snapshot1.child("name").getValue().toString().equals(holder.nametext.getText())&&snapshot1.child("uuid").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                                HashMap updateq = new HashMap();
//                                updateq.put("totalquantity",String.valueOf(q));
//                                updateq.put("totalprice",String.valueOf(Integer.parseInt(up) * q));
//                                cart.child(snapshot1.child("parent").getValue().toString()).updateChildren(updateq);
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//            }
//        });
//        holder.ib12.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseDatabase db = FirebaseDatabase.getInstance();
//                DatabaseReference cart =  db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Cart");
//                cart.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                            String s = snapshot1.child("totalquantity").getValue().toString();
//                            int q = Integer.parseInt(s) - 1;
//                            if(q==0)
//                            {
//                                cart.child(snapshot1.child("parent").getValue().toString()).removeValue();
//                                break;
//
//                            }
//
//                            String up = snapshot1.child("unitprice").getValue().toString();
//                            if (snapshot1.child("name").getValue().toString().equals(holder.nametext.getText())&&snapshot1.child("uuid").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                                HashMap updateq = new HashMap();
//                                updateq.put("totalquantity",String.valueOf(q));
//                                updateq.put("totalprice",String.valueOf(Integer.parseInt(up) * q));
//                                cart.child(snapshot1.child("parent").getValue().toString()).updateChildren(updateq);
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });

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
        //,ib1,ib11,ib12;
        TextView nametext,price,quantity,quantityInCart;
        CheckBox lb;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img1=itemView.findViewById(R.id.img1);
            nametext=itemView.findViewById(R.id.nametext);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);
            lb=itemView.findViewById(R.id.wish_Adder);
//            ib1=itemView.findViewById(R.id.add);
//            ib11=itemView.findViewById(R.id.addInside);
//            ib12=itemView.findViewById(R.id.subInside);
            //  quantityInCart=itemView.findViewById(R.id.quantityText);
            nametext.setOnCreateContextMenuListener(this::onCreateContextMenu);

        }
        public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo)
        {
            contextMenu.setHeaderTitle("Select Language");
            contextMenu.add(this.getAbsoluteAdapterPosition(),121,0,"Gujarati");
            contextMenu.add(this.getAbsoluteAdapterPosition(),122,0,"Hindi");
        }


    }
    public String setToGujarati(String s1)
    {

        String  n1=s1;
        TranslatorOptions translatorOptions=new TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH).setTargetLanguage(TranslateLanguage.GUJARATI).build();
        translator= Translation.getClient(translatorOptions);
        DownloadConditions downloadConditions=new DownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(downloadConditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                downloaded=true;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                downloaded=false;

            }
        });
        if(downloaded)
        {
            translator.translate(n1).addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    ret=s;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ret= e.toString();
                }
            });
        }
        return ret;
    }
    public String setToHindi(String s2)
    {
        String  n1=s2;
        TranslatorOptions translatorOptions=new TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH).setTargetLanguage(TranslateLanguage.HINDI).build();
        translator1= Translation.getClient(translatorOptions);
        DownloadConditions downloadConditions=new DownloadConditions.Builder().build();
        translator1.downloadModelIfNeeded(downloadConditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                downloaded1=true;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                downloaded1=false;

            }
        });
        if(downloaded1)
        {
            translator1.translate(n1).addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    ret1=s;

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ret1=e.toString();
                }
            });

        }
        return  ret1;

    }
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {

            case 121:
                gujarati=true;
                return true;
            case 122:
                hindi=true;
                return true;
            default:
                return false;

        }

    }
}

