package liftup.tech.mixtureofstyles.Activity.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import liftup.tech.mixtureofstyles.Activity.ContactUsActivity;
import liftup.tech.mixtureofstyles.Activity.DashbordActivity;
import liftup.tech.mixtureofstyles.Activity.PrivacyPolicyActivity;
import liftup.tech.mixtureofstyles.Activity.ProfileActivity;
import liftup.tech.mixtureofstyles.Activity.SelectPackageActivity;
import liftup.tech.mixtureofstyles.Activity.SelectPackageActivity1;
import liftup.tech.mixtureofstyles.Activity.SettingActivity;
import liftup.tech.mixtureofstyles.Activity.SignInActivity;
import liftup.tech.mixtureofstyles.Adapter.DashbordDanceStyleListAdapter;
import liftup.tech.mixtureofstyles.Adapter.DashbordInstructorListAdaptor;
import liftup.tech.mixtureofstyles.Adapter.DashbordStudentShowcaseLisAdapter;
import liftup.tech.mixtureofstyles.Adapter.SliderAdapter;
import liftup.tech.mixtureofstyles.Adapter.UniqueFeaturesListAdapter;
import liftup.tech.mixtureofstyles.Model.DashbordDanceStyleList;
import liftup.tech.mixtureofstyles.Model.DashbordInstructorList;
import liftup.tech.mixtureofstyles.Model.DashbordStudentShowcaseList;
import liftup.tech.mixtureofstyles.Model.Slider_Image_List;
import liftup.tech.mixtureofstyles.Model.UniqueFeaturesList;
import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.SessionManager;
import liftup.tech.mixtureofstyles.Utils.WebInterface;
import me.relex.circleindicator.CircleIndicator3;

import static android.content.Context.MODE_APPEND;
import static android.provider.Settings.NameValueTable.NAME;
import static liftup.tech.mixtureofstyles.Activity.CreateAccountActivity.TAG;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.EMAIL_ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.PAYED;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.PROFILE_PIC;

public class HomeFragment extends Fragment {
    DrawerLayout drawerLayout;
    Activity context;
    private ImageView options_menu, back;
    private List<Slider_Image_List> listItems;
    private ArrayList<Slider_Image_List> imageModelArrayList;
    private static ViewPager mPager;
    private TabLayout tabLayout;
    ListView mDrawerListView;
    SessionManager session;
    Slider_Image_List image_list;
    private boolean flag = false;
    private RecyclerView recyclerView, danceStyleRecyclerView, instructorsRecyclerView, studentShowcaseRecyclerView;
    private List<UniqueFeaturesList> featuresLists;
    private List<DashbordDanceStyleList> danceStyleLists;
    private List<DashbordInstructorList> instructorLists;
    private List<DashbordStudentShowcaseList> studentShowcaseLists;
    private ViewPager2 viewPager2;
    private CircleIndicator3 indicator;
    private Handler sliderHandler = new Handler();
    private String userId, userName, userEmail, userProfile, strPayed;
    TextView txtUserName, txtuserEmail, txtPremium;
    CircleImageView ivProfile;
    TextView txtViewAllDanceStyle, txtViewAllInstructor, txtViewAllStudentShowcase;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        // Session class instance
        session = new SessionManager(getContext());
        @SuppressLint("WrongConstant")
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs",MODE_APPEND);
        userId = sharedPreferences.getString(ID, " ");
        userName = sharedPreferences.getString(NAME, " ");
        userEmail = sharedPreferences.getString(EMAIL_ID, " ");
        strPayed = sharedPreferences.getString(PAYED," ");
        userProfile = sharedPreferences.getString(PROFILE_PIC, " ");
        Log.d(TAG,"Email ID >>>"+strPayed);

        drawerLayout = root.findViewById(R.id.drawerlayout);
        options_menu = root.findViewById(R.id.ic_menu);
        recyclerView = root.findViewById(R.id.recyclerview);
//        mDrawerListView = root.findViewById(R.id.list_slidermenu);
        danceStyleRecyclerView = root.findViewById(R.id.danceStyle);
        instructorsRecyclerView = root.findViewById(R.id.instructors);
        indicator = root.findViewById(R.id.indicator);
        studentShowcaseRecyclerView = root.findViewById(R.id.studentShowcase);
        txtViewAllDanceStyle = root.findViewById(R.id.viewAllDanceStyle);
        txtViewAllInstructor = root.findViewById(R.id.viewAllInstructor);
        txtViewAllStudentShowcase = root.findViewById(R.id.viewAllStudentShowcase);
        txtViewAllDanceStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requireActivity() instanceof DashbordActivity) {
                    ((DashbordActivity) context).selectedMode = 0;
                    ((DashbordActivity) context).loadFragment(R.id.navigation_showcase);
                }
            }
        });
        txtViewAllInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requireActivity() instanceof DashbordActivity) {
                    ((DashbordActivity) context).selectedMode = 1;
                    ((DashbordActivity) context).loadFragment(R.id.navigation_showcase);
                }
            }
        });
        txtViewAllStudentShowcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requireActivity() instanceof DashbordActivity) {
                    ((DashbordActivity) context).selectedMode = 2;
                    ((DashbordActivity) context).loadFragment(R.id.navigation_showcase);

                }
            }
        });
        options_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        viewPager2 = root.findViewById(R.id.viewPagerImageSlider);
//        mPager = root.findViewById(R.id.pager);
//        tabLayout = root.findViewById(R.id.my_tablayout);
        // Make a copy of the slides you'll be presenting.
        listItems = new ArrayList<>() ;
        getSlider();
//        root.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        NavigationView navigationView = (NavigationView)root.findViewById(R.id.nvView);
        View navigationHeader = navigationView.getHeaderView(0);
        back = navigationHeader.findViewById(R.id.back);
        ivProfile = navigationHeader.findViewById(R.id.profileImg);
        txtUserName = navigationHeader.findViewById(R.id.userName);
        txtuserEmail = navigationHeader.findViewById(R.id.userEmail);
        txtPremium = root.findViewById(R.id.premium);
        Glide.with(context).load(userProfile).into(ivProfile);
        txtUserName.setText(userName);
        txtuserEmail.setText(userEmail);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        txtPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), SelectPackageActivity.class);
                intent.putExtra("STATUS", strPayed);
                startActivity(intent);
                drawerLayout.closeDrawers();

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.terms) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                }
                if (menuItem.getItemId() == R.id.privecy) {
                    Intent intent = new Intent(getContext(), PrivacyPolicyActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                }
                if (menuItem.getItemId() == R.id.settings) {
                    Intent intent = new Intent(getContext(), SettingActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                }
                if (menuItem.getItemId() == R.id.support) {
                    Intent intent = new Intent(getContext(), ContactUsActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                }

                if (menuItem.getItemId() == R.id.logout) {
                    Intent intent=new Intent( getContext(), SignInActivity.class) ;
                    startActivity( intent );
                    session.logoutUser();
                }
                return true;
            }
        });

        featuresLists = new ArrayList<>();
        getUniqueFeatures();

        danceStyleLists = new ArrayList<>();
        getDanceStyleList();

        instructorLists = new ArrayList<>();
        getInstructorList();

        studentShowcaseLists = new ArrayList<>();
        getStudentList();

        return root;
    }

    private void getUniqueFeatures() {
        String request_url = WebInterface.BASE_URL + "unique_feture.php";
        Log.d("URL","URL >>>>"+request_url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request_url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        hideProgressDialogWithTitle();
                        Log.d("Response","Response >>>"+response);
//                        System.out.println(response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("record");
                            for (int p=0; p<jsonArray.length(); p++){
                                JSONObject jsonObject = jsonArray.getJSONObject(p);

                                UniqueFeaturesList savedVideoList = new UniqueFeaturesList(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("vname"),
                                        jsonObject.getString("vdescrip"),
                                        jsonObject.getString("uname"),
                                        jsonObject.getString("vthub"),
                                        jsonObject.getString("vurl"),
                                        jsonObject.getString("timeline"),
                                        jsonObject.getString("style")
                                );
                                featuresLists.add(p, savedVideoList);
                                Log.d("features","Features Lists List>>> "+featuresLists);
                            }
                            UniqueFeaturesListAdapter featuresListAdapter = new UniqueFeaturesListAdapter(getContext(),featuresLists);
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                            recyclerView.setAdapter(featuresListAdapter);

                        } catch (JSONException e) {
                            Toast.makeText(context, "No Data found", Toast.LENGTH_SHORT).show();
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

    private void getDanceStyleList() {
        String request_url = WebInterface.BASE_URL + "home/style.php";;
        Log.d("URL","URL >>>>"+request_url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // display response
                Log.d("Response", response.toString());
                try {
                    if (response.length() > 0) {
                        danceStyleLists.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            DashbordDanceStyleList danceStyleList = new DashbordDanceStyleList(jsonObject.getString("id"),
                                    jsonObject.getString("uname"),
                                    jsonObject.getString("vthub"),
                                    jsonObject.getString("vurl"),
                                    jsonObject.getString("timeline"),
                                    jsonObject.getString("style")
                            );
                            danceStyleLists.add(i, danceStyleList);
                        }
                        DashbordDanceStyleListAdapter danceStyleListAdapter = new DashbordDanceStyleListAdapter(getContext(),danceStyleLists);
                        danceStyleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                        danceStyleRecyclerView.setAdapter(danceStyleListAdapter);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"Server Not Responding..Please Try Again",Toast.LENGTH_SHORT).show();
                    Log.e("catchException",e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        //Creating a request queue
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }
    private void getInstructorList() {
        String request_url = WebInterface.BASE_URL + "home/instru.php";;
        Log.d("URL","URL >>>>"+request_url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // display response
                Log.d("Response", response.toString());
                try {
                    if (response.length() > 0) {
                        instructorLists.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            DashbordInstructorList instructorList = new DashbordInstructorList(jsonObject.getString("id"),
                                    jsonObject.getString("uname"),
                                    jsonObject.getString("vthub"),
                                    jsonObject.getString("vurl"),
                                    jsonObject.getString("timeline"),
                                    jsonObject.getString("style")
                            );
                            instructorLists.add(i, instructorList);
                        }
                        DashbordInstructorListAdaptor instructorListAdaptor = new DashbordInstructorListAdaptor(getContext(),instructorLists);
                        instructorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                        instructorsRecyclerView.setAdapter(instructorListAdaptor);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"Server Not Responding..Please Try Again",Toast.LENGTH_SHORT).show();
                    Log.e("catchException",e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        //Creating a request queue
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }
    private void getStudentList() {
        String request_url = WebInterface.BASE_URL + "home/style.php";;
        Log.d("URL","URL >>>>"+request_url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // display response
                Log.d("Response", response.toString());
                try {
                    if (response.length() > 0) {
                        studentShowcaseLists.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            DashbordStudentShowcaseList studentShowcaseList = new DashbordStudentShowcaseList(jsonObject.getString("id"),
                                    jsonObject.getString("uname"),
                                    jsonObject.getString("vthub"),
                                    jsonObject.getString("vurl"),
                                    jsonObject.getString("timeline"),
                                    jsonObject.getString("style")
                            );
                            studentShowcaseLists.add(i, studentShowcaseList);
                        }
                        DashbordStudentShowcaseLisAdapter studentShowcaseLisAdapter = new DashbordStudentShowcaseLisAdapter(getContext(),studentShowcaseLists);
                        studentShowcaseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                        studentShowcaseRecyclerView.setAdapter(studentShowcaseLisAdapter);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"Server Not Responding..Please Try Again",Toast.LENGTH_SHORT).show();
                    Log.e("catchException",e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        //Creating a request queue
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }
    private void getSlider() {
        String request_url = WebInterface.BASE_URL + "home/slider.php";;
        Log.d("URL","URL >>>>"+request_url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // display response
                Log.d("Response", response.toString());
                try {
                    if (response.length() > 0) {
                        listItems.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            image_list = new Slider_Image_List();
                            image_list.id = jsonObject.getString("id");
                            image_list.name = jsonObject.getString("uname");
                            image_list.thumbnail = jsonObject.getString("vthub");
                            image_list.video_url = jsonObject.getString("vurl");
                            image_list.video_time = jsonObject.getString("timeline");
                            image_list.video_style = jsonObject.getString("style");
                            listItems.add(i, image_list);
                        }

                        sliderAdapter = new SliderAdapter(getContext(),listItems,viewPager2);
                        viewPager2.setAdapter(sliderAdapter);
                        indicator.setViewPager(viewPager2);
                        //                        indicator.animatePageSelected(2);
                        viewPager2.setClipToPadding(false);
                        viewPager2.setClipChildren(false);
                        viewPager2.setOffscreenPageLimit(3);
                        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                            @Override
                            public void transformPage(@NonNull View page, float position) {
                                float r = 1-Math.abs(position);
                                page.setScaleY(0.85f + r * 0.15f);

                            }
                        });
                        viewPager2.setPageTransformer(compositePageTransformer);

                        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageSelected(int position) {
                                super.onPageSelected(position);
                                sliderHandler.removeCallbacks(sliderRunnable);
                                sliderHandler.postDelayed(sliderRunnable, 2000); // slide duration 2 seconds
                            }
                        });
                      /*  Slide_items_Pager_Adapter itemsPager_adapter = new Slide_items_Pager_Adapter(getContext(), listItems);
                        mPager.setAdapter(itemsPager_adapter);
                        // The_slide_timer
                        java.util.Timer timer = new java.util.Timer();
                        timer.scheduleAtFixedRate(new The_slide_timer(),2000,3000);
                        tabLayout.setupWithViewPager(mPager,true);*/
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"Server Not Responding..Please Try Again",Toast.LENGTH_SHORT).show();
                    Log.e("catchException",e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }

    private SliderAdapter sliderAdapter;

/*
    public class The_slide_timer extends TimerTask {
        @Override
        public void run() {
            if (isVisible()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPager.getCurrentItem()< listItems.size()-1) {
                            mPager.setCurrentItem(mPager.getCurrentItem()+1);
                        }
                        else
                            mPager.setCurrentItem(0);
                    }
                });
            }

        }
    }
*/
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (sliderAdapter != null) {
                if (viewPager2.getCurrentItem() == sliderAdapter.getItemCount() - 1) {
                    viewPager2.setCurrentItem(0);
                } else {
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                }
            }
        }
    };
    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);

    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}