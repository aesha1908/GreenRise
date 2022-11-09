package com.example.greenrise_sgp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyInfoFragment extends Fragment {

    TextView name,email,phone;


    public MyInfoFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseDatabase database;
        DatabaseReference reference;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        name = view.findViewById(R.id.Username);
        email = view.findViewById(R.id.Email);
        phone = view.findViewById(R.id.Contact);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        name.setText(CurrentUser.currentUser.getName());
        email.setText(CurrentUser.currentUser.getEmail());
        phone.setText(CurrentUser.currentUser.getPhone());
        return view;
    }
}