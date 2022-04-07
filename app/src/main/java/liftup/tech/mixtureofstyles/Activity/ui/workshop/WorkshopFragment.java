package liftup.tech.mixtureofstyles.Activity.ui.workshop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import liftup.tech.mixtureofstyles.Adapter.DanceStyleListAdapter;
import liftup.tech.mixtureofstyles.Adapter.ShowYearListAdapter;
import liftup.tech.mixtureofstyles.Adapter.TabLayoutAdapter;
import liftup.tech.mixtureofstyles.Adapter.WorkshopListAdapter;
import liftup.tech.mixtureofstyles.Adapter.WorkshopTabLayoutAdapter;
import liftup.tech.mixtureofstyles.Model.DanceStyleList;
import liftup.tech.mixtureofstyles.Model.ShowYearList;
import liftup.tech.mixtureofstyles.Model.ShowcaseCategoryList;
import liftup.tech.mixtureofstyles.Model.WorkshopList;
import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

import static liftup.tech.mixtureofstyles.Activity.CreateAccountActivity.TAG;

public class WorkshopFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    private List<ShowYearList> showYearLists;
    RecyclerView recyclerView;
    String year;
    WorkshopTabLayoutAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_workshop, container, false);
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);
        recyclerView = root.findViewById(R.id.recyclerview);

        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        showYearLists = new ArrayList<>();
        getYearList();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        adapter = new WorkshopTabLayoutAdapter(getContext(),getChildFragmentManager(), tabLayout.getTabCount(),year );
        viewPager.setAdapter(adapter);
    }

    private void getYearList() {
        String request_url = WebInterface.BASE_URL + "work/year.php";
        Log.d("URL","URL >>>>"+request_url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request_url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        hideProgressDialogWithTitle();
                        Log.d("Response","Response >>>"+response);
//                        System.out.println(response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("year");
                            for (int p=0; p<jsonArray.length(); p++){
                                JSONObject jsonObject = jsonArray.getJSONObject(p);
                                ShowYearList yearList = new ShowYearList(
                                        jsonObject.getString("yid"),
                                        jsonObject.getString("year")
                                );
                                showYearLists.add(p, yearList);
                                Log.d("Category","Category List>>> "+showYearLists);
                            }
                            ShowYearListAdapter showYearListAdapter = new ShowYearListAdapter(getContext(), showYearLists, new ShowYearListAdapter.OnItemSelectedLister() {
                                @Override
                                public void onItemSelect(String id) {
                                    Log.d(TAG, "onItemSelect: ID :"+id);
                                    year = id;

                                    Fragment currentFragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                                    if (currentFragment instanceof VideosFragment) {
                                        if (id == null){
                                            ((VideosFragment) currentFragment).getWorkShopVideos("All");
                                        }else {
                                            ((VideosFragment) currentFragment).getWorkShopVideos(id);
                                        }

                                    } else if (currentFragment instanceof PhotosFragment) {
                                        ((PhotosFragment) currentFragment).getPhotos(id);
                                    }

                                }
                            });
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
                            recyclerView.setAdapter(showYearListAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        hideProgressDialogWithTitle();
                        Log.e(TAG, "onErrorResponse: Error"+error );
                    }
                });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
}