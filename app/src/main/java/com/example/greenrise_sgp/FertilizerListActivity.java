package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FertilizerListActivity extends AppCompatActivity {
    GridView recyclerView;
    FertilizerAdapter adapter;
    BottomNavigationView bnv;
    SearchView searchView;
    ArrayList<Fertilizer> searchlist;
    ImageView voice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_list);
        recyclerView = findViewById(R.id.gvfe);
        searchView = findViewById(R.id.searchfe);
        voice = findViewById(R.id.voicesearchfe);
        adapter = new FertilizerAdapter(this, DBQuery.fe_list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        bnv = findViewById(R.id.bottomnav);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchlist = new ArrayList<>();
                if (s.length() > 0) {
                    for (int i = 0; i < DBQuery.fe_list.size(); i++) {
                        if (DBQuery.fe_list.get(i).getName().toLowerCase().contains(s.toLowerCase())) {
                            Fertilizer plant = new Fertilizer();
                            plant.setName(DBQuery.fe_list.get(i).getName());
                            plant.setAbout(DBQuery.fe_list.get(i).getAbout());
                            plant.setPrice(DBQuery.fe_list.get(i).getPrice());
                            plant.setQuantity(DBQuery.fe_list.get(i).getQuantity());
                            searchlist.add(plant);
                        }
                    }
                    adapter = new FertilizerAdapter(FertilizerListActivity.this, searchlist);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchlist = new ArrayList<>();
                if (s.length() > 0) {
                    for (int i = 0; i < DBQuery.fe_list.size(); i++) {
                        if (DBQuery.fe_list.get(i).getName().toLowerCase().contains(s.toLowerCase())) {
                            Fertilizer flower = new Fertilizer();
                            flower.setName(DBQuery.fe_list.get(i).getName());
                            flower.setAbout(DBQuery.fe_list.get(i).getAbout());
                            flower.setPrice(DBQuery.fe_list.get(i).getPrice());
                            flower.setQuantity(DBQuery.fe_list.get(i).getQuantity());
                            searchlist.add(flower);
                        }
                    }
                    adapter = new FertilizerAdapter(FertilizerListActivity.this, searchlist);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openvoice();
            }
        });
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homei:
                        Intent intent = new Intent(FertilizerListActivity.this, SellerHomeActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.profilei:
                        Intent intent1 = new Intent(FertilizerListActivity.this, SellerInformationActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.carti:
                        Intent intent2 = new Intent(FertilizerListActivity.this, SellerOrdersActivity.class);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });
    }

    private void openvoice() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice Searching...");
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException e) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://market.android.com/details?id=APP_PACKAGE_NAME"));
            startActivity(browserIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            final ArrayList < String > matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String voice = matches.get(0);
            if (!matches.isEmpty())
            {
                searchlist = new ArrayList<>();
                for (int i = 0; i < DBQuery.fe_list.size(); i++) {
                    if (DBQuery.fe_list.get(i).getName().toLowerCase().contains(voice)) {
                        Fertilizer flower = new Fertilizer();
                        flower.setName(DBQuery.fe_list.get(i).getName());
                        flower.setAbout(DBQuery.fe_list.get(i).getAbout());
                        flower.setPrice(DBQuery.fe_list.get(i).getPrice());
                        flower.setQuantity(DBQuery.fe_list.get(i).getQuantity());
                        searchlist.add(flower);
                    }
                }
                adapter = new FertilizerAdapter(FertilizerListActivity.this, searchlist);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
        } else {
            Toast.makeText(FertilizerListActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
        }
    }
}
