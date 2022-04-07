package liftup.tech.mixtureofstyles.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import liftup.tech.mixtureofstyles.Activity.ui.workshop.PhotosFragment;
import liftup.tech.mixtureofstyles.Activity.ui.workshop.VideosFragment;

public class WorkshopTabLayoutAdapter extends FragmentPagerAdapter {
    private static final String TAG = WorkshopTabLayoutAdapter.class.getSimpleName();
    private Context myContext;
    int totalTabs;
    String year;

    public WorkshopTabLayoutAdapter(Context context, FragmentManager fm, int totalTabs, String year) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.year = year;
        Log.d(TAG, "WorkshopTabLayoutAdapter: Year : "+year);
    }
    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new VideosFragment();
            case 1:
                return new PhotosFragment();
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
