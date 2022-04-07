package liftup.tech.mixtureofstyles.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import liftup.tech.mixtureofstyles.Model.PhotosWorkshopList;
import liftup.tech.mixtureofstyles.Model.WorkshopList;
import liftup.tech.mixtureofstyles.R;

public class PhotosWorkshopListAdapter extends RecyclerView.Adapter<PhotosWorkshopListAdapter.MyViewHolder>{
    Context context;
    private List<PhotosWorkshopList> workshopLists;
    public PhotosWorkshopListAdapter(Context applicationContext, List<PhotosWorkshopList> workshopLists) {
        this.context = applicationContext;
        this.workshopLists = workshopLists;
    }
    @NonNull
    @Override
    public PhotosWorkshopListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_photos_workshop,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosWorkshopListAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(workshopLists.get(position).getThumb()).into(holder.imgproduct);
    }

    @Override
    public int getItemCount() {
        return workshopLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgproduct;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.image);
        }
    }
}
