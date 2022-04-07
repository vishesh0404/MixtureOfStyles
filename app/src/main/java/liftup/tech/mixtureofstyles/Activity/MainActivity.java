package liftup.tech.mixtureofstyles.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.SessionManager;

public class MainActivity extends AppCompatActivity {
    private static final String IS_LOGIN = "IsLoggedIn";
    boolean isLoggedIn;
    Handler handler;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        handler = new Handler();
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                SharedPreferences settings = getSharedPreferences("MyLoginPrefsFile", MODE_PRIVATE); // 0 - for private mode
                if(settings.getBoolean(IS_LOGIN, isLoggedIn)) {
                    Intent intent=new Intent(getApplicationContext(), DashbordActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }else {
                    Intent intent=new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }

            }
        }, 2000 );    }
}