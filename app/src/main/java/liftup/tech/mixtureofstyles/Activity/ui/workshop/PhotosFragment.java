package liftup.tech.mixtureofstyles.Activity.ui.workshop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

import liftup.tech.mixtureofstyles.Adapter.PhotosWorkshopListAdapter;
import liftup.tech.mixtureofstyles.Adapter.WorkshopListAdapter;
import liftup.tech.mixtureofstyles.Model.PhotosWorkshopList;
import liftup.tech.mixtureofstyles.Model.WorkshopList;
import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.BaseFragment;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

import static liftup.tech.mixtureofstyles.Activity.CreateAccountActivity.TAG;

public class PhotosFragment extends BaseFragment {
    RecyclerView recyclerView;
    private List<PhotosWorkshopList> photosWorkshopLists;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_photos, container, false);
        recyclerView = root.findViewById(R.id.recyclerview);
        photosWorkshopLists = new ArrayList<>();

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photosWorkshopLists = new ArrayList<>();
        getPhotos("All");
    }

    public void getPhotos(String year) {
        showProgressDialog("Please wait ...");
        photosWorkshopLists = new ArrayList<>();
        PhotosWorkshopListAdapter workshopListAdapter = new PhotosWorkshopListAdapter(getContext(),photosWorkshopLists);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(workshopListAdapter);

//        Toast.makeText(requireContext(), year, Toast.LENGTH_LONG).show();
        String request_url = WebInterface.BASE_URL + "work/videoimage.php";
        Log.d("URL","URL >>>>"+request_url);
        Map<String, String> params = new HashMap();
        params.put("year", year);
        JSONObject parameters = new JSONObject(params);
        Log.d("TAG", "getWorkShopPhotos: parameters: " + parameters);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, request_url,parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismissProgressDialog();
                        Log.d("getWorkShopVideos","Response >>>"+response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("vidoe");
                            for (int p=0; p<jsonArray.length(); p++){
                                JSONObject jsonObject = jsonArray.getJSONObject(p);
                                PhotosWorkshopList workshopList = new PhotosWorkshopList(
                                        jsonObject.getString("Iid"),
                                        jsonObject.getString("Ilogin"),
                                        jsonObject.getString("Vid"),
                                        jsonObject.getString("vname"),
                                        jsonObject.getString("videourl"),
                                        jsonObject.getString("thumb"),
                                        jsonObject.getString("timeline"),
                                        jsonObject.getString("istyle"),
                                        jsonObject.getString("vdescri")
                                );

                                photosWorkshopLists.add(p, workshopList);
                                Log.d("Category","Category List>>> "+photosWorkshopLists);
                            }
                            PhotosWorkshopListAdapter workshopListAdapter = new PhotosWorkshopListAdapter(getContext(),photosWorkshopLists);
                            recyclerView.setAdapter(workshopListAdapter);

                        } catch (JSONException e) {
                            dismissProgressDialog();
                            Toast.makeText(getContext(), "No Photos Found...", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressDialog();
                        Toast.makeText(requireContext(), "No Photos Found....", Toast.LENGTH_SHORT).show();
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