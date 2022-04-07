package liftup.tech.mixtureofstyles.Adapter;

import android.content.Context;
import android.content.Intent;
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

import liftup.tech.mixtureofstyles.Activity.DanceDetailsActivity;
import liftup.tech.mixtureofstyles.Activity.ExoplayerActivity;
import liftup.tech.mixtureofstyles.Model.DanceDetailsList;
import liftup.tech.mixtureofstyles.Model.DanceStyleVideoList;
import liftup.tech.mixtureofstyles.R;

public class DanceDetailsListAdapter extends RecyclerView.Adapter<DanceDetailsListAdapter.MyViewHolder>{
    Context context;
    private List<DanceDetailsList> detailsLists;
    public DanceDetailsListAdapter(Context applicationContext, List<DanceDetailsList> danceDetailsLists) {
        this.context = applicationContext;
        this.detailsLists = danceDetailsLists;
    }
    @NonNull
    @Override
    public DanceDetailsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dance_details_list,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DanceDetailsListAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(detailsLists.get(position).getThumb()).into(holder.image);
        holder.txtName.setText(detailsLists.get(position).getVname());
        holder.txtStyle.setText("Style : "+detailsLists.get(position).getStyle());
        holder.txtTime.setText(detailsLists.get(position).getTimeline());
    }

    @Override
    public int getItemCount() {
        return detailsLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView txtName, txtStyle, txtTime;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txtName = itemView.findViewById(R.id.name);
            txtStyle = itemView.findViewById(R.id.style);
            txtTime = itemView.findViewById(R.id.time);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vUrl = detailsLists.get(getAdapterPosition()).getVideourl();
                    Intent intent = new Intent(context, ExoplayerActivity.class);
                    intent.putExtra("VURL", vUrl);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    /*
                    String id = detailsLists.get(getAdapterPosition()).getId();
                    Intent intent = new Intent(context, DanceDetailsActivity.class);
                    intent.putExtra("ID", id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/

                }
            });
        }
    }
}
