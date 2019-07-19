package com.example.a10017078.bananaclicker;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.invoke.ConstantCallSite;

public class MainActivity extends AppCompatActivity {

    static ImageView img, monkey;
    static TextView bview, monkeyCount, textView2;
    static int count;
    static int rate;
    static ScaleAnimation scaleAnimation;
    static ConstraintLayout layout;
    static boolean boothang;
    static int m;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.id_img);
        bview = findViewById(R.id.id_bview);
        monkeyCount = findViewById(R.id.id_monkeyCount);
        textView2 = findViewById(R.id.textView2);
        m = 0;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        count = preferences.getInt("count", 0);
        rate = 0;
        layout = findViewById(R.id.id_layout);
        img.setImageResource(R.drawable.bananabunch);
        scaleAnimation = new ScaleAnimation(.8f, 1.0f, .8f, 1.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        scaleAnimation.setDuration(50);
        boothang = true;

        new MyThread().start();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img.startAnimation(scaleAnimation);
                count++;
                bview.setText("" + count);

                TranslateAnimation animation = new TranslateAnimation(-150, 0, 0, -550);
                animation.setDuration(500);

                final ImageView popimg = new ImageView(MainActivity.this);
                popimg.setId(View.generateViewId());
                popimg.setImageResource(R.drawable.banana);
                popimg.setScaleX((float) .1);
                popimg.setScaleY((float) .1);
                popimg.setX((float) Math.random() * 230);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layout.removeView(popimg);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                layout.addView(popimg);
                popimg.startAnimation(animation);
                popimg.setVisibility(View.INVISIBLE);


            }
        });

    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("count", count);
        editor.commit();
        super.onPause();
    }

    public class MyThread extends Thread{
        @Override
        public void run(){

            while(true) {
                addMethod();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private synchronized void addMethod() {
        count+=rate;
        bview.post(new Runnable() {
            @Override
            public void run() {
                bview.setText(""+count);
                monkeyCount.setText("You have " + m + " monkey(s)");

            }
        });
       checkMethod();
    }

    private void checkMethod() {
        if (count >= (30+rate)) {

            if(boothang) {
                monkey = new ImageView(getApplicationContext());
                monkey.setId(View.generateViewId());
                monkey.setImageResource(R.drawable.monkey);
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                monkey.setLayoutParams(lp);

                layout.post(new Runnable() {
                    @Override
                    public void run() {
                        layout.addView(monkey);
                        monkey.setScaleX(.2f);
                        monkey.setScaleY(.2f);

                        AlphaAnimation fade = new AlphaAnimation(0f, 1.0f);
                        fade.setDuration(800);
                        fade.setFillAfter(true);
                        monkey.startAnimation(fade);
                        monkey.setLeft(1);

                        ConstraintSet cs = new ConstraintSet();
                        cs.clone(layout);
                        cs.connect(monkey.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, 0);
                        cs.connect(monkey.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 0);
                        cs.applyTo(layout);
                    }
                });

                boothang = false;
            }

            monkey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m++;
                    AlphaAnimation fadeout = new AlphaAnimation(1.0f, 0f);
                    fadeout.setDuration(600);
                    monkey.startAnimation(fadeout);
                    monkey.setEnabled(false);

                    fadeout.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            layout.post(new Runnable() {
                                @Override
                                public void run() {
                                    layout.removeView(monkey);
                                    boothang = true;
                                }
                            });

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    count -= (30 + rate);
                    rate += 3;

                    textView2.post(new Runnable() {
                        @Override
                        public void run() {
                            textView2.setText("A monkey costs " + (30+rate) + " bananas");
                        }
                    });

                    final ConstraintLayout cl = findViewById(R.id.id_layout2);
                    final ImageView iv = new ImageView(getApplicationContext());
                    iv.setImageResource(R.drawable.monkey);
                    iv.setId(View.generateViewId());
                    
                    cl.post(new Runnable() {
                        @Override
                        public void run() {
                            cl.addView(iv);

                            ConstraintSet cs = new ConstraintSet();
                            cs.clone(cl);
                            int leftMargin = (int) (Math.random()*300);
                            int topMargin = (int) (Math.random()*50);
                            cs.connect(iv.getId(), ConstraintSet.LEFT, cl.getId(), ConstraintSet.LEFT, leftMargin);
                            cs.connect(iv.getId(), ConstraintSet.TOP, cl.getId(), ConstraintSet.TOP, topMargin);
                            cs.applyTo(cl);
                        }
                    });

                }
            });
        }
    }

}