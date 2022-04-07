package liftup.tech.mixtureofstyles.Activity.ui.showcase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import liftup.tech.mixtureofstyles.Activity.DashbordActivity;
import liftup.tech.mixtureofstyles.Adapter.TabLayoutAdapter;
import liftup.tech.mixtureofstyles.R;

public class ShowcaseFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView imgSearch;
    SearchView searchView;
    LinearLayout searchLayout;
    TabLayoutAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Dance Styles"));
        tabLayout.addTab(tabLayout.newTab().setText("Instructors"));
        tabLayout.addTab(tabLayout.newTab().setText("Student Showcase"));
        imgSearch =  root.findViewById(R.id.search);
        searchView = root.findViewById(R.id.searchView);
        searchLayout = root.findViewById(R.id.searchLayout);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {
                Log.d(this.toString(), "onQueryTextSubmit: String : "+queryString);
                onUserSearched(queryString);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String queryString) {
                Log.d(this.toString(), "onQueryTextChange: String : "+queryString);
                onUserSearched(queryString);
                return false;
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchLayout.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.GONE);
            }
        });
        searchLayout.setVisibility(View.GONE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        adapter = new TabLayoutAdapter(getContext(),getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }

    private void onUserSearched(String searchedString) {
        if (viewPager != null &&adapter != null) {
            if (viewPager.getCurrentItem() == 0) {
                DanceStyleFragment fragment = (DanceStyleFragment) adapter.instantiateItem(viewPager, 0);
                fragment.onUserSearched(searchedString);
            } else if (viewPager.getCurrentItem() == 1) {
                InststructorsFragment fragment = (InststructorsFragment) adapter.instantiateItem(viewPager, 1);
                fragment.onUserSearched(searchedString);
            } else if (viewPager.getCurrentItem() == 2) {
                StudentShowcaseFragment fragment = (StudentShowcaseFragment) adapter.instantiateItem(viewPager, 2);
                fragment.onUserSearched(searchedString);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requireContext() != null) {
            int selectedMode = 0;
            if (requireActivity() instanceof DashbordActivity) {
                selectedMode = ((DashbordActivity) requireActivity()).selectedMode;
            }
            viewPager.setCurrentItem(selectedMode);
        }
    }
}