package vn.nb.foodmanager;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import vn.nb.foodmanager.database.AppDatabase;
import vn.nb.foodmanager.model.Category;


public class NewCategoryActivity extends AppCompatActivity {
    private Intent intent = new Intent();

    private AppDatabase database;

    private vn.nb.foodmanager.databinding.ActivityNewCategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = vn.nb.foodmanager.databinding.ActivityNewCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = AppDatabase.getInstance(this);
        Gson gson = new Gson();
        Category category = gson.fromJson(getIntent().getStringExtra("category"), Category.class);

        if (category == null){
            binding.edtName.setText("");
            binding.edtDescription.setText("");
        }else {
            binding.edtName.setText(category.getName());
            binding.edtDescription.setText(category.getDescription());
        }

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSave.setOnClickListener(v -> {

            String name = binding.edtName.getText().toString();
            String des = binding.edtDescription.getText().toString();

            if (name.isEmpty() || des.isEmpty()){
                Toast.makeText(this, "Please fill in all information", Toast.LENGTH_SHORT).show();
                return;
            }

            if (category == null){
               Category cg = new Category();
               cg.setName(name);
               cg.setDescription(des);
               database.categoryDao().insert(cg);
            }else {
               category.setName(name);
               category.setDescription(des);
               database.categoryDao().update(category);
            }

            setResult(Activity.RESULT_OK,intent);
            finish();


        });



    }
}