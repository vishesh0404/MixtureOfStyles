package liftup.tech.mixtureofstyles.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.SessionManager;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

import static android.provider.Settings.NameValueTable.NAME;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.EMAIL_ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.GOAL;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.MEMBER;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.MOBILE_NO;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.PAYED;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.PROFILE_PIC;

public class SignInActivity extends AppCompatActivity {
    public static final String TAG = SignInActivity.class.getSimpleName();
    private String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private EditText txtuserId, txtuserPassword;
    private TextView btnLogin;
    private String userid, password, message;
    private ProgressDialog progressDialog;
    private String id, name, email, mobile, member, goal, profilepic;
    String payed ;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_in);
        progressDialog = new ProgressDialog(this);
        session = new SessionManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        btnLogin =findViewById(R.id.sign_in);
        txtuserId = findViewById(R.id.userEmail);
        txtuserPassword = findViewById(R.id.userPassword);

        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateAccountActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        findViewById(R.id.forgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        // TODO Click on Login button
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                userid = txtuserId.getText().toString();
                password = txtuserPassword.getText().toString();
                if (userid.isEmpty() && password.isEmpty()){
                    txtuserId.setError("User Id & Password");
                    txtuserId.requestFocus();
                }
                else if (userid.isEmpty()) {
                    txtuserId.setError("User Id");
                    txtuserId.requestFocus();
                }
                else if (!userid.matches(emailPattern)) {
                    txtuserId.setError("Valid Email Id");
                    txtuserId.requestFocus();
                }
                else if (password.isEmpty()) {
                    txtuserPassword.setError("Password");
                    txtuserPassword.requestFocus();
                }
                else{
                    if (!isConnected(SignInActivity.this)) {
                        showCustomDialog();
                    } else{
                        //TODO Call login method to pass email & password varibale
                        login(userid, password);
                    }
                }
            }
        });

    }
    // TODO Call login method
    private void login(String userid, String password) {
        showProgressDialogWithTitle("Please wait..");
        String url = "";
        url = WebInterface.BASE_URL + "login.php";
        Log.e("Login", "Login URL: " + url);
        Map<String, String> params = new HashMap();
        params.put("email", userid);
        params.put("pass", password);
        JSONObject parameters = new JSONObject(params);
        Log.d("TAG", "login: parameters: "+parameters);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            //JSONArray mArray;
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Login", "Login Response" + response);
                hideProgressDialogWithTitle();
                try {
                    String message = response.getString("status");
                    Log.d(TAG, "onResponse: message : "+message);
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    JSONObject tutorialsArray = obj.getJSONObject( "data") ;
                    id = tutorialsArray.getString("id");
                    name = tutorialsArray.getString("name");
                    email = tutorialsArray.getString("email");
                    mobile = tutorialsArray.getString("mob");
                    member = tutorialsArray.getString("member");
                    goal = tutorialsArray.getString("goal");
                    payed = tutorialsArray.getString("payed");
                    profilepic = tutorialsArray.getString("profilepic");

                    Log.d(TAG, "onResponse: id : "+id);
                    Log.d(TAG, "onResponse: name : "+name);
                    Log.d(TAG, "onResponse: email : "+email);
                    Log.d(TAG, "onResponse: mobile : "+mobile);
                    Log.d(TAG, "onResponse: member : "+member);
                    Log.d(TAG, "onResponse: goal : "+goal);
                    Log.d(TAG, "onResponse: Payed : "+payed);
                    Log.d(TAG, "onResponse: profilepic : "+profilepic);
                    if (payed.matches("1")){
                        String status = "0";
                        Intent intent = new Intent(getApplicationContext(),SelectPackageActivity1.class);
                        intent.putExtra("STATUS", status);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();

                    }else {
                        session.createLoginSession(email,password);
                        SharedPreferences data=getSharedPreferences("MyPrefs",MODE_PRIVATE);
                        SharedPreferences.Editor editor=data.edit();
                        editor.putString(ID, id);
                        editor.putString(NAME, name);
                        editor.putString(EMAIL_ID, email);
                        editor.putString(MOBILE_NO, mobile);
                        editor.putString(MEMBER, member);
                        editor.putString(GOAL, goal);
                        editor.putString(PAYED, payed);
                        editor.putString(PROFILE_PIC, profilepic);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(),DashbordActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                txtuserId.setText("");
                txtuserPassword.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
                hideProgressDialogWithTitle();
                Toast.makeText(SignInActivity.this, "Invalid Login ", Toast.LENGTH_SHORT).show();
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
        finishAffinity();
    }
}