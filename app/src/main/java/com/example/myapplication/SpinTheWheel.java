package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class SpinTheWheel extends AppCompatActivity {
    ImageView wheeling;
    Button spinthewheel, GoToLogin;
    String[] sectors={"Ami","3 Amigo","Star of India","Sushi Crystal",
            "Show Wok","Hot Pot 9","Cuisine Sizchuan","Mon Ami","Nos The",
            "PM Restaurant","Pho","The Keg"};

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_the_wheel);

        wheeling=findViewById(R.id.wheel);
        textView=findViewById(R.id.txtshow);
        GoToLogin = findViewById(R.id.btnGoToLogin);

        GoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToProfile();
            }
        });

        Collections.reverse(Arrays.asList(sectors));

        spinthewheel = findViewById(R.id.btnSpin);
        spinthewheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinWheel();
            }
        });


    }



    public void spinWheel()
    {
        Random rr=new Random();
        final int degree = rr.nextInt(360);

        RotateAnimation rotateAnimation=new RotateAnimation(0,degree+720,
                RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(3000);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

               // CalculatePoint(degree);
                openRestaurant();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        wheeling.startAnimation(rotateAnimation);
    }

    public void CalculatePoint(int degree)
    {
        //total degree 360|| 12 segment ||30 degree each segment
        int initialPoint=0;
        int endPoint=30;
        int i=0;
        String res=null;
        do{
            if(degree >initialPoint&& degree<endPoint){
                res=sectors[i];

            }
            initialPoint+=30;endPoint+=30;
            i++;
        }while (res==null);

        textView.setText(res);
    }
    public void openRestaurant() {
        Intent intent = new Intent(this, ShowRestaurant.class);
        startActivity(intent);
    }

    public void GoToProfile(){

        Intent intent = new Intent(this,Login.class);
        startActivity(intent);


    }






}