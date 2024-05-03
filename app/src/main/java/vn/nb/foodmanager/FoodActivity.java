package vn.nb.foodmanager;

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

import vn.nb.foodmanager.adapter.FoodAdapter;
import vn.nb.foodmanager.database.AppDatabase;
import vn.nb.foodmanager.databinding.ActivityFoodBinding;
import vn.nb.foodmanager.interfaces.FoodOnListener;
import vn.nb.foodmanager.model.Category;
import vn.nb.foodmanager.model.FoodWithCategory;

public class FoodActivity extends AppCompatActivity {

    private ActivityFoodBinding binding;

    private AppDatabase database;
    private Gson gson;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = AppDatabase.getInstance(this);
        gson = new Gson();
        category = gson.fromJson(getIntent().getStringExtra("category"), Category.class);
        if (category == null) {
            return;
        }
        initList();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private FoodAdapter foodAdapter;

    private void initList() {
        foodAdapter = new FoodAdapter();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(foodAdapter);
        foodAdapter.addItem(database.foodDao().getAllFoodWithCategoryById(category.getId()));

        foodAdapter.setFoodOnListener(new FoodOnListener() {
            @Override
            public void onItemClick(FoodWithCategory food, int p) {
                Intent intent = new Intent(FoodActivity.this, NewFoodActivity.class);
                intent.putExtra("food", gson.toJson(food));
                startActivity(intent);
            }

            @Override
            public void onLongClick(FoodWithCategory food, int p) {
                final String[] fonts = {
                        "Edit", "Delete"
                };


                AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
                builder.setItems(fonts, (dialog, which) -> {

                    switch (fonts[which]) {
                        case "Edit": {
                            Intent intent = new Intent(FoodActivity.this, NewFoodActivity.class);
                            intent.putExtra("food", gson.toJson(food));
                            startActivity(intent);
                            break;
                        }
                        case "Delete": {
                            food.food.setDeleted(true);
                            database.foodDao().removed(food.food);
                            foodAdapter.removeItem(food, p);
                            break;
                        }
                    }

                });
                builder.show();
            }
        });

    }

}