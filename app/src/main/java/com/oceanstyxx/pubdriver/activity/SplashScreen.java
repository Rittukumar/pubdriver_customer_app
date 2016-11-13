package com.oceanstyxx.pubdriver.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.helper.SessionManager;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Session manager
        session = new SessionManager(getApplicationContext());


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if (session.isLoggedInRemember()) {
                    // User is already logged in. Take him to main activity
                    session.setLogin(true);
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    // close this activity
                    finish();
                }
                else {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);
    }
}
