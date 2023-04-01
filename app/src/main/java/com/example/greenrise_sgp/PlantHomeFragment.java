package com.example.greenrise_sgp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PlantHomeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String email;

    private String mParam1;
    private String mParam2;
    RecyclerView rv;
    myadapter adapter;

    public PlantHomeFragment() {
        // Required empty public constructor
    }

    public static PlantHomeFragment newInstance(String param1, String param2) {
        PlantHomeFragment fragment = new PlantHomeFragment();
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
        View view= inflater.inflate(R.layout.fragment_plant_home, container, false);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        rv=(RecyclerView) view.findViewById(R.id.recView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Plant"),Model.class)
                        .build();
        adapter=new myadapter(options);
        rv.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        rv.setLayoutManager(gridLayoutManager);
        ImageView iv = view.findViewById(R.id.filter);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseRecyclerOptions<Model> options =
                        new FirebaseRecyclerOptions.Builder<Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Plant").orderByChild("price"),Model.class)
                                .build();
                adapter=new myadapter(options);
                adapter.startListening();
                rv.setAdapter(adapter);
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}