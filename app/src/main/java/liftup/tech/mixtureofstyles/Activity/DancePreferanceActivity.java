package liftup.tech.mixtureofstyles.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

import static liftup.tech.mixtureofstyles.Utils.WebInterface.EMAIL_ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.ID;

public class DancePreferanceActivity extends AppCompatActivity {
    private static final String TAG = DancePreferanceActivity.class.getSimpleName();
    RadioGroup radioGroup, goalRadioGroup;
    RadioButton rbtnBeginner, rbtnIntermediate, radioAdvanced, radioButton, goalRadioButton;
    String dance_type, goal_type;
    TextView txtBeginner, txtIntermediate, txtAdvanced;
    private String dance_category, goal_category;
    String dance_level= "";
    private String dance_goal = "";
    String username, name, email, mob, member, goal, profilepic;
    private String userId, strUserName, strFullName, strEmail;

    TextView next;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_dance_preferance);
        progressDialog = new ProgressDialog(this);
        radioGroup = findViewById(R.id.radioGroup);
        rbtnBeginner = findViewById(R.id.beginner);
        rbtnIntermediate = findViewById(R.id.intermediate);
        radioAdvanced = findViewById(R.id.advanced);
        txtBeginner =  findViewById(R.id.txtbeginner);
        txtIntermediate = findViewById(R.id.txtintermediate);
        txtAdvanced = findViewById(R.id.txtadvanced);
        next = findViewById(R.id.next);
        goalRadioGroup = findViewById(R.id.radioGroup1);

        @SuppressLint("WrongConstant")
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs",MODE_APPEND);
        userId = sharedPreferences.getString(ID, " ");
        strEmail = sharedPreferences.getString(EMAIL_ID, " ");
        Log.d(TAG,"Email ID >>>"+strEmail);

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        name = intent.getStringExtra("NAME");
        email = intent.getStringExtra("EMAIL");
        mob = intent.getStringExtra("MOBILE");
        member = intent.getStringExtra("MEMBER");
        goal = intent.getStringExtra("GOAL");
        profilepic = intent.getStringExtra("PROFILEPIC");
        Log.d(TAG, "onCreate: Data : User Name : "+username+" Name : "+name+" Email : "+email+" Mobile :"+mob+" Member : "+member+" Goal : "+goal);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = group.findViewById(checkedId);
                dance_type = radioButton.getText().toString();
                Log.d(TAG, "onCheckedChanged: Address Type : "+dance_type);
                if (dance_type.equals("Beginner")){
                    dance_level = "23";
                    txtBeginner.setTextColor(getResources().getColor(R.color.white));
                    txtIntermediate.setTextColor(getResources().getColor(R.color.lightgray2));
                    txtAdvanced.setTextColor(getResources().getColor(R.color.lightgray2));
                }else if (dance_type.equals("Intermediate")){
                    dance_level = "24";
                    txtBeginner.setTextColor(getResources().getColor(R.color.lightgray2));
                    txtIntermediate.setTextColor(getResources().getColor(R.color.white));
                    txtAdvanced.setTextColor(getResources().getColor(R.color.lightgray2));
                }else if (dance_type.equals("Advanced")){
                    dance_level = "25";
                    txtBeginner.setTextColor(getResources().getColor(R.color.lightgray2));
                    txtIntermediate.setTextColor(getResources().getColor(R.color.lightgray2));
                    txtAdvanced.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
        goalRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                goalRadioButton = group.findViewById(checkedId);
                goal_type = goalRadioButton.getText().toString();
                Log.d(TAG, "onCheckedChanged: Goal Type : "+goal_type);
                if (goal_type.equals("Learn How To Choreograph")){
                    dance_goal = "18";
                }else if (goal_type.equals("Dance In Public Confidently")){
                    dance_goal = "19";
                }else if (goal_type.equals("Learn Basic Dance Foundations")){
                    dance_goal = "20";
                }else if (goal_type.equals("Learn How To Freestyle")){
                    dance_goal = "21";
                }else if (goal_type.equals("Stay Healthy With Fun Dance Classes")){
                    dance_goal = "22";
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dance_category = radioButton.getText().toString();
                goal_category = goalRadioButton.getText().toString();
                updateProfile(dance_level, dance_goal, userId);
//                userRegistration(strUserEmail, strUSerPassword, dance_level, dance_goal);
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
    private void updateProfile(String dance_level, String dance_goal, String userId) {
        showProgressDialogWithTitle("Please Wait..");
        String url = "";
        url = WebInterface.BASE_URL + "profile/pref_update.php";
        Log.e(TAG, "verifyUser URL: " + url);
        Map<String, String> params = new HashMap();
        params.put("level", dance_level);
        params.put("goal", dance_goal);
        params.put("loginid", userId);
        JSONObject parameters = new JSONObject(params);
        Log.d(TAG, "verifyUser: parameters: "+parameters);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            //JSONArray mArray;
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "verifyUser Response" + response);
                hideProgressDialogWithTitle();
                try {
                    String message = response.getString("message");
                    Toast.makeText(DancePreferanceActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    onBackPressed();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Server Not Responding..Please Try Again",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
                hideProgressDialogWithTitle();
                Toast.makeText(DancePreferanceActivity.this, "Profile not update..", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonRequest);
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