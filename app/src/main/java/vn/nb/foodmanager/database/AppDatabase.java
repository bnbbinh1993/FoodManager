package vn.nb.foodmanager.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import vn.nb.foodmanager.model.Category;
import vn.nb.foodmanager.model.Food;


@Database(entities = {Food.class, Category.class}, version = 4)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract CategoryDao categoryDao();
    public abstract FoodDao foodDao();






    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "food")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


}
