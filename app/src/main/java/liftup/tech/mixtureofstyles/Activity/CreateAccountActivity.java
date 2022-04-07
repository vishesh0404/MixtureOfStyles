package liftup.tech.mixtureofstyles.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
import static liftup.tech.mixtureofstyles.Utils.WebInterface.EMAIL_ID;

public class CreateAccountActivity extends AppCompatActivity {
    public static final String TAG = CreateAccountActivity.class.getSimpleName();
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{6,12}$";
    private  String userEmail, userPassword;
    private  EditText txtEmail, txtPassword;
    TextView btnsign_up;
    ProgressDialog progressDialog;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_account);
        progressDialog = new ProgressDialog(this);

        txtEmail = findViewById(R.id.userEmail);
        txtPassword = findViewById(R.id.userPassword);

        btnsign_up = findViewById(R.id.sign_up);
        btnsign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = txtEmail.getText().toString();
                userPassword = txtPassword.getText().toString();
                isValidation(userEmail, userPassword);
            }
        });
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
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
    }
    private void isValidation(String userEmail, String userPassword) {
        if (userEmail.isEmpty() && userPassword.isEmpty()){
            txtEmail.setError("Email Id & Password");
            txtEmail.requestFocus();
        }
        else if (userEmail.isEmpty()) {
            txtEmail.setError("Email Id");
            txtEmail.requestFocus();
        }
        else if (!userEmail.matches(emailPattern)) {
            txtEmail.setError("Valid Email Id");
            txtEmail.requestFocus();
        }
        else if (userPassword.isEmpty()) {
            txtPassword.setError("Password");
            txtPassword.requestFocus();
        }
        else{
            if (!isConnected(CreateAccountActivity.this)) {
                showCustomDialog();
            } else{
                checkUserDetails(userEmail, userPassword);
            }
        }
    }

    private void checkUserDetails(String userEmail, String userPassword) {
        showProgressDialogWithTitle("Verify data..");
        String url = "";
        url = WebInterface.BASE_URL + "verify.php";
        Log.e(TAG, "verifyUser URL: " + url);
        Map<String, String> params = new HashMap();
        params.put("email", userEmail);
        JSONObject parameters = new JSONObject(params);
        Log.d(TAG, "verifyUser: parameters: "+parameters);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            //JSONArray mArray;
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "verifyUser Response" + response);
                hideProgressDialogWithTitle();
                try {
                    message = response.getString("message");
                    Intent intent = new Intent(getApplicationContext(),AccountCreateActivity.class);
                    intent.putExtra(EMAIL_ID, userEmail);
                    intent.putExtra(PASSWORD, userPassword);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
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
                Toast.makeText(CreateAccountActivity.this, "User Already exist..", Toast.LENGTH_SHORT).show();
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