package com.example.greenrise_sgp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.nl.translate.Translator;

public class FlowerFragmnet extends Fragment {


    private Translator translator;
    private boolean downloaded=false;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String email;

    private String mParam1;
    private String mParam2;
    RecyclerView rv;
    flower1Adapter adapter;
    static View view1;

    public FlowerFragmnet() {
        // Required empty public constructor
    }

    public static FlowerFragmnet newInstance(String param1, String param2) {
        FlowerFragmnet fragment = new FlowerFragmnet();
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
        View view= inflater.inflate(R.layout.fragment_flower_fragmnet, container, false);
        view1=view;
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        rv=(RecyclerView) view.findViewById(R.id.recView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Flowers"),Model.class)
                        .build();
        adapter=new flower1Adapter(options);
        rv.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        rv.setLayoutManager(gridLayoutManager);
        ImageView iv = view.findViewById(R.id.filter);
        registerForContextMenu(iv);
        return view;
    }
    public static void processSearch(String query) {
        RecyclerView rv;
        rv=(RecyclerView) view1.findViewById(R.id.recView);
        rv.setLayoutManager(new LinearLayoutManager(view1.getContext()));
        potAdapter adapter;
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Flowers").orderByChild("name").startAt(query).endAt(query+"\uf8ff"),Model.class)
                        .build();
        adapter=new potAdapter(options);
        adapter.startListening();
        rv.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view1.getContext(), 2);
        rv.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.filtercategorymenu,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.filter1:
                FirebaseRecyclerOptions<Model> options =
                        new FirebaseRecyclerOptions.Builder<Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Flowers").orderByChild("price"),Model.class)
                                .build();
                adapter=new flower1Adapter(options);
                adapter.startListening();
                rv.setAdapter(adapter);
                break;
            case R.id.filter2:
                FirebaseRecyclerOptions<Model> options1 =
                        new FirebaseRecyclerOptions.Builder<Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Flowers").orderByChild("price").startAt(0).endAt(50),Model.class)
                                .build();
                adapter=new flower1Adapter(options1);
                adapter.startListening();
                rv.setAdapter(adapter);
                break;
            case R.id.filter3:
                FirebaseRecyclerOptions<Model> options2 =
                        new FirebaseRecyclerOptions.Builder<Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Flowers").orderByChild("price").startAt(50).endAt(100),Model.class)
                                .build();
                adapter=new flower1Adapter(options2);
                adapter.startListening();
                rv.setAdapter(adapter);
                break;
            case R.id.filter4:
                FirebaseRecyclerOptions<Model> options3 =
                        new FirebaseRecyclerOptions.Builder<Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Flowers").orderByChild("price").startAt(100).endAt(150),Model.class)
                                .build();
                adapter=new flower1Adapter(options3);
                adapter.startListening();
                rv.setAdapter(adapter);
                break;
            case R.id.filter5:
                FirebaseRecyclerOptions<Model> options4 =
                        new FirebaseRecyclerOptions.Builder<Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Flowers").orderByChild("price").startAt(150).endAt(200),Model.class)
                                .build();
                adapter=new flower1Adapter(options4);
                adapter.startListening();
                rv.setAdapter(adapter);
                break;
            case R.id.filter6:
                FirebaseRecyclerOptions<Model> options5 =
                        new FirebaseRecyclerOptions.Builder<Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Flowers").orderByChild("price").startAt(200).endAt(250),Model.class)
                                .build();
                adapter=new flower1Adapter(options5);
                adapter.startListening();
                rv.setAdapter(adapter);
                break;
            case R.id.filter7:
                FirebaseRecyclerOptions<Model> options6 =
                        new FirebaseRecyclerOptions.Builder<Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Flowers").orderByChild("price").startAt(250).endAt(300),Model.class)
                                .build();
                adapter=new flower1Adapter(options6);
                adapter.startListening();
                rv.setAdapter(adapter);
                break;
            case R.id.filter8:
                FirebaseRecyclerOptions<Model> options7 =
                        new FirebaseRecyclerOptions.Builder<Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Flowers").orderByChild("price").startAt(300).endAt(350),Model.class)
                                .build();
                adapter=new flower1Adapter(options7);
                adapter.startListening();
                rv.setAdapter(adapter);
                break;
            case R.id.filter9:
                FirebaseRecyclerOptions<Model> options8 =
                        new FirebaseRecyclerOptions.Builder<Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Flowers").orderByChild("price").startAt(350),Model.class)
                                .build();
                adapter=new flower1Adapter(options8);
                adapter.startListening();
                rv.setAdapter(adapter);
                break;

        }
        return super.onContextItemSelected(item);

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