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
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import static liftup.tech.mixtureofstyles.Utils.WebInterface.EMAIL_ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.ID;

public class PasswordChangeActivity extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";
    private String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
    private EditText newpass, confpass;
    private TextView changepass;
    private String Newpass, Confpass;
    private ProgressDialog progressDialog;
    private String userId, strEmail;

    ImageView imgPasswordToggle, imgRegPassToggle, imgPasswordToggle_Eye, imgRegPassToggle_Eye;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_password_change);
        progressDialog = new ProgressDialog(this);
        newpass = findViewById(R.id.newPassword);
        confpass = findViewById(R.id.reEnterPassword);
        imgPasswordToggle = findViewById(R.id.password_toggle);
        imgRegPassToggle = findViewById(R.id.re_password_toggle);
        imgPasswordToggle_Eye = findViewById(R.id.password_toggle_disable);
        imgRegPassToggle_Eye = findViewById(R.id.re_password_toggle_disable);

        @SuppressLint("WrongConstant")
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs",MODE_APPEND);
        userId = sharedPreferences.getString(ID, " ");
        strEmail = sharedPreferences.getString(EMAIL_ID, " ");
        Log.d(TAG,"Email ID >>>"+strEmail);
        findViewById(R.id.changePass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Newpass = newpass.getText().toString();
                Confpass = confpass.getText().toString();
                isValidate(Newpass, Confpass, userId);
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgPasswordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newpass.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                    newpass.setTransformationMethod(new SingleLineTransformationMethod());
                    imgPasswordToggle_Eye.setVisibility(View.VISIBLE);
                    imgPasswordToggle.setVisibility(View.GONE);
                }
               /* else {
                    newpass.setTransformationMethod(new PasswordTransformationMethod());
                }*/

                newpass.setSelection(newpass.getText().length());

            }
        });
        imgRegPassToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confpass.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                    confpass.setTransformationMethod(new SingleLineTransformationMethod());
                    imgRegPassToggle_Eye.setVisibility(View.VISIBLE);
                    imgRegPassToggle.setVisibility(View.GONE);
                }
                confpass.setSelection(confpass.getText().length());
            }
        });
        imgPasswordToggle_Eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newpass.setTransformationMethod(new PasswordTransformationMethod());
                imgPasswordToggle_Eye.setVisibility(View.GONE);
                imgPasswordToggle.setVisibility(View.VISIBLE);
                newpass.setSelection(newpass.getText().length());

            }
        });
        imgRegPassToggle_Eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confpass.setTransformationMethod(new PasswordTransformationMethod());
                imgRegPassToggle_Eye.setVisibility(View.GONE);
                imgRegPassToggle.setVisibility(View.VISIBLE);
                confpass.setSelection(confpass.getText().length());
            }
        });
    }
    private void isValidate(String newpassword, String confpassword, String userId) {
        if (newpassword.isEmpty() && confpassword.isEmpty()){
            newpass.requestFocus();
            newpass.setError("Enter password");
            confpass.setError("Enter password");
//            Toast.makeText(getApplicationContext(), "Fields can not be empty", Toast.LENGTH_SHORT).show();

        } else if (newpassword.isEmpty()){
            newpass.requestFocus();
            newpass.setError("Enter password");

        }else if (!newpass.getText().toString().matches(passwordPattern)) {
            newpass.requestFocus();
            Toast.makeText(getApplicationContext(), "Password must be one upper case letter, Special charrectors symbol, and number value \n For ex. Admin@123", Toast.LENGTH_SHORT).show();

        } else if (confpassword.isEmpty()){
            confpass.requestFocus();
            confpass.setError("Enter confirm password");
        }
        else if (!newpassword.matches(confpassword)){
            confpass.requestFocus();
            confpass.setError("Password does not matched");
        }
        else {
            changePassword(newpassword, userId);
        }
    }

    private void changePassword(String newpassword, String userId) {
        showProgressDialogWithTitle("Please wait..");
        String url = "";
        url = WebInterface.BASE_URL + "acc_reset.php";
        Log.e("changePassword", "reset_password URL: " + url);
        Map<String, String> params = new HashMap();
        params.put("login", userId);
        params.put("pass", newpassword);
        org.json.JSONObject parameters = new JSONObject(params);
        Log.d(TAG, "Reset Password: "+parameters);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, response -> {
            hideProgressDialogWithTitle();
//              progressBar.setVisibility(View.GONE);
            Log.d("changePassword", "reset_password Response " + response);
            Toast.makeText(PasswordChangeActivity.this, "Password Changed Successfull..", Toast.LENGTH_SHORT).show();
            newpass.setText("");
            confpass.setText("");
            onBackPressed();

        }, error -> {
            error.printStackTrace();
            hideProgressDialogWithTitle();
            Log.e(TAG, "onErrorResponse: "+error.getLocalizedMessage());
            Toast.makeText(PasswordChangeActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            //TODO: handle failure
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