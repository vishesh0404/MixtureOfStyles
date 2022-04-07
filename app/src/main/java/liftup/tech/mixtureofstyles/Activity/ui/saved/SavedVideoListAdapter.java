package liftup.tech.mixtureofstyles.Activity.ui.saved;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import liftup.tech.mixtureofstyles.Activity.ExoplayerActivity;
import liftup.tech.mixtureofstyles.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SavedVideoListAdapter extends RecyclerView.Adapter<SavedVideoListAdapter.MyViewHolder>{
    Context context;
    List<SavedVideoList> savedVideoLists;
    SavedVideoList savedVideoListModel;
    private int row_index;
    boolean isEnabled = false;
    boolean isSelectAll = false;
    List<Object> selectList = new ArrayList<>();
    private OnItemSelectedListner onItemSelectedListner;
    public interface OnItemSelectedListner {
        void onItemSelect(List<Object> id);
    }
    public SavedVideoListAdapter(Context applicationContext, List<SavedVideoList> savedVideoLists, OnItemSelectedListner onItemSelectedListner) {
        this.context = applicationContext;
        this.savedVideoLists = savedVideoLists;
        this.onItemSelectedListner = onItemSelectedListner;
        Log.d(TAG, "SavedVideoListAdapter: List : "+savedVideoLists);
    }
    @NonNull
    @Override
    public SavedVideoListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_saved_videos,parent,false);
//        savedVideoListModel = ViewModelProviders.of((FragmentActivity) context).get(SavedVideoList.class);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedVideoListAdapter.MyViewHolder holder, int position) {
        Glide.with(context)
                .load(savedVideoLists.get(position).getThumb())
                .into(holder.imgTumbnail);
        holder.txtName.setText(savedVideoLists.get(position).getVname());
        holder.txtStyle.setText(savedVideoLists.get(position).getIstyle());
        holder.txtTime.setText(savedVideoLists.get(position).getTimeline());
        holder.ivCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String isCheck;
                String data = null;
                if (isChecked){
                    isCheck = savedVideoLists.get(position).getId();
                    Log.d(TAG, "onCheckedChanged: Video ID : "+isCheck);
                    selectList.add(isCheck);
                    onItemSelectedListner.onItemSelect(selectList);
//                    Toast.makeText(context, "ID : "+isCheck, Toast.LENGTH_SHORT).show();
                }else {
                    isCheck = savedVideoLists.get(position).getId();
                    Log.d(TAG, "onCheckedChanged: Video ID : "+isCheck);
                }

            }
        });


    }

    private void ClickItem(MyViewHolder holder) {
        String s = savedVideoLists.get(holder.getAdapterPosition()).getId();
        if (holder.ivCheckBox.getVisibility() == View.GONE){
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            selectList.add(s);
        }else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            selectList.remove(s);
        }
        savedVideoListModel.setText(String.valueOf(selectList.size()));
    }

    @Override
    public int getItemCount() {
        return savedVideoLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTumbnail;
        TextView txtName, txtStyle, txtTime;
        CardView cardView;
        CheckBox ivCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTumbnail = itemView.findViewById(R.id.image);
            txtName = itemView.findViewById(R.id.name);
            txtStyle = itemView.findViewById(R.id.style);
            txtTime = itemView.findViewById(R.id.time);
            ivCheckBox = itemView.findViewById(R.id.check_box);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vUrl = savedVideoLists.get(getAdapterPosition()).getVideourl();
                    Intent intent = new Intent(context, ExoplayerActivity.class);
                    intent.putExtra("VURL", vUrl);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
