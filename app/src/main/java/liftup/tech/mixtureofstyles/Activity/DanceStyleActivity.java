package liftup.tech.mixtureofstyles.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import de.hdodenhof.circleimageview.CircleImageView;
import liftup.tech.mixtureofstyles.Adapter.DanceDetailsListAdapter;
import liftup.tech.mixtureofstyles.Adapter.DanceStyleListAdapter;
import liftup.tech.mixtureofstyles.Adapter.DanceStyleVideoListAdapter;
import liftup.tech.mixtureofstyles.Model.DanceDetailsList;
import liftup.tech.mixtureofstyles.Model.DanceStyleList;
import liftup.tech.mixtureofstyles.Model.DanceStyleVideoList;
import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

public class DanceStyleActivity extends AppCompatActivity {
    private static final String TAG = DanceDetailsActivity.class.getSimpleName();
    RecyclerView recyclerView;
    private List<DanceStyleVideoList> styleVideoLists;
    TextView txtName, txtStyle, txtVideo, txtDescription;
    ImageView imgInsta;
    CircleImageView imgThumbnail;
    ProgressDialog progressDialog;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_dance_style);
        progressDialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recyclerview);

        txtName = findViewById(R.id.name);
        txtStyle = findViewById(R.id.style);
        txtVideo = findViewById(R.id.video);
        txtDescription = findViewById(R.id.description);
        imgThumbnail = findViewById(R.id.tumbnail);
        imgInsta = findViewById(R.id.insta);
        Intent i = getIntent();
        id = i.getStringExtra("ID");
        Log.d(TAG, "onCreate: ID : "+id);

        styleVideoLists = new ArrayList<>();
        getVideoDetailsList();
        imgInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/swappn");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/swappn")));
                }

            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
    }

    private void getVideoDetailsList() {
        showProgressDialogWithTitle("Please wait..");
        String request_url = WebInterface.BASE_URL + "work/list_details.php";
        Log.d("URL","URL >>>>"+request_url);
        Map<String, String> params = new HashMap();
        params.put("id", id);
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
                            String coursalvideo = tutorialsArray.getString("videourl");
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
                                DanceStyleVideoList styleVideoList = new DanceStyleVideoList(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("vname"),
                                        jsonObject.getString("vdesc"),
                                        jsonObject.getString("videourl"),
                                        jsonObject.getString("thumb"),
                                        jsonObject.getString("timeline"),
                                        jsonObject.getString("style"),
                                        jsonObject.getString("level")
                                );
                                styleVideoLists.add(p, styleVideoList);
                                Log.d("Category","Category List>>> "+styleVideoLists);

                                txtName.setText(name);
                                txtVideo.setText(video+" Tutorial Videos ");
                                txtStyle.setText(style);
                                txtDescription.setText(descri);
                                Glide.with(getApplicationContext()).load(thumb).into(imgThumbnail);
                            }
                            DanceStyleVideoListAdapter danceStyleVideoListAdapter = new DanceStyleVideoListAdapter(getApplicationContext(),styleVideoLists);
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                            recyclerView.setAdapter(danceStyleVideoListAdapter);

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