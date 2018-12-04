package aagym.mqdigital.com.privatgo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by alfatih.me on 26/8/2018
 */

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set activity layout
        setContentView(R.layout.activity_splash_screen);

        // define splash time
        int SPLASH_TIME_OUT = 3000; // 3 second in millis

        // create delay when executing code inside
        new Handler().postDelayed(new Runnable() {
            /*
             * show splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // this method will be executed once the timer is over
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);

                // custom pending transition for smooth
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
