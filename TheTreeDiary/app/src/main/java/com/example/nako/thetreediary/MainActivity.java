package com.example.nako.thetreediary;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.nako.thetreediary.myClass.GifImageView;
import com.example.nako.thetreediary.myClass.LoadGif;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textview;
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_gif);
        final GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.open);

        textview = (TextView) findViewById(R.id.LogoName);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "Font/wt034.ttf");
        textview.setTypeface(custom_font);

        delayText1();
        delayText2();
        delayText3();
        delayText4();
        delayText();
        gifImageView.setOnClickListener(action);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!flag) {
                    Intent i = new Intent(MainActivity.this, TreeSelect.class);
                    startActivity(i);
                    finish();
                }
            }
        },6500);
        /*
        Intent intent = new Intent();
        intent.setClass(this,TreeSelect.class);
        finish();
        startActivity(intent)
        */;
    }

    void delayText1(){
        textview.setVisibility(View.INVISIBLE);
        textview.postDelayed(new Runnable() {
            @Override
            public void run() {
                textview.setVisibility(View.VISIBLE);
                textview.setText("小");
            }
        }, 0);
        return;
    }

    void delayText2(){
        textview.setVisibility(View.INVISIBLE);
        textview.postDelayed(new Runnable() {
            @Override
            public void run() {
                textview.setVisibility(View.VISIBLE);
                textview.setText("小樹");
            }
        }, 1500);
        return;
    }
    void delayText3(){
        textview.setVisibility(View.INVISIBLE);
        textview.postDelayed(new Runnable() {
            @Override
            public void run() {
                textview.setVisibility(View.VISIBLE);
                textview.setText("小樹爬");
            }
        },3000);
        return;
    }

    void delayText4(){
        textview.setVisibility(View.INVISIBLE);
        textview.postDelayed(new Runnable() {
            @Override
            public void run() {
                textview.setVisibility(View.VISIBLE);
                textview.setText("小樹爬芽");
            }
        }, 4500);
        return;
    }

    void delayText(){
        textview.setVisibility(View.INVISIBLE);
        textview.postDelayed(new Runnable() {
            @Override
            public void run() {
                textview.setVisibility(View.VISIBLE);
                textview.setText("小樹爬芽爬");
            }
        }, 6000);
        return;
    }

    private View.OnClickListener action = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            flag=true;
            Intent i = new Intent(MainActivity.this, TreeSelect.class);
            startActivity(i);
            finish();
        }
    };
}
