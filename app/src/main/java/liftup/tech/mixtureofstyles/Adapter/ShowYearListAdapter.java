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

import liftup.tech.mixtureofstyles.Model.DanceStyleList;
import liftup.tech.mixtureofstyles.Model.ShowYearList;
import liftup.tech.mixtureofstyles.Model.ShowcaseCategoryList;
import liftup.tech.mixtureofstyles.R;

import static liftup.tech.mixtureofstyles.Activity.AccountSettingsActivity.TAG;

public class ShowYearListAdapter extends RecyclerView.Adapter<ShowYearListAdapter.MyViewHolder>{
    Context context;
    private List<ShowYearList> showYearLists;
    private int row_index;
    private OnItemSelectedLister onItemSelectedLister;
    public interface OnItemSelectedLister {
        void onItemSelect(String id);
    }
    public ShowYearListAdapter(Context applicationContext, List<ShowYearList> showYearLists, OnItemSelectedLister onItemSelectedLister) {
        this.context = applicationContext;
        this.showYearLists = showYearLists;
        this.onItemSelectedLister = onItemSelectedLister;
    }
    @NonNull
    @Override
    public ShowYearListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.showcase_category_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowYearListAdapter.MyViewHolder holder, int position) {
        holder.txtYear.setText(showYearLists.get(position).getYear());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                onItemSelectedLister.onItemSelect(showYearLists.get(position).getYid());
                Log.d(TAG, "onClick: Row Index : "+row_index);
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.recycler_view_selecter);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#1D70EA"));
            holder.cardView.setBackground(unwrappedDrawable);
            holder.txtYear.setTextColor(Color.parseColor("#FFFFFFFF"));
        }
        else
        {
            holder.cardView.setBackgroundColor(Color.parseColor("#1D1D1D"));
            holder.txtYear.setTextColor(Color.parseColor("#66C7C7C7"));
        }
    }

    @Override
    public int getItemCount() {
        return showYearLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtYear;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtYear = itemView.findViewById(R.id.categoryName);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
