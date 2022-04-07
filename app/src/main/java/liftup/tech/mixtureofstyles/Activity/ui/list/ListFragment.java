package liftup.tech.mixtureofstyles.Activity.ui.list;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import liftup.tech.mixtureofstyles.Activity.DanceDetailsActivity;
import liftup.tech.mixtureofstyles.Adapter.DashbordDanceStyleListAdapter;
import liftup.tech.mixtureofstyles.Adapter.ListAdapter;
import liftup.tech.mixtureofstyles.Model.DashbordDanceStyleList;
import liftup.tech.mixtureofstyles.Model.DashbordInstructorList;
import liftup.tech.mixtureofstyles.Model.ListViewModel;
import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

public class ListFragment extends Fragment {
    private static final String TAG = ListFragment.class.getSimpleName();
    //    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    private List<ListViewModel> listViewModels;
    SearchView searchView;
    ListAdapter listAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = root.findViewById(R.id.recyclerview);
        searchView = root.findViewById(R.id.searchView);
        listViewModels = new ArrayList<>();
        getVideosList();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {
                Log.d(TAG, "onQueryTextSubmit: String : "+queryString);
                listAdapter.getFilter().filter(queryString);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String queryString) {
                listAdapter.getFilter().filter(queryString);
                return false;
            }
        });
        return root;
    }
    private void getVideosList() {
//        showProgressDialogWithTitle("Loading categories...");
        String request_url = WebInterface.BASE_URL+ "work/list.php";
        Log.d("getVideosList","URL >>>>"+request_url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request_url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        hideProgressDialogWithTitle();
                        Log.d("getVideosList","Response >>>"+response);
//                        System.out.println(response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("record");
                            for (int p=0; p<jsonArray.length(); p++){
                                JSONObject jsonObject = jsonArray.getJSONObject(p);
                                ListViewModel category = new ListViewModel(
                                        jsonObject.getString("lid"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("uname"),
                                        jsonObject.getString("style"),
                                        jsonObject.getString("video"),
                                        jsonObject.getString("pimage"));
                                listViewModels.add(category);
//                                homeServices.add(jsonObject.getString(Config.SERVICE_NAME));
                                Log.d("getVideosList","Category List>>> "+listViewModels);
                            }
                            listAdapter = new ListAdapter(getContext(),listViewModels);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(listAdapter);

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
        //Creating a request queue
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }


}