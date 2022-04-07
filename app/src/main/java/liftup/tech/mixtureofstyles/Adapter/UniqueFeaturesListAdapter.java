package liftup.tech.mixtureofstyles.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import liftup.tech.mixtureofstyles.Model.DanceDetailsList;
import liftup.tech.mixtureofstyles.Model.UniqueFeaturesList;
import liftup.tech.mixtureofstyles.R;

public class UniqueFeaturesListAdapter extends RecyclerView.Adapter<UniqueFeaturesListAdapter.MyViewHolder>{
    Context context;
    private List<UniqueFeaturesList> featuresLists;
    public UniqueFeaturesListAdapter(Context applicationContext, List<UniqueFeaturesList> featuresLists) {
        this.context = applicationContext;
        this.featuresLists = featuresLists;
    }
    @NonNull
    @Override
    public UniqueFeaturesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_unique_features,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniqueFeaturesListAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(featuresLists.get(position).getVthub()).into(holder.thumbnail);
        holder.txtFName.setText(featuresLists.get(position).getUname());
        holder.txtFDescription.setText(featuresLists.get(position).getVdescrip());
    }

    @Override
    public int getItemCount() {
        return featuresLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView txtFName, txtFDescription;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.unique_tumbnail);
            txtFName = itemView.findViewById(R.id.feature_name);
            txtFDescription = itemView.findViewById(R.id.feature_description);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
