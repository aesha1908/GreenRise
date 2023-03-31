package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class pageAdapater extends FragmentStateAdapter {
    private String[] titles ={"Plants","Fertilizers","Flowers","Pots","Pebbles"};
    public pageAdapater(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position)
        {
            case 0:
                return  new PlantHomeFragment();
            case 1:
                return new FertilizerHomeFragment();
            case 2:
                return new FlowerFragmnet();
            case 3:
                return new PotHomeFragment();
            case 4:
                return new PebblesHomeFragment();
        }
        return new PlantHomeFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
