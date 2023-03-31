package com.example.greenrise_sgp;

import android.content.ClipData;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.greenrise_sgp.databinding.ActivityHomePageBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String[] titles ={"Plants","Fertilizers","Flowers","Pots","Pebbles"};
    ActivityHomePageBinding binding;
    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_home,container,false);
        ImageSlider is=(ImageSlider) view.findViewById(R.id.imageSlider);
        TabLayout navTab=(TabLayout) view.findViewById(R.id.upnavigatortab);
        ViewPager2 vpager=(ViewPager2) view.findViewById(R.id.view_pager2);
        SearchView search =(SearchView) view.findViewById(R.id.searchView);
        search.clearFocus();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        pageAdapater pa;
        pa=new pageAdapater(getActivity());
        vpager.setAdapter(pa);
        new TabLayoutMediator(navTab,vpager,((tab, position) -> tab.setText(titles[position]))).attach();
        final List<SlideModel> images  = new ArrayList<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference imagesSlider=db.getReference().child("Plant");
        imagesSlider.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    images.add(new SlideModel(snapshot1.child("image").getValue().toString(),snapshot1.child("name").getValue().toString(), ScaleTypes.FIT));
                }
                is.setImageList(images, ScaleTypes.CENTER_INSIDE);
                is.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
                        Toast.makeText(view.getContext(),images.get(i).getTitle().toString(),Toast.LENGTH_LONG ).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }




}