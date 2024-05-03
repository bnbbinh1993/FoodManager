package vn.nb.foodmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import vn.nb.foodmanager.database.AppDatabase;
import vn.nb.foodmanager.databinding.ActivityNewFoodBinding;
import vn.nb.foodmanager.model.Category;
import vn.nb.foodmanager.model.Food;
import vn.nb.foodmanager.model.FoodWithCategory;
import vn.nb.foodmanager.utils.Utils;

@SuppressLint("MissingInflatedId")
public class NewFoodActivity extends AppCompatActivity {

    private Intent intent = new Intent();

    private AppDatabase database;

    private Category category;

    private ActivityNewFoodBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = AppDatabase.getInstance(this);
        Gson gson = new Gson();
        FoodWithCategory foodWithCategory = gson.fromJson(getIntent().getStringExtra("food"), FoodWithCategory.class);
        Food food;
        if (foodWithCategory != null) {
            food = foodWithCategory.food;
            category = foodWithCategory.category;

            Log.d("__log", "onCreate: "+food.getId());
            Log.d("__log", "onCreate: "+category.getId());
        } else {
            food = null;
        }


        if (food == null) {
            binding.edtName.setText("");
            binding.edtPrice.setText("");
            Calendar calendar = Calendar.getInstance();
            timeInMillis = calendar.getTimeInMillis();
            binding.tvType.setText("");
            binding.tvServiceTime.setText(Utils.formatTime(calendar.getTimeInMillis()));
            Glide.with(this)
                    .load(R.drawable.imag_default)
                    .into(binding.image);
        } else {
            binding.tvType.setText(category.getName());
            timeInMillis = food.getServiceTime();
            path = food.getImage();
            binding.edtName.setText(food.getName());
            binding.edtPrice.setText(String.valueOf(food.getPrice()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeInMillis);
            binding.tvServiceTime.setText(Utils.formatTime(calendar.getTimeInMillis()));
            Glide.with(this)
                    .load(path)
                    .into(binding.image);
        }


        binding.image.setOnClickListener(v -> {
            checkAndRequestPermissions();

        });

        binding.btnBack.setOnClickListener(v -> finish());
        binding.tvServiceTime.setOnClickListener(v -> showDateTimePickerDialog(binding.tvServiceTime));
        binding.tvType.setOnClickListener(v -> startForResult.launch(new Intent(NewFoodActivity.this, CategoryActivity.class)));

        binding.btnSave.setOnClickListener(v -> {
            String name = binding.edtName.getText().toString();
            String price = binding.edtPrice.getText().toString();

            if (name.isEmpty() || price.isEmpty() || path.isEmpty()) {
                Toast.makeText(this, "Please fill in all information", Toast.LENGTH_SHORT).show();
                return;
            }

            if (food == null) {
                Food fd = new Food();
                fd.setName(name);
                fd.setPrice(Integer.parseInt(price));
                fd.setImage(path);
                fd.setServiceTime(timeInMillis);
                fd.setCategoryId(category.getId());
                database.foodDao().insert(fd);
            } else {
                food.setName(name);
                food.setPrice(Integer.parseInt(price));
                food.setImage(path);
                food.setServiceTime(timeInMillis);
                food.setCategoryId(category.getId());
                database.foodDao().update(food);
            }


            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK, intent);
            finish();
        });


    }


    private void checkAndRequestPermissions() {
        String[] permissions = getPermissions();

        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            String[] permissionsArray = permissionsToRequest.toArray(new String[0]);
            requestPermissionsLauncher.launch(permissionsArray);
        } else {

            showBottomSheetImageChooser();
        }
    }

    @NonNull
    private static String[] getPermissions() {
        String[] newSDKPermissions = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_IMAGES};
        String[] oldSDKPermissions = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        String[] permissions;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = newSDKPermissions;
        } else {
            permissions = oldSDKPermissions;
        }
        return permissions;
    }

    private void showBottomSheetImageChooser() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.layout_bottom_photo);
        dialog.setCancelable(true);
        dialog.show();

        View btnCamera = dialog.findViewById(R.id.tv_camera);
        View btnGallery = dialog.findViewById(R.id.tv_gallery);
        View btnCancel = dialog.findViewById(R.id.cancel);

        Objects.requireNonNull(btnCamera).setOnClickListener(v -> {
            getPhoto("camera");
            dialog.dismiss();
        });

        Objects.requireNonNull(btnGallery).setOnClickListener(v -> {
            getPhoto("gallery");
            dialog.dismiss();
        });

        Objects.requireNonNull(btnCancel).setOnClickListener(v -> dialog.dismiss());
    }

    private void getPhoto(String from) {
        try {
            Intent intent = new Intent(this, CropImageActivity.class);
            intent.putExtra("maxSize", 600);
            intent.putExtra("maxQuality", 75);
            intent.putExtra("photoFrom", from);
            croppedStartForResult.launch(intent);
        } catch (Exception exp) {
            Toast.makeText(this, exp.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private String path = "";
    private ActivityResultLauncher<Intent> croppedStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        path = intent.getStringExtra("path");

                        Glide.with(this)
                                .load(path)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .centerCrop()
                                .into(binding.image);

                    }
                }
            }
    );

    private ActivityResultLauncher<String[]> requestPermissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                boolean allPermissionsGranted = true;
                for (Boolean isGranted : permissions.values()) {
                    if (!isGranted) {
                        allPermissionsGranted = false;
                        break;
                    }
                }
                if (allPermissionsGranted) {
                    showBottomSheetImageChooser();
                } else {
                    Toast.makeText(this, "Quyền truy cập camera và bộ nhớ bị từ chối!", Toast.LENGTH_SHORT).show();
                }
            });


    private long timeInMillis = 0;

    private void showDateTimePickerDialog(TextView v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    final int selectedYear = year1;
                    final int selectedMonth = monthOfYear;
                    final int selectedDay = dayOfMonth;
                    TimePickerDialog timePickerDialog = new TimePickerDialog(NewFoodActivity.this,
                            (view1, hourOfDay, minute1) -> {
                                Calendar selectedDateTime = Calendar.getInstance();
                                selectedDateTime.set(selectedYear, selectedMonth, selectedDay, hourOfDay, minute1);
                                timeInMillis = selectedDateTime.getTimeInMillis();
                                v.setText(Utils.formatTime(timeInMillis));

                            }, hour, minute, true);
                    timePickerDialog.show();
                }, year, month, day);
        datePickerDialog.show();
    }


    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                Gson gson = new Gson();
                Category ca = gson.fromJson(result.getData().getStringExtra("category"), Category.class);
                category = ca;
                binding.tvType.setText(category.getName());
            }

        }
    });
}