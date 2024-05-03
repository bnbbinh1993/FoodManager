package vn.nb.foodmanager;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;


import vn.nb.foodmanager.adapter.PagerAdapter;
import vn.nb.foodmanager.adapter.TabAdapter;

import vn.nb.foodmanager.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initTab();
    }

    private TabAdapter tabAdapter = new TabAdapter();
    PagerAdapter adapter = new PagerAdapter(MainActivity.this);

    private void initTab() {
        binding.recyclerview.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        binding.recyclerview.setHasFixedSize(true);

        binding.recyclerview.setAdapter(tabAdapter);


        binding.viewPager.setOffscreenPageLimit(2);
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setSaveEnabled(false);
        binding.viewPager.setAdapter(adapter);
        tabAdapter.updateView(0);

        tabAdapter.setOnTabListener((dataTab, position) -> {
            binding.viewPager.setCurrentItem(position, false);
            tabAdapter.updateView(position);
        });


    }


}