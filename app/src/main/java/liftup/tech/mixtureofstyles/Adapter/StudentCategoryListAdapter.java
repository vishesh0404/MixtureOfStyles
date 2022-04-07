package liftup.tech.mixtureofstyles.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import liftup.tech.mixtureofstyles.Model.ShowcaseCategoryList;
import liftup.tech.mixtureofstyles.R;

import static liftup.tech.mixtureofstyles.Activity.AccountSettingsActivity.TAG;

public class StudentCategoryListAdapter extends RecyclerView.Adapter<StudentCategoryListAdapter.MyViewHolder>{
    Context context;
    private List<ShowcaseCategoryList> categoryLists;
    private int row_index;
    private OnItemSelectedLister onItemSelectedLister;
    public interface OnItemSelectedLister {
        void onItemSelect(String id);
    }
    public StudentCategoryListAdapter(Context applicationContext, List<ShowcaseCategoryList> categoryLists, OnItemSelectedLister onItemSelectedLister) {
        this.context = applicationContext;
        this.categoryLists = categoryLists;
        this.onItemSelectedLister=onItemSelectedLister;
    }
    @NonNull
    @Override
    public StudentCategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.showcase_category_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentCategoryListAdapter.MyViewHolder holder, int position) {
        holder.txtDanceCategory.setText(categoryLists.get(position).getDanceCategory());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                onItemSelectedLister.onItemSelect(categoryLists.get(position).getId());
                Log.d(TAG, "onClick: Row Index : "+row_index);
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.recycler_view_selecter);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#1D70EA"));
            holder.cardView.setBackground(unwrappedDrawable);
            holder.txtDanceCategory.setTextColor(Color.parseColor("#FFFFFFFF"));
        }
        else
        {
            holder.cardView.setBackgroundColor(Color.parseColor("#1D1D1D"));
            holder.txtDanceCategory.setTextColor(Color.parseColor("#66C7C7C7"));
        }
    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDanceCategory;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDanceCategory = itemView.findViewById(R.id.categoryName);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
