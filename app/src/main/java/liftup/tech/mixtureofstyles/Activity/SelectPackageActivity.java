package liftup.tech.mixtureofstyles.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import liftup.tech.mixtureofstyles.R;

import static android.provider.Settings.NameValueTable.NAME;
import static liftup.tech.mixtureofstyles.Activity.CreateAccountActivity.TAG;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.EMAIL_ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.PAYED;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.PROFILE_PIC;

public class SelectPackageActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    TextView txtFreeTrail, txtMonthlyTrail, txtThreeMonthTrail;
    Float TotalPay;
    LinearLayout freeLayout;
    private String userId, userName, userEmail, userProfile, strPayed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_package);
        txtFreeTrail = findViewById(R.id.free_trail);
        txtMonthlyTrail = findViewById(R.id.monthlyTrail);
        txtThreeMonthTrail = findViewById(R.id.ThreeMonthTrail);
        freeLayout = findViewById(R.id.freeLayout);

        @SuppressLint("WrongConstant")
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs",MODE_APPEND);
        userId = sharedPreferences.getString(ID, " ");
        userName = sharedPreferences.getString(NAME, " ");
        userEmail = sharedPreferences.getString(EMAIL_ID, " ");
        strPayed = sharedPreferences.getString(PAYED," ");
        userProfile = sharedPreferences.getString(PROFILE_PIC, " ");
        Log.d(TAG,"Email ID >>>"+strPayed);

        if (strPayed.matches("0")){
            freeLayout.setVisibility(View.GONE);
        }else {
            freeLayout.setVisibility(View.GONE);
        }
        txtMonthlyTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TotalPay = Float.valueOf(699);
                startPayment(TotalPay);

            }
        });
        txtThreeMonthTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TotalPay = Float.valueOf(1800);
                startPayment(TotalPay);
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
    public void startPayment(Float totalPay) {
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.payment_logo);
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
        Intent intent = new Intent(getApplicationContext(),DashbordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Intent intent = new Intent(getApplicationContext(),SelectPackageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();

    }
}