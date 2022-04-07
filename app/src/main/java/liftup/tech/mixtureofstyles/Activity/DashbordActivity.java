package liftup.tech.mixtureofstyles.Activity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import liftup.tech.mixtureofstyles.R;

public class DashbordActivity extends AppCompatActivity {

    public Integer selectedMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);
        getSupportActionBar().hide();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_saved, R.id.navigation_showcase, R.id.navigation_list, R.id.navigation_showcase)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void loadFragment(int destination) {
        NavHostFragment navHostFragment = new NavHostFragment();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.navigation_showcase, null, new NavOptions.Builder()
        .setPopUpTo(R.id.navigation_home, true)
        .build());
//        navController.popBackStack(R.id.navigation_home, true);
//        navController
    }

}