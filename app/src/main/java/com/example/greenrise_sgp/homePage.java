package com.example.greenrise_sgp;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.greenrise_sgp.databinding.ActivityHomePageBinding;

public class homePage extends AppCompatActivity {
    ActivityHomePageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        binding=ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bnv.setOnItemSelectedListener(item -> {
            switch(item.getItemId())
            {
                case R.id.chip1:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.chip2:
                    replaceFragment(new profileFragment());
                    break;
                case R.id.chip3:
                    replaceFragment(new BuyFragment());
                    break;
            }

            return true;
        });

    }
private void replaceFragment(Fragment fragment)
{
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.frameLayout,fragment);
    fragmentTransaction.commit();
}
}
