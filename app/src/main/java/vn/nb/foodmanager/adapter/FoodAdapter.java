package vn.nb.foodmanager.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import vn.nb.foodmanager.R;
import vn.nb.foodmanager.interfaces.FoodOnListener;
import vn.nb.foodmanager.model.Food;
import vn.nb.foodmanager.model.FoodWithCategory;
import vn.nb.foodmanager.utils.Utils;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.VH> {
    private List<FoodWithCategory> foodList = new ArrayList<>();

    private FoodOnListener foodOnListener;

    public void setFoodOnListener(FoodOnListener foodOnListener) {
        this.foodOnListener = foodOnListener;
    }

    @NonNull
    @Override
    public FoodAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.VH holder, int position) {
        FoodWithCategory food = foodList.get(holder.getAdapterPosition());
        holder.tvPrice.setText("Price: " + Utils.formatCurrency(food.food.getPrice()));

        holder.tvName.setText("Name: " + food.food.getName());


        holder.tvType.setText("Type: " + food.category.getName());
        holder.tvTime.setText("Time Order: " + Utils.formatTime(food.food.getServiceTime()));
        Glide.with(holder.itemView.getContext())
                .load(food.food.getImage())
                .error(R.drawable.imag_default)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> foodOnListener.onItemClick(food, holder.getAdapterPosition()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                foodOnListener.onLongClick(food, holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(List<FoodWithCategory> allFood) {
        this.foodList = allFood;
        notifyDataSetChanged();
    }

    public void removeItem(FoodWithCategory food, int p) {
        this.foodList.remove(food);
        notifyItemRemoved(p);
    }


    static class VH extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvPrice;
        private TextView tvTime;
        private TextView tvType;
        private ImageView image;


        public VH(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTime = itemView.findViewById(R.id.tvTime);
            image = itemView.findViewById(R.id.image);
        }
    }
}
