package liftup.tech.mixtureofstyles.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import liftup.tech.mixtureofstyles.Adapter.DanceDetailsListAdapter;
import liftup.tech.mixtureofstyles.Adapter.DanceStyleListAdapter;
import liftup.tech.mixtureofstyles.Model.DanceDetailsList;
import liftup.tech.mixtureofstyles.Model.DanceStyleList;
import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

import static liftup.tech.mixtureofstyles.Activity.CreateAccountActivity.TAG;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.EMAIL_ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.ID;

public class DanceDetailsActivity extends AppCompatActivity {
    private static final String TAG = DanceDetailsActivity.class.getSimpleName();
    RecyclerView recyclerView;
    private List<DanceDetailsList> detailsLists;
    String id;
    TextView txtname, txtTimelin, txtStyle, txtDescription, txtViewAll;
    ImageView imgThumbnail, imgPlay;
    ProgressDialog progressDialog;
    LinearLayout saveVideo;
    private String userId, strUserName, strFullName, strEmail;
    String videoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_dance_details);
        recyclerView = findViewById(R.id.recyclerview);
        progressDialog = new ProgressDialog(this);

        txtname = findViewById(R.id.name);
        txtTimelin = findViewById(R.id.timeline);
        txtStyle = findViewById(R.id.style);
        txtDescription = findViewById(R.id.description);
        imgThumbnail = findViewById(R.id.thumbnail);
        imgPlay = findViewById(R.id.play);
        txtViewAll = findViewById(R.id.viewAll);
        saveVideo = findViewById(R.id.save_video);

        Intent i = getIntent();
        id = i.getStringExtra("ID");
//        videoUrl = i.getStringExtra("VIDEO_URL");
        Log.d(TAG, "onCreate: ID : "+id);

        @SuppressLint("WrongConstant")
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs",MODE_APPEND);
        userId = sharedPreferences.getString(ID, " ");
        strEmail = sharedPreferences.getString(EMAIL_ID, " ");
        Log.d(TAG,"Email ID >>>"+strEmail);

        saveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoSaved(userId, id);
            }
        });

        txtViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DanceStyleActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        detailsLists = new ArrayList<>();
        getVideoDetailsList(id);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
    }

    private void videoSaved(String userId, String video_id) {
        showProgressDialogWithTitle("Please wait..");
        String request_url = WebInterface.BASE_URL + "save/video_add.php";
        Log.d("URL","URL >>>>"+request_url);
        Map<String, String> params = new HashMap();
        params.put("login", userId);
        params.put("video", video_id);
        JSONObject parameters = new JSONObject(params);
        Log.d("TAG", "login: parameters: " + parameters);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, request_url,parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialogWithTitle();
                        Log.d("Response","Response >>>"+response);
//                        System.out.println(response);
                        Toast.makeText(DanceDetailsActivity.this, "Video Added in saved List..", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialogWithTitle();
                        Toast.makeText(DanceDetailsActivity.this, "Video not saved..", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onErrorResponse: Error"+error );
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }

    private void getVideoDetailsList(String video_id) {
        showProgressDialogWithTitle("Please wait..");
        String request_url = WebInterface.BASE_URL + "work/list_details.php";
        Log.d("URL","URL >>>>"+request_url);
        Map<String, String> params = new HashMap();
        params.put("id", video_id);
        JSONObject parameters = new JSONObject(params);
        Log.d("TAG", "login: parameters: " + parameters);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, request_url,parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialogWithTitle();
                        Log.d("Response","Response >>>"+response);
//                        System.out.println(response);
                        try {
                            String message = response.getString("id");
                            JSONObject obj = new JSONObject(String.valueOf(response));
                            JSONObject tutorialsArray = obj.getJSONObject( "coursalvideo") ;
                            String videoUrl = tutorialsArray.getString("videourl");
                            String thumb = tutorialsArray.getString("thumb");
                            String timeline = tutorialsArray.getString("timeline");
                            String istyle = tutorialsArray.getString("istyle");

                            String name = response.getString("name");
                            String uname = response.getString("uname");
                            String style = response.getString("style");
                            String descri = response.getString("descri");
                            String video = response.getString("video");
                            String inst = response.getString("inst");
                            String pimage = response.getString("pimage");

                            JSONArray jsonArray = response.getJSONArray("allvideo");
                            for (int p=0; p<jsonArray.length(); p++){
                                JSONObject jsonObject = jsonArray.getJSONObject(p);
                                DanceDetailsList danceDetailsList = new DanceDetailsList(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("vname"),
                                        jsonObject.getString("vdesc"),
                                        jsonObject.getString("videourl"),
                                        jsonObject.getString("thumb"),
                                        jsonObject.getString("timeline"),
                                        jsonObject.getString("style"),
                                        jsonObject.getString("level")
                                );
                                detailsLists.add(p, danceDetailsList);
                                Log.d("Category","Category List>>> "+danceDetailsList);

                                imgPlay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), ExoplayerActivity.class);
                                        intent.putExtra("VURL", videoUrl);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });
                                txtname.setText(name);
                                txtTimelin.setText(" "+timeline+" ");
                                txtStyle.setText(style);
                                txtDescription.setText(descri);
                                Glide.with(getApplicationContext()).load(thumb).into(imgThumbnail);
                            }
                            DanceDetailsListAdapter danceDetailsListAdapter = new DanceDetailsListAdapter(getApplicationContext(),detailsLists);
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                            recyclerView.setAdapter(danceDetailsListAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialogWithTitle();
                        Log.e(TAG, "onErrorResponse: Error"+error );
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }
    //TODO Method to show Progress bar
    private void showProgressDialogWithTitle(String substring) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //TODO Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage(substring);
        progressDialog.show();
    }
    // TODO Method to hide/ dismiss Progress bar
    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
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
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}