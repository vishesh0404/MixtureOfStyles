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
import liftup.tech.mixtureofstyles.Model.DashbordInstructorList;
import liftup.tech.mixtureofstyles.R;

public class DashbordInstructorListAdaptor extends RecyclerView.Adapter<DashbordInstructorListAdaptor.MyViewHolder>{
    Context context;
    private List<DashbordInstructorList> styleLists;
    public DashbordInstructorListAdaptor(Context applicationContext, List<DashbordInstructorList> styleLists) {
        this.context = applicationContext;
        this.styleLists = styleLists;
    }
    @NonNull
    @Override
    public DashbordInstructorListAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_dashbord_showcase,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashbordInstructorListAdaptor.MyViewHolder holder, int position) {
        Glide.with(context).load(styleLists.get(position).getVthub()).into(holder.imgproduct);
        holder.txtName.setText(styleLists.get(position).getUname());
        holder.txtWeight.setText(styleLists.get(position).getStyle());
    }
    @Override
    public int getItemCount() {
        return styleLists.size();
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
                    String vUrl = styleLists.get(getAdapterPosition()).getVurl();
                    Intent intent = new Intent(context, ExoplayerActivity.class);
                    intent.putExtra("VURL", vUrl);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
