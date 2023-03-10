package com.example.greenrise_sgp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.List;

public class PlantAdapter extends BaseAdapter {
    Context context;
    List<Plant> list;
    ImageView newimg;
    Button change;
    int IMAGE_REQUEST_CODE = 7;
    Uri uri;
    CoordinatorLayout coordinatorLayout;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference = firebaseDatabase.getReference("Plant");

    public PlantAdapter(Context context, List<Plant> list) {
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
        if (view == null) {
            myvie = LayoutInflater.from(context).inflate(R.layout.plantentry, viewGroup, false);
        } else {
            myvie = view;
        }
        myvie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBQuery.g_selectedcatindex = i;
                Intent intent = new Intent(view.getContext(), PlantListActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        ImageView iv, up, del;
        TextView name, desc, price, quantity;
        iv = myvie.findViewById(R.id.plantimage);
        name = myvie.findViewById(R.id.plantname);
        desc = myvie.findViewById(R.id.plantabout);
        price = myvie.findViewById(R.id.plantprice);
        quantity = myvie.findViewById(R.id.plantquantity);
        up = myvie.findViewById(R.id.updatelogo);
        del = myvie.findViewById(R.id.deletelogo);
        coordinatorLayout = myvie.findViewById(R.id.rl);
        Glide.with(iv.getContext()).load(list.get(i).getImage()).into(iv);
        name.setText(list.get(i).getName());
        desc.setText(list.get(i).getAbout());
        price.setText(list.get(i).getPrice() + " Rs.");
        quantity.setText("Quantity: " + list.get(i).getQuantity());
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(iv.getContext())
                        .setContentHolder(new ViewHolder(R.layout.plantupdate))
                        .setExpanded(true, 2050)
                        .create();
                View v = dialogPlus.getHolderView();
                EditText etname = v.findViewById(R.id.upname);
                EditText etabout = v.findViewById(R.id.upabout);
                EditText etprice = v.findViewById(R.id.upprice);
                EditText etquantity = v.findViewById(R.id.upquantity);
                ImageView imgv = v.findViewById(R.id.previmage);
                Button chnge = v.findViewById(R.id.uploadupimg);
                Button upd = v.findViewById(R.id.update);
                etname.setText(list.get(i).getName());
                etabout.setText(list.get(i).getAbout());
                etprice.setText(String.valueOf(list.get(i).getPrice()));
                etquantity.setText(String.valueOf(list.get(i).getQuantity()));
                Glide.with(iv.getContext()).load(list.get(i).getImage()).into(imgv);
                dialogPlus.show();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Images");
                chnge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_REQUEST_CODE);
                        if(uri!=null)
                        {
                            StorageReference str = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(uri));
                            str.putFile(uri);
                            imgv.setImageURI(uri);
                        }
                    }
                });
                upd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot.child("key").getValue().toString().equals(list.get(i).getKey())) {
                                        HashMap map = new HashMap();
                                        map.put("name", etname.getText().toString());
                                        map.put("about", etabout.getText().toString());
                                        map.put("price", etprice.getText().toString());
                                        map.put("quantity", etquantity.getText().toString());
                                        reference.child(dataSnapshot.child("key").getValue().toString()).updateChildren(map);
                                        dialogPlus.dismiss();
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
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("key").getValue().toString().equals(list.get(i).getKey())) {
                                reference.child(dataSnapshot.child("key").getValue().toString()).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(name.getContext(), "Data Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(name.getContext(), "Error occured!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return myvie;
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
