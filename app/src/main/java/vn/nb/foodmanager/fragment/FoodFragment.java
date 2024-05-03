package vn.nb.foodmanager.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.List;

import vn.nb.foodmanager.MainActivity;
import vn.nb.foodmanager.NewFoodActivity;
import vn.nb.foodmanager.R;
import vn.nb.foodmanager.adapter.FoodAdapter;
import vn.nb.foodmanager.database.AppDatabase;
import vn.nb.foodmanager.databinding.FragmentHomeBinding;
import vn.nb.foodmanager.interfaces.FoodOnListener;

import vn.nb.foodmanager.model.FoodWithCategory;


public class FoodFragment extends Fragment {


    private FragmentHomeBinding binding;
    private AppDatabase database;
    private Gson gson;

    public static FoodFragment newInstance() {
        return new FoodFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = AppDatabase.getInstance(requireActivity());
        gson = new Gson();
        initList();
        binding.btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), NewFoodActivity.class);
            intent.putExtra("food", gson.toJson(null));
            startActivity(intent);
        });

        binding.btnFilter.setOnClickListener(v -> showMenu());


        binding.edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newText = editable.toString();
                if (newText.isEmpty()) {
                    foodAdapter.addItem(database.foodDao().getAllFoodWithCategory());
                } else {
                    foodAdapter.addItem(database.foodDao().searchFoodByName(newText));
                }
            }
        });


    }

    private FoodAdapter foodAdapter;

    private void initList() {
        foodAdapter = new FoodAdapter();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(foodAdapter);
        foodAdapter.addItem(database.foodDao().getAllFoodWithCategory());

        foodAdapter.setFoodOnListener(new FoodOnListener() {
            @Override
            public void onItemClick(FoodWithCategory food, int p) {
                Intent intent = new Intent(requireActivity(), NewFoodActivity.class);
                intent.putExtra("food", gson.toJson(food));
                startActivity(intent);
            }

            @Override
            public void onLongClick(FoodWithCategory food, int p) {
                final String[] fonts = {
                        "Edit", "Delete"
                };


                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setItems(fonts, (dialog, which) -> {

                    switch (fonts[which]) {
                        case "Edit": {
                            Intent intent = new Intent(requireActivity(), NewFoodActivity.class);
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


    private void showMenu() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireActivity());
        dialog.setContentView(R.layout.layout_menu_bottom);
        dialog.setCancelable(true);
        dialog.show();

        View btnA = dialog.findViewById(R.id.tvSortA);
        View btnB = dialog.findViewById(R.id.tvSortB);

        View btnCancel = dialog.findViewById(R.id.cancel);

        if (btnA != null) {
            btnA.setOnClickListener(v -> {
                foodAdapter.addItem(database.foodDao().getFoodWithinPriceAndServiceTimeGreaterThanCurrentTime((System.currentTimeMillis() + (15 * 60 * 1000))));
                dialog.dismiss();
            });
        }
        if (btnB != null) {
            btnB.setOnClickListener(v -> {
                foodAdapter.addItem(database.foodDao().getAllFoodWithCategory());
                dialog.dismiss();
            });
        }
        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> {

                dialog.dismiss();
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        foodAdapter.addItem(database.foodDao().getAllFoodWithCategory());
    }
}