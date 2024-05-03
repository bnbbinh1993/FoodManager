package vn.nb.foodmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import vn.nb.foodmanager.adapter.CategoryAdapter;
import vn.nb.foodmanager.database.AppDatabase;
import vn.nb.foodmanager.databinding.ActivityCategoryBinding;
import vn.nb.foodmanager.interfaces.CategoryOnListener;
import vn.nb.foodmanager.model.Category;

public class CategoryActivity extends AppCompatActivity {
    private ActivityCategoryBinding binding;
    private AppDatabase database;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gson = new Gson();
        database = AppDatabase.getInstance(this);
        initList();

        binding.btnBack.setOnClickListener(v -> finish());
    }
    private CategoryAdapter categoryAdapter;
    private void initList() {
        categoryAdapter = new CategoryAdapter();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(categoryAdapter);
        categoryAdapter.addItem(database.categoryDao().getAllCategory());
        categoryAdapter.setCategoryOnListener(new CategoryOnListener() {
            @Override
            public void onItemClick(Category category, int p) {
              Intent intent = new Intent();
              intent.putExtra("category",gson.toJson(category));
              setResult(Activity.RESULT_OK,intent);
              finish();
            }

            @Override
            public void onLongClick(Category category, int p) {

            }
        });
    }
}