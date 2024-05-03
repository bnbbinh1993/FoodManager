package vn.nb.foodmanager.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class FoodWithCategory {
    @Embedded
    public Food food;

    @Relation(parentColumn = "category_Id", entityColumn = "categoryId")
    public Category category;
}

