package vn.nb.foodmanager.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.Objects;

import vn.nb.foodmanager.FoodActivity;
import vn.nb.foodmanager.MainActivity;
import vn.nb.foodmanager.NewCategoryActivity;
import vn.nb.foodmanager.NewFoodActivity;
import vn.nb.foodmanager.R;
import vn.nb.foodmanager.adapter.CategoryAdapter;
import vn.nb.foodmanager.database.AppDatabase;
import vn.nb.foodmanager.databinding.FragmentCategoryBinding;
import vn.nb.foodmanager.interfaces.CategoryOnListener;

import vn.nb.foodmanager.model.Category;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;
    private AppDatabase database;
    private Gson gson;


    public static CategoryFragment newInstance() {

        return new CategoryFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gson = new Gson();
        database = AppDatabase.getInstance(requireActivity());
        initList();

        binding.btnCreate.setOnClickListener(v -> startForResult.launch(new Intent(requireActivity(), NewCategoryActivity.class)));


    }

    private CategoryAdapter categoryAdapter;

    private void initList() {
        categoryAdapter = new CategoryAdapter();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(categoryAdapter);
        categoryAdapter.addItem(database.categoryDao().getAllCategory());
        categoryAdapter.setCategoryOnListener(new CategoryOnListener() {
            @Override
            public void onItemClick(Category category, int p) {
                Intent intent = new Intent(requireActivity(), FoodActivity.class);
                intent.putExtra("category", gson.toJson(category));
                startForResult.launch(intent);
            }

            @Override
            public void onLongClick(Category category, int p) {
                final String[] fonts = {
                        "Edit", "Delete"
                };


                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setItems(fonts, (dialog, which) -> {

                    switch (fonts[which]) {
                        case "Edit": {
                            Intent intent = new Intent(requireActivity(), NewCategoryActivity.class);
                            intent.putExtra("category", gson.toJson(category));
                            startActivity(intent);
                            break;
                        }
                        case "Delete": {
                            category.setDeleted(true);
                            database.categoryDao().removed(category);
                            categoryAdapter.removeItem(category, p);
                            break;
                        }
                    }

                });
                builder.show();
            }
        });
    }

    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            categoryAdapter.addItem(database.categoryDao().getAllCategory());



        }
    });

}