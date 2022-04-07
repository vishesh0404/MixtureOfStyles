package liftup.tech.mixtureofstyles.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import liftup.tech.mixtureofstyles.Activity.ui.showcase.DanceStyleFragment;
import liftup.tech.mixtureofstyles.Activity.ui.showcase.InststructorsFragment;
import liftup.tech.mixtureofstyles.Activity.ui.showcase.StudentShowcaseFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public TabLayoutAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DanceStyleFragment danceStyleFragment = new DanceStyleFragment();
                return danceStyleFragment;
            case 1:
                InststructorsFragment inststructorsFragment = new InststructorsFragment();
                return inststructorsFragment;
            case 2:
                StudentShowcaseFragment studentShowcaseFragment = new StudentShowcaseFragment();
                return studentShowcaseFragment;
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
