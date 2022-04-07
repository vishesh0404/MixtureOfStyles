package liftup.tech.mixtureofstyles.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import liftup.tech.mixtureofstyles.Model.Slider_Image_List;
import liftup.tech.mixtureofstyles.R;

public class Slide_items_Pager_Adapter extends PagerAdapter {
    private Context Mcontext;
    private List<Slider_Image_List> theSlideItemsModelClassList;


    public Slide_items_Pager_Adapter(Context Mcontext, List<Slider_Image_List> theSlideItemsModelClassList) {
        this.Mcontext = Mcontext;
        this.theSlideItemsModelClassList = theSlideItemsModelClassList;
    }
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sliderLayout = inflater.inflate(R.layout.slidingimages_layout,null);

        ImageView image = sliderLayout.findViewById(R.id.image);
        TextView caption_title = sliderLayout.findViewById(R.id.name);
        TextView dance_style = sliderLayout.findViewById(R.id.style);
        TextView dance_time = sliderLayout.findViewById(R.id.time);
        ImageView play = sliderLayout.findViewById(R.id.play);
        Glide.with(Mcontext).load(theSlideItemsModelClassList.get(position).getThumbnail()).into(image);
        caption_title.setText(theSlideItemsModelClassList.get(position).getName());
        dance_style.setText(theSlideItemsModelClassList.get(position).getVideo_style());
        dance_time.setText(theSlideItemsModelClassList.get(position).getVideo_time());
       /* play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dance_time.setText(theSlideItemsModelClassList.get(position).video_url());

            }
        });*/
        container.addView(sliderLayout);
        return sliderLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return theSlideItemsModelClassList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
