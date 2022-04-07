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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();
    String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    EditText txtuserEmail;
    String emailId;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgot_password);
        progressDialog = new ProgressDialog(this);
        txtuserEmail = findViewById(R.id.userEmail);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        findViewById(R.id.Continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailId = txtuserEmail.getText().toString().trim();
                isValidate(emailId);

            }
        });
    }
    private void isValidate(String emailId) {
        if (emailId.isEmpty()){
            txtuserEmail.requestFocus();
            txtuserEmail.setError("Please enter registered email");
        }else if(!emailId.matches(emailPattern)){
            txtuserEmail.requestFocus();
            txtuserEmail.setError("Please entered valied email");
        }else {
            if (!isConnected(ForgotPasswordActivity.this)) {
                showCustomDialog();
            } else{
                sendEmail(emailId);
            }
        }
    }
    private void sendEmail(String emailId) {
        showProgressDialogWithTitle("Mail sending..");
        String url = WebInterface.BASE_URL+"forget.php";
        Map<String, String> params = new HashMap<>();
        params.put("email", emailId);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressDialogWithTitle();
                Log.e("check_email", "check_email Response " + response);
                try {
                    txtuserEmail.setText("");
                    Log.d(TAG, "onResponse: Response : "+response);
                    showCustomDialog1();
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Server Not Responding..Please Try Again",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                hideProgressDialogWithTitle();
                Toast.makeText(ForgotPasswordActivity.this, "Mail not send..", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onErrorResponse: "+error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
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
    //TODO Display No Internet custom dialog
    private void showCustomDialog1() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Message Send");
        alert.setMessage("We will send a password reset link to your registered mail id\n Please check your mail!!!");
        alert.setCancelable(false);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(),DisplayPassMessageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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