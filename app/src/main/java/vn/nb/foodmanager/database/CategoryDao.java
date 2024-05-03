package vn.nb.foodmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import vn.nb.foodmanager.model.Category;


@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Category category);


    @Query("SELECT * FROM category WHERE categoryDeleted = 0")
    List<Category> getAllCategory();

    @Update
    void update(Category category);

    @Update
    void removed(Category category);
}
