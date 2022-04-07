package liftup.tech.mixtureofstyles.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import liftup.tech.mixtureofstyles.Activity.DanceDetailsActivity;
import liftup.tech.mixtureofstyles.Activity.ExoplayerActivity;
import liftup.tech.mixtureofstyles.Model.DanceStyleList;
import liftup.tech.mixtureofstyles.Model.ListViewModel;
import liftup.tech.mixtureofstyles.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder>implements Filterable {
    Context context;
    private List<ListViewModel> listViewModels;
    private List<ListViewModel> mDataListFiltered;
    public ListAdapter(Context applicationContext, List<ListViewModel> listViewModels) {
        this.context = applicationContext;
        this.listViewModels = listViewModels;
        this.mDataListFiltered = listViewModels;
    }
    @NonNull
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MyViewHolder holder, int position) {
        ListViewModel currentItem = mDataListFiltered.get(position);
        Glide.with(context).load(currentItem.getPimage()).into(holder.imgproduct);
        holder.txtName.setText(currentItem.getName());
        holder.txtStyle.setText("Styles : "+currentItem.getStyle());
        holder.txtVideos.setText(currentItem.getVideo()+" Videos");
    }

    @Override
    public int getItemCount() {
        return mDataListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    mDataListFiltered = listViewModels;
                } else {
                    List<ListViewModel> filteredList = new ArrayList<>();
                    for (ListViewModel categoryName : listViewModels) {
                        if (categoryName.getName().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(categoryName);
                        }
                        mDataListFiltered = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = mDataListFiltered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataListFiltered = (List<ListViewModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgproduct;
        TextView txtName,txtStyle, txtVideos;
        ImageView next;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.image);
            txtName = itemView.findViewById(R.id.name);
            txtStyle = itemView.findViewById(R.id.style);
            txtVideos = itemView.findViewById(R.id.videos);
            next = itemView.findViewById(R.id.next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = mDataListFiltered.get(getAdapterPosition()).getLid();
//                    String videoURL = listViewModels.get(getAdapterPosition()).getVideo();
                    Intent intent = new Intent(context, DanceDetailsActivity.class);
                    intent.putExtra("ID", id);
//                    intent.putExtra("VIDEO_URL", videoURL);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
        }
    }
}
