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

import liftup.tech.mixtureofstyles.Activity.ExoplayerActivity;
import liftup.tech.mixtureofstyles.Model.DanceStyleList;
import liftup.tech.mixtureofstyles.Model.DanceStyleVideoList;
import liftup.tech.mixtureofstyles.R;

public class DanceStyleVideoListAdapter extends RecyclerView.Adapter<DanceStyleVideoListAdapter.MyViewHolder>{
    Context context;
    private List<DanceStyleVideoList> styleLists;
    public DanceStyleVideoListAdapter(Context applicationContext, List<DanceStyleVideoList> styleLists) {
        this.context = applicationContext;
        this.styleLists = styleLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_dance_style,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(styleLists.get(position).getThumb()).into(holder.imgproduct);
        holder.txtName.setText(styleLists.get(position).getVname());
        holder.txtStyle.setText(styleLists.get(position).getStyle());
        holder.txtTime.setText(styleLists.get(position).getTimeline());
    }

    @Override
    public int getItemCount() {
        return styleLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgproduct;
        TextView txtName, txtStyle, txtTime;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.image);
            txtName = itemView.findViewById(R.id.name);
            txtStyle = itemView.findViewById(R.id.style);
            txtTime = itemView.findViewById(R.id.time);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vUrl = styleLists.get(getAdapterPosition()).getVideourl();
                    Intent intent = new Intent(context, ExoplayerActivity.class);
                    intent.putExtra("VURL", vUrl);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
