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
import liftup.tech.mixtureofstyles.Model.DanceStyleVideoList;
import liftup.tech.mixtureofstyles.Model.WorkshopList;
import liftup.tech.mixtureofstyles.R;

public class WorkshopListAdapter extends RecyclerView.Adapter<WorkshopListAdapter.MyViewHolder>{
    Context context;
    private List<WorkshopList> workshopLists;
    public WorkshopListAdapter(Context applicationContext, List<WorkshopList> workshopLists) {
        this.context = applicationContext;
        this.workshopLists = workshopLists;
    }
    @NonNull
    @Override
    public WorkshopListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_workshops_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkshopListAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(workshopLists.get(position).getThumb()).into(holder.imgproduct);
        holder.txtName.setText(workshopLists.get(position).getIlogin());
        holder.txtStyle.setText(workshopLists.get(position).getIstyle());
        holder.txtTime.setText(" "+workshopLists.get(position).getTimeline()+" ");
    }
    @Override
    public int getItemCount() {
        return workshopLists.size();
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
                    String vUrl = workshopLists.get(getAdapterPosition()).getVideourl();
                    Intent intent = new Intent(context, ExoplayerActivity.class);
                    intent.putExtra("VURL", vUrl);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
