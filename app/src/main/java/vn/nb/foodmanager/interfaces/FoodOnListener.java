package vn.nb.foodmanager.interfaces;


import vn.nb.foodmanager.model.DataTab;
import vn.nb.foodmanager.model.Food;
import vn.nb.foodmanager.model.FoodWithCategory;

public interface FoodOnListener {
    void onItemClick(FoodWithCategory food, int p);
    void onLongClick(FoodWithCategory food, int p);
}
