package liftup.tech.mixtureofstyles.Activity.ui.saved;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

import static android.content.Context.MODE_APPEND;
import static liftup.tech.mixtureofstyles.Activity.CreateAccountActivity.TAG;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.EMAIL_ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.ID;


public class SavedFragment extends Fragment {

    private SavedVideoList dashboardViewModel;
    private List<SavedVideoList> savedVideoLists;
    private RecyclerView recyclerView;
    private String userId, strEmail;
    ImageView ivDelete;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = root.findViewById(R.id.recyclerview);
        ivDelete = root.findViewById(R.id.delete);

        @SuppressLint("WrongConstant")
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs",MODE_APPEND);
        userId = sharedPreferences.getString(ID, " ");
        strEmail = sharedPreferences.getString(EMAIL_ID, " ");
        Log.d(TAG,"Email ID >>>"+strEmail);

        savedVideoLists = new ArrayList<>();
        getVideoList(userId);

        return root;
    }

    private void getVideoList(String userId) {
        savedVideoLists = new ArrayList<>();
        SavedVideoListAdapter listAdapter = new SavedVideoListAdapter(getContext(), savedVideoLists, new SavedVideoListAdapter.OnItemSelectedListner() {
            @Override
            public void onItemSelect(List<Object> id) {
                Log.d(TAG, "onItemSelect: ID : "+id);
                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savedVideoLists.removeAll(id);
                        videoDelete(id);
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);


        //        showProgressDialogWithTitle("Loading Product...");
        String request_url = WebInterface.BASE_URL + "save/video_view.php";
        Log.d("URL","URL >>>>"+request_url);
        Map<String, String> params = new HashMap();
        params.put("login", userId);
        JSONObject parameters = new JSONObject(params);
        Log.d("TAG", "login: parameters: " + parameters);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, request_url,parameters,
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
                                SavedVideoList savedVideoList = new SavedVideoList(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("videourl"),
                                        jsonObject.getString("thumb"),
                                        jsonObject.getString("vname"),
                                        jsonObject.getString("iname"),
                                        jsonObject.getString("timeline"),
                                        jsonObject.getString("istyle")
                                );
                                savedVideoLists.add(p, savedVideoList);
//                                homeServices.add(jsonObject.getString(Config.SERVICE_NAME));
                                Log.d("Category","Category List>>> "+savedVideoLists);
                            }
                            SavedVideoListAdapter listAdapter = new SavedVideoListAdapter(getContext(), savedVideoLists, new SavedVideoListAdapter.OnItemSelectedListner() {
                                @Override
                                public void onItemSelect(List<Object> id) {
                                    Log.d(TAG, "onItemSelect: ID : "+id);
                                    ivDelete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            savedVideoLists.removeAll(id);
                                            videoDelete(id);
                                        }
                                    });

                                }
                            });
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(listAdapter);

                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Video Not Found...", Toast.LENGTH_SHORT).show();
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
       /* JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, request_url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        hideProgressDialogWithTitle();
                        Log.d("Response","Response >>>"+response);
                        try {
                            if (response.length() > 0) {
                                savedVideoLists.clear();
                                JSONArray jsonArray = response.getJSONArray("record");
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    SavedVideoList savedVideoList = new SavedVideoList();
                                    savedVideoList.id = jsonObject.getString("id");
                                    savedVideoList.videourl = jsonObject.getString("videourl");
                                    savedVideoList.vname = jsonObject.getString("vname");
                                    savedVideoList.thumb = jsonObject.getString("thumb");
                                    savedVideoList.istyle = jsonObject.getString("istyle");
                                    savedVideoList.timeline = jsonObject.getString("iname");
                                    savedVideoLists.add(i, savedVideoList);
                                    Log.d(TAG, "onResponse: List Data : "+savedVideoList);
                                }
                                SavedVideoListAdapter allSpiecesListAdapter = new SavedVideoListAdapter(getContext(),savedVideoLists);
                                recyclerView.setAdapter(allSpiecesListAdapter);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"Server Not Responding..Please Try Again",Toast.LENGTH_SHORT).show();
                            Log.e("catchException",e.toString());
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
                });*/
        //Creating a request queue
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);

    }

    private void videoDelete(List<Object> id) {

        Log.d(TAG, "videoDelete: List : "+id);
        String request_url = WebInterface.BASE_URL + "save/video_del.php";
        Log.d("URL","URL >>>>"+request_url);
        Map<String, Object> params = new HashMap();
        params.put("ids", id);
        JSONObject parameters = new JSONObject(params);
        Log.d("TAG", "videoDelete: parameters: " + parameters);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, request_url,parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        hideProgressDialogWithTitle();
                        Log.d("videoDelete","Response >>>"+response);
                        Toast.makeText(getContext(), "Video Delete Success", Toast.LENGTH_SHORT).show();
                        getVideoList(userId);

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

    //TODO Internet Connection
    //TODO check network connection
    public boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        } else
            return false;
    }
    //TODO Display No Internet custom dialog
    private void showCustomDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("No Internet Connection");
        alert.setMessage("Please check your internet connection and try again...!!!");
        alert.setCancelable(false);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }
}