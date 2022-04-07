package liftup.tech.mixtureofstyles.Activity.ui.showcase;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import liftup.tech.mixtureofstyles.Adapter.DanceStyleListAdapter;
import liftup.tech.mixtureofstyles.Adapter.InstructorVideoListAdapter;
import liftup.tech.mixtureofstyles.Adapter.InstructorsCategoryListAdapter;
import liftup.tech.mixtureofstyles.Adapter.StudentShowcaseListAdapter;
import liftup.tech.mixtureofstyles.Model.DanceStyleList;
import liftup.tech.mixtureofstyles.Model.ShowcaseCategoryList;
import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.BaseFragment;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

import static liftup.tech.mixtureofstyles.Activity.CreateAccountActivity.TAG;

public class InststructorsFragment extends BaseFragment {
    RecyclerView recyclerView, recyclerView1;
    private List<DanceStyleList> styleLists;
    private List<ShowcaseCategoryList> categoryLists;
    InstructorVideoListAdapter instructorVideoListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_inststructors, container, false);

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView1 = root.findViewById(R.id.recyclerview1);
        categoryLists = new ArrayList<>();
        getCategoryName();
        getInstructorVideos("www");
        styleLists = new ArrayList<>();

        return root;
    }

    public void onUserSearched(String searchText) {
        instructorVideoListAdapter.getFilter().filter(searchText);

    }

    private void getCategoryName() {
        String request_url = WebInterface.BASE_URL +"user_list.php";
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
                                ShowcaseCategoryList categoryList = new ShowcaseCategoryList();
                                categoryList.id = jsonObject.getString("id");
                                categoryList.danceCategory = jsonObject.getString("name");

                                categoryLists.add(p, categoryList);
                                Log.d("Category","Category List>>> "+categoryLists);
                            }
                            InstructorsCategoryListAdapter categoryListAdapter = new InstructorsCategoryListAdapter(getContext(), categoryLists, new InstructorsCategoryListAdapter.OnItemSelectedLister() {
                                @Override
                                public void onItemSelect(String id) {
                                    Log.d(TAG, "onItemSelect: ID : "+id);
                                    styleLists.clear();
                                    getInstructorVideos(id);

                                }
                            });
                            recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
                            recyclerView1.setAdapter(categoryListAdapter);

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

    private void getInstructorVideos(String id) {
        styleLists = new ArrayList<>();
        instructorVideoListAdapter = new InstructorVideoListAdapter(getContext(),styleLists);
        recyclerView.setAdapter(instructorVideoListAdapter);

        showProgressDialog("Please wait ...");
        String request_url = WebInterface.BASE_URL + "inst_style.php";
        Log.d("URL","URL >>>>"+request_url);
        Map<String, String> params = new HashMap();
        params.put("style", id);
        JSONObject parameters = new JSONObject(params);
        Log.d("TAG", "getInstructorVideos: parameters: " + parameters);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, request_url,parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismissProgressDialog();
                        Log.d("Response","Response >>>"+response);
//                        System.out.println(response);
                        try {
                            JSONArray jsonArray = new JSONArray();
                            jsonArray = response.getJSONArray("record");
                            for (int p=0; p<jsonArray.length(); p++){
                                JSONObject jsonObject = jsonArray.getJSONObject(p);
                                DanceStyleList danceStyleLists = new DanceStyleList(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("uname"),
                                        jsonObject.getString("vthub"),
                                        jsonObject.getString("vurl"),
                                        jsonObject.getString("timeline"),
                                        jsonObject.getString("style")
                                );
                                styleLists.add(p, danceStyleLists);
//                                homeServices.add(jsonObject.getString(Config.SERVICE_NAME));
                                Log.d("Category","Category List>>> "+styleLists);
                            }
                            instructorVideoListAdapter = new InstructorVideoListAdapter(getContext(),styleLists);
                            recyclerView.setAdapter(instructorVideoListAdapter);

                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "No Video Found...", Toast.LENGTH_SHORT).show();
                            dismissProgressDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressDialog();
                        Log.e(TAG, "onErrorResponse: Error"+error );
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

}