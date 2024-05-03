package vn.nb.foodmanager.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.nb.foodmanager.fragment.FoodFragment;
import vn.nb.foodmanager.fragment.CategoryFragment;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fm;
        switch (position) {
            case 0:
                fm = FoodFragment.newInstance();
                break;
            case 1:
                fm = CategoryFragment.newInstance();
                break;


            default:
                fm = new Fragment();
                break;
        }
        return fm;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
