package liftup.tech.mixtureofstyles.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.util.List;

import liftup.tech.mixtureofstyles.Activity.ExoplayerActivity;
import liftup.tech.mixtureofstyles.Model.Slider_Image_List;
import liftup.tech.mixtureofstyles.R;

import static liftup.tech.mixtureofstyles.Activity.AccountSettingsActivity.TAG;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{
    private List<Slider_Image_List> sliderItems;
    private ViewPager2 viewPager2;
    private Context Mcontext;
    public SliderAdapter(Context Mcontext, List<Slider_Image_List> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.Mcontext = Mcontext;
    }
    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slidingimages_layout, parent, false
                ) );
    }
    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));
    }
    @Override
    public int getItemCount() {
        return sliderItems.size();
    }
    class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView strName, strStyle, strTime;
        private CardView cardView;
        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            strName = itemView.findViewById(R.id.name);
            strStyle = itemView.findViewById(R.id.style);
            strTime = itemView.findViewById(R.id.time);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vUrl = sliderItems.get(getAdapterPosition()).getVideo_url();
                    Intent intent = new Intent(Mcontext, ExoplayerActivity.class);
                    intent.putExtra("VURL", vUrl);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Mcontext.startActivity(intent);
                }
            });
        }
        void setImage(Slider_Image_List sliderItems){
//use glide or picasso in case you get image from internet
            String url = sliderItems.getThumbnail();
            Log.d(TAG, "setImage: URL : "+url);
            Glide.with(Mcontext).load(url).into(imageView);
            strName.setText(sliderItems.getName());
            strStyle.setText(sliderItems.getVideo_style());
            strTime.setText(sliderItems.getVideo_time());
//            Glide.with(Mcontext).load(sliderItems.get(getAdapterPosition()).getThumbnail()).into(imageView);
//            imageView.setImageResource(sliderItems.getThumbnail());
        }
    }
}
