package liftup.tech.mixtureofstyles.Activity;

import androidx.appcompat.app.AppCompatActivity;
import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static liftup.tech.mixtureofstyles.Utils.WebInterface.EMAIL_ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.GOAL;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.PASSWORD;

public class SelectPackageActivity1 extends AppCompatActivity implements PaymentResultWithDataListener {

    private static final String TAG = SelectPackageActivity.class.getSimpleName();
    TextView txtMonthly, txtThreeMonth;
    ProgressDialog progressDialog;
    String strUserEmail, strUserPassword, strDanceStyle, strDanceGoal;
    String strpay;
    String transId;
    String status;
    TextView txtFreeTrail;
    Float TotalPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_package1);
        progressDialog = new ProgressDialog(this);
        txtMonthly = findViewById(R.id.tvMonthly);
        txtThreeMonth = findViewById(R.id.tvThreeMonth);
        txtFreeTrail = findViewById(R.id.free_trail);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        txtMonthly.setPaintFlags(txtMonthly.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtThreeMonth.setPaintFlags(txtThreeMonth.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Intent intent = getIntent();
        strUserEmail = intent.getStringExtra("EMAIL");
        strUserPassword = intent.getStringExtra("PASSWORD");
        strDanceStyle = intent.getStringExtra("MEMBERSHIP");
        strDanceGoal = intent.getStringExtra("GOAL");
        status = intent.getStringExtra("STATUS");
        Log.d(TAG, "onCreate: Status : "+status);
        if (status.matches("0")){
            txtFreeTrail.setVisibility(View.GONE);
        }else {
            txtFreeTrail.setVisibility(View.VISIBLE);

        }
        txtFreeTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transId = "0";
                strpay = "0";
                userRegistration(strUserEmail, strUserPassword, strDanceStyle, strDanceGoal, transId, strpay);

            }
        });

        findViewById(R.id.month_trail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TotalPay = Float.valueOf(699);
                startPayment(TotalPay);
                Toast.makeText(getApplicationContext(), "Monthly", Toast.LENGTH_SHORT).show();

            }
        });

        findViewById(R.id.threeMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TotalPay = Float.valueOf(1800);
                startPayment(TotalPay);
                Toast.makeText(getApplicationContext(), "Three month", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void startPayment(Float totalPay) {
        Checkout checkout = new Checkout();
        checkout.setImage(R.mipmap.ic_launcher);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("currency", "INR");
            options.put("description", "My Orders");
            options.put("amount", totalPay*100);//pass amount in currency subunits
            JSONObject prefilled = new JSONObject();
            prefilled.put("email","swapnil1232gmail.com");
            prefilled.put("contact","9823472465");
            prefilled.put("prefilled",prefilled);
            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(SelectPackageActivity1.this, "Payment Success....", Toast.LENGTH_SHORT).show();
        transId = paymentData.getPaymentId();
        strpay ="2";
        userRegistration(strUserEmail, strUserPassword, strDanceStyle, strDanceGoal, transId, strpay);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Intent intent = new Intent(getApplicationContext(),SelectPackageActivity1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();

    }
    private void userRegistration(String strUserEmail, String strUSerPassword, String dance_level, String dance_goal,String transId, String strpay) {
        showProgressDialogWithTitle("Please wait..");
        String url = WebInterface.BASE_URL + "register.php";
        Log.d(TAG, "userRegistration: URL : "+url);
        Map<String, String> params = new HashMap();
        params.put("email", strUserEmail);
        params.put("pass", strUSerPassword);
        params.put("membership", dance_level);
        params.put("goal", dance_goal);
        params.put("transid",transId);
        params.put("payed",strpay);
        JSONObject parameters = new JSONObject(params);
        Log.e("Register","parameters"+parameters);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
                Log.e("Register","Response"+response);
                hideProgressDialogWithTitle();
                //                    message = response.getString("message");
                Toast.makeText(SelectPackageActivity1.this, "Registration Success....", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "User does not exist...", Toast.LENGTH_SHORT).show();
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