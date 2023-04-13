package com.example.greenrise_sgp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;


public class profileFragment extends Fragment {
    Button switchtoseller,orderuser,wishlistuser,infouser,logout,language,pass;
    TextView name;
    ImageView uimage;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        loadLocale();
        switchtoseller=view.findViewById(R.id.Usts);
        orderuser = view.findViewById(R.id.Uorders);
        wishlistuser = view.findViewById(R.id.Uwish);
        infouser = view.findViewById(R.id.UInfo);
        logout = view.findViewById(R.id.Ulog);
        name = view.findViewById(R.id.Uname);
<<<<<<< HEAD
        language = view.findViewById(R.id.languagechanger);
=======
        language=view.findViewById(R.id.languagechanger);
>>>>>>> da1e42bccf047de3392473d2b4f385b0d8d82736
        pass = view.findViewById(R.id.Uchpw);
        uimage = view.findViewById(R.id.imageView);
        mAuth = FirebaseAuth.getInstance();
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForgetPassUser.class);
                startActivity(intent);
            }
        });
        switchtoseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SellerLoginActivity.class);
                startActivity(intent);
            }
        });
        orderuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new OrderFragment()).addToBackStack(null).commit();
            }
        });
        infouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.frameLayout,new MyInfoFragment()).
                        addToBackStack(null).commit();
            }
        });
        wishlistuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new WishFragment()).addToBackStack(null).commit();
            }
        });
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changelanguage();
            }
        });
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference cart =  db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Cart");
        DatabaseReference wish =  db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Wishlist");
        DatabaseReference order =  db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Order");
        DatabaseReference user=db.getReference("Users");
        String uid = mAuth.getCurrentUser().getUid();
        user.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User users = snapshot.getValue(User.class);
                if (users != null) {
                    String zname = users.name;
                    name.setText("Welcome " + zname + " !");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                            cart.child(snapshot1.child("parent").getValue().toString()).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                wish.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                            wish.child(snapshot1.child("parent").getValue().toString()).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                order.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                            order.child(snapshot1.child("parent").getValue().toString()).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(getActivity(),UserLoginActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void changelanguage() {
        final String languages[]={"English","Guajarti","Bengali"};
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(getContext());
        mBuilder.setTitle("Choose language");
        mBuilder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0)
                {
                    setLocale("");
                    getActivity().recreate();
                }
                else if(i==1)
                {
                    setLocale("gu");
                    getActivity().recreate();
                }
                else if(i==2)
                {
                    setLocale("hi");
                    getActivity().recreate();
                }


            }
        });
        mBuilder.create();
        mBuilder.show();

    }

    private void setLocale(String s) {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getActivity().getBaseContext().getResources().updateConfiguration(configuration,getActivity().getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("app_lang",s);
        editor.apply();
    }
    private  void loadLocale()
    {
        SharedPreferences preferences = getActivity().getSharedPreferences("Settings",Context.MODE_PRIVATE);
        String language=preferences.getString("app_lang","");
        setLocale(language);
    }
}