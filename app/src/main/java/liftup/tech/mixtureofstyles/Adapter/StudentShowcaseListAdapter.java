package liftup.tech.mixtureofstyles.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import liftup.tech.mixtureofstyles.Activity.ExoplayerActivity;
import liftup.tech.mixtureofstyles.Model.DanceStyleList;
import liftup.tech.mixtureofstyles.Model.ShowcaseCategoryList;
import liftup.tech.mixtureofstyles.R;

import static liftup.tech.mixtureofstyles.Activity.AccountSettingsActivity.TAG;

public class StudentShowcaseListAdapter extends RecyclerView.Adapter<StudentShowcaseListAdapter.MyViewHolder> implements Filterable {
    Context context;
    private List<DanceStyleList> styleLists;
    private List<DanceStyleList> styleListsFilter;

    public StudentShowcaseListAdapter(Context applicationContext, List<DanceStyleList> styleLists) {
        this.context = applicationContext;
        this.styleLists = styleLists;
        this.styleListsFilter = styleLists;
    }
    @NonNull
    @Override
    public StudentShowcaseListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_showcase,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentShowcaseListAdapter.MyViewHolder holder, int position) {
        DanceStyleList currentItem = styleListsFilter.get(position);
        Glide.with(context)
                .load(currentItem.getVthub())
                .into(holder.imgproduct);
        holder.txtName.setText(currentItem.getUname());
        holder.txtWeight.setText(currentItem.getStyle());
    }

    @Override
    public int getItemCount() {
        return styleListsFilter.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    styleListsFilter = styleLists;
                } else {
                    List<DanceStyleList> filteredList = new ArrayList<>();
                    for (DanceStyleList categoryName : styleLists) {
                        if (categoryName.getUname().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(categoryName);
                        }
                        styleListsFilter = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = styleListsFilter;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                styleListsFilter = (List<DanceStyleList>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgproduct;
        TextView txtName, txtWeight;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.image);
            txtName = itemView.findViewById(R.id.name);
            txtWeight = itemView.findViewById(R.id.style);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vUrl = styleListsFilter.get(getAdapterPosition()).getVurl();
                    Intent intent = new Intent(context, ExoplayerActivity.class);
                    intent.putExtra("VURL", vUrl);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
