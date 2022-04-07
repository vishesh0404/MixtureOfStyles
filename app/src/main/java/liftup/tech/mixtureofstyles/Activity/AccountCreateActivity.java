package liftup.tech.mixtureofstyles.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import static liftup.tech.mixtureofstyles.Utils.WebInterface.PASSWORD;

public class AccountCreateActivity extends AppCompatActivity {
    private static final String TAG = AccountCreateActivity.class.getSimpleName();
    private TextView btnNext;
    private RadioGroup radioGroup, goalRadioGroup;
    private RadioButton radioButton, goalRadioButtonon;
    private String dance_type, goal_type, strUserEmail, strUSerPassword;
    private TextView txtBeginner, txtIntermediate, txtAdvanced;
    private String dance_category, goal_category;
    private ProgressDialog progressDialog;
    private String dance_level = "";
    private String dance_goal = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_create);
        progressDialog = new ProgressDialog(this);
        btnNext = findViewById(R.id.next);

        Intent intent = getIntent();
        strUserEmail = intent.getStringExtra(EMAIL_ID);
        strUSerPassword = intent.getStringExtra(PASSWORD);

        radioGroup = findViewById(R.id.radioGroup);
        txtBeginner =  findViewById(R.id.txtbeginner);
        txtIntermediate = findViewById(R.id.txtintermediate);
        txtAdvanced = findViewById(R.id.txtadvanced);

        goalRadioGroup = findViewById(R.id.radioGroup1);
        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
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
                goalRadioButtonon = group.findViewById(checkedId);
                goal_type = goalRadioButtonon.getText().toString();
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


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dance_category = radioButton.getText().toString();
                goal_category = goalRadioButtonon.getText().toString();
                Intent intent = new Intent(getApplicationContext(),SelectPackageActivity1.class);
                intent.putExtra("EMAIL", strUserEmail);
                intent.putExtra("PASSWORD", strUSerPassword);
                intent.putExtra("MEMBERSHIP", dance_level);
                intent.putExtra("GOAL", dance_goal);
                intent.putExtra("STATUS", "");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
    private void userRegistration(String strUserEmail, String strUSerPassword, String dance_level, String dance_goal) {
        showProgressDialogWithTitle("Please wait..");
        String url = WebInterface.BASE_URL + "register.php";
        Log.d(TAG, "userRegistration: URL : "+url);
        Map<String, String> params = new HashMap();
        params.put("email", strUserEmail);
        params.put("pass", strUSerPassword);
        params.put("membership", dance_level);
        params.put("goal", dance_goal);
        JSONObject parameters = new JSONObject(params);
        Log.e("Register","parameters"+parameters);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
                Log.e("Register","Response"+response);
                hideProgressDialogWithTitle();
                //                    message = response.getString("message");
//                Toast.makeText(AccountCreateActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e( "errorrr",""+error );
                hideProgressDialogWithTitle();
                Toast.makeText(AccountCreateActivity.this, "User does not exist...", Toast.LENGTH_SHORT).show();
                //TODO: handle failure
            }
        });
        Volley.newRequestQueue(this).add(jsonRequest);
    }
    //Internet Connection
    //check network connection
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
    // Method to show Progress bar
    private void showProgressDialogWithTitle(String substring) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage(substring);
        progressDialog.show();
    }
    // Method to hide/ dismiss Progress bar
    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }
    private void showCustomDialog() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
        alert.setTitle("No Internet Connection");
        alert.setMessage("Please check your internet connection and try again...!!!");
        alert.setCancelable(false);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        android.app.AlertDialog dialog = alert.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}