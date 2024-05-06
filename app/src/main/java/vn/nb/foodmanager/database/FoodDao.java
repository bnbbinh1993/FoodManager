package vn.nb.foodmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import vn.nb.foodmanager.model.Food;
import vn.nb.foodmanager.model.FoodWithCategory;

@Dao
public interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Food food);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Food food);


    @Update
    void removed(Food food);
    @Transaction
    @Query("SELECT * FROM Food LEFT JOIN Category ON Food.category_Id = Category.categoryId WHERE Food.foodDeleted = 0 AND (Food.foodName LIKE '%' || :searchQuery || '%' OR Category.categoryName LIKE '%' || :searchQuery || '%')")
    List<FoodWithCategory> searchFoodByName(String searchQuery);


    @Transaction
    @Query("SELECT * FROM Food LEFT JOIN Category ON Food.category_Id = Category.categoryId WHERE Food.foodDeleted = 0 AND Category.categoryDeleted = 0")
    List<FoodWithCategory> getAllFoodWithCategory();

    @Transaction
    @Query("SELECT Food.*, Category.* FROM Food LEFT JOIN Category ON Food.category_Id = Category.categoryId AND Category.categoryDeleted = 0 WHERE Food.foodDeleted = 0 AND Food.foodPrice BETWEEN 100000 AND 200000 AND Food.foodServiceTime < :currentTimePlus15Minutes")
    List<FoodWithCategory> getFoodWithinPriceAndServiceTimeGreaterThanCurrentTime(long currentTimePlus15Minutes);


    @Transaction
    @Query("SELECT * FROM Food LEFT JOIN Category ON Food.category_Id = Category.categoryId WHERE Food.category_Id =:categoryId AND Food.foodDeleted = 0 AND Category.categoryDeleted = 0")
    List<FoodWithCategory> getAllFoodWithCategoryById(int categoryId);



}
