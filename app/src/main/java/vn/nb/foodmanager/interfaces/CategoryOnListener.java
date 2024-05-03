package vn.nb.foodmanager.interfaces;


import vn.nb.foodmanager.model.Category;
import vn.nb.foodmanager.model.Food;

public interface CategoryOnListener {
    void onItemClick(Category category, int p);
    void onLongClick(Category category, int p);
}
