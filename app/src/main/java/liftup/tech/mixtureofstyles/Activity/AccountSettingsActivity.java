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
import android.widget.EditText;
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

import static android.provider.Telephony.Carriers.PASSWORD;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.EMAIL_ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.ID;

public class AccountSettingsActivity extends AppCompatActivity {
    public static final String TAG = AccountSettingsActivity.class.getSimpleName();
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private EditText txtUserName, txtFullName, txtUserEmail;
    private TextView btnUpdate;
    private String userId, strUserName, strFullName, strEmail;
    private ProgressDialog progressDialog;
    String username, name, email, mob, member, goal, profilepic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_settings);
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        name = intent.getStringExtra("NAME");
        email = intent.getStringExtra("EMAIL");
        mob = intent.getStringExtra("MOBILE");
        member = intent.getStringExtra("MEMBER");
        goal = intent.getStringExtra("GOAL");
        profilepic = intent.getStringExtra("PROFILEPIC");


        @SuppressLint("WrongConstant")
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs",MODE_APPEND);
        userId = sharedPreferences.getString(ID, " ");
        strEmail = sharedPreferences.getString(EMAIL_ID, " ");
        Log.d(TAG,"Email ID >>>"+strEmail);

        txtUserName = findViewById(R.id.uname);
        txtFullName = findViewById(R.id.fullName);
        txtUserEmail = findViewById(R.id.userEmail);
        btnUpdate = findViewById(R.id.update);
        txtUserName.setText(username);
        txtFullName.setText(name);
        txtUserEmail.setText(strEmail);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strUserName = txtUserName.getText().toString();
                strFullName = txtFullName.getText().toString();
                isValidate(strUserName, strFullName, email);
            }
        });
    }

    private void isValidate(String strUserName, String strFullName, String strEmail) {
        if (strUserName.isEmpty() && strFullName.isEmpty() && strEmail.isEmpty()){
            txtUserName.setError("User Name");
            txtFullName.setError("Full Name");
            txtUserEmail.setError("Email Name");
            txtUserName.requestFocus();
        }
        else if (strUserName.isEmpty()) {
            txtUserName.setError("User Name");
            txtUserName.requestFocus();
        }
        else if (strFullName.isEmpty()) {
            txtFullName.setError("Full Name");
            txtFullName.requestFocus();
        }
        else if (strEmail.isEmpty()) {
            txtUserEmail.setError("Enter Registered Email");
            txtUserEmail.requestFocus();
        }
        else if (!strEmail.matches(emailPattern)) {
            txtUserEmail.setError("Valid Email Id");
            txtUserEmail.requestFocus();
        }
        else{
            if (!isConnected(AccountSettingsActivity.this)) {
                showCustomDialog();
            } else{
                updateProfile(strUserName, strFullName, strEmail, userId);
            }
        }
    }
    private void updateProfile(String strUserName, String strFullName, String strEmail, String userID) {
        showProgressDialogWithTitle("Please Wait..");
        String url = "";
        url = WebInterface.BASE_URL + "profile/profile_edit.php";
        Log.e(TAG, "verifyUser URL: " + url);
        Map<String, String> params = new HashMap();
        params.put("username", strUserName);
        params.put("name", strFullName);
        params.put("email", strEmail);
        params.put("loginid", userID);
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
                    Toast.makeText(AccountSettingsActivity.this, ""+message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AccountSettingsActivity.this, "Profile not update..", Toast.LENGTH_SHORT).show();
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