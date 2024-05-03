package vn.nb.foodmanager.adapter;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.nb.foodmanager.R;
import vn.nb.foodmanager.interfaces.OnTabListener;
import vn.nb.foodmanager.model.DataTab;

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.VH> {
    private List<DataTab> dataTabs = getDataTabs();

    private OnTabListener onTabListener;

    public void setOnTabListener(OnTabListener onTabListener) {
        this.onTabListener = onTabListener;
    }

    private List<DataTab> getDataTabs(){
        List<DataTab> list = new ArrayList<>();

        list.add(new DataTab("Food",R.drawable.home));
        list.add(new DataTab("Category",R.drawable.order));


        return list;
    }

    @NonNull
    @Override
    public TabAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bar, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TabAdapter.VH holder, int position) {

        DataTab dataTab = dataTabs.get(holder.getAdapterPosition());

        holder.itemView.setOnClickListener(v -> onTabListener.onItemClick(dataTab, holder.getAdapterPosition()));

        holder.nameRes.setText(dataTab.getNameRes());
        holder.iconRes.setImageResource(dataTab.getIconRes());


        if (dataTab.isSelected()) {
            holder.iconRes.setColorFilter(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.icon_bottom_selected),
                    PorterDuff.Mode.SRC_IN
            );
            holder.nameRes.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.icon_bottom_selected));
        } else {
            holder.iconRes.setColorFilter(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.icon_bottom),
                    PorterDuff.Mode.SRC_IN
            );
            holder.nameRes.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.icon_bottom));
        }




    }

    public void updateView(int p) {
        for (int i = 0;i<dataTabs.size();i++) {
            if (this.dataTabs.get(i).isSelected()) {
                this.dataTabs.get(i).setSelected(false);
                notifyItemChanged(i);
                break;
            }
        }
        this.dataTabs.get(p).setSelected(true);
        notifyItemChanged(p);

    }

    @Override
    public int getItemCount() {
        return dataTabs.size();
    }

    static class VH extends RecyclerView.ViewHolder {


        private TextView nameRes;
        private ImageView iconRes;

        public VH(@NonNull View itemView) {
            super(itemView);
            nameRes = itemView.findViewById(R.id.nameRes);
            iconRes = itemView.findViewById(R.id.iconRes);
        }
    }
}

