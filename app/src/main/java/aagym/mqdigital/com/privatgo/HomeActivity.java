package aagym.mqdigital.com.privatgo;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import aagym.mqdigital.com.privatgo.fragment.HomeFragment;
import aagym.mqdigital.com.privatgo.fragment.NotificationFragment;
import aagym.mqdigital.com.privatgo.fragment.ProfileFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(new ProfileFragment());
                    return true;
                case R.id.navigation_notifications:
                    replaceFragment(new NotificationFragment());
                    return true;
            }
            return false;
        }
    };
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        // initialize fragment manager and fragment transaction
        fragmentManager = getSupportFragmentManager();
        addFragment(new HomeFragment());
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        navigation.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
//        navigation.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
    }


    public void addFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        // fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
