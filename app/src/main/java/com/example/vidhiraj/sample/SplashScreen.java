package com.example.vidhiraj.sample;

/**
 * Created by lenovo on 20/08/2016.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by androiddev on 8/12/16.
 */
public class SplashScreen extends AppCompatActivity {
    ImageView eracod_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        eracod_image = (ImageView) findViewById(R.id.eracord_pic);
        Thread timer = new Thread() {
            public void run() {
                try {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
                    eracod_image.startAnimation(animation);
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }

}













