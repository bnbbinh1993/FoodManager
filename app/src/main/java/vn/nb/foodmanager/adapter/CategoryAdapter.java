package vn.nb.foodmanager.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.nb.foodmanager.R;
import vn.nb.foodmanager.interfaces.CategoryOnListener;
import vn.nb.foodmanager.model.Category;
import vn.nb.foodmanager.model.Food;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {
    private List<Category> categoryList = new ArrayList<>();

    private CategoryOnListener categoryOnListener;
    public void setCategoryOnListener(CategoryOnListener  categoryOnListener){
        this.categoryOnListener = categoryOnListener;
    }

    @NonNull
    @Override
    public CategoryAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.VH holder, int position) {

        Category category = categoryList.get(holder.getAdapterPosition());
        holder.name.setText(category.getName());
        holder.des.setText(category.getDescription());
        holder.itemView.setOnLongClickListener(v -> {
            categoryOnListener.onLongClick(category,holder.getAdapterPosition());
            return false;
        });
        holder.itemView.setOnClickListener(v -> categoryOnListener.onItemClick(category,holder.getAdapterPosition()));

    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public void removeItem(Category category, int p) {
        this.categoryList.remove(category);
        notifyItemRemoved(p);
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    static class VH extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView des;

        public VH(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvName);
            des = itemView.findViewById(R.id.tvDes);
        }
    }
}
