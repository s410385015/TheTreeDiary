
package com.example.nako.thetreediary.myClass;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.nako.thetreediary.R;
import com.example.nako.thetreediary.TreeSelect;

public class LoadGif extends AppCompatActivity {
    TextView textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_gif);
        final GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.growing);

        textview = (TextView) findViewById(R.id.LogoName);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "Font/wt034.ttf");
        textview.setTypeface(custom_font);

        delayText();
        gifImageView.setOnClickListener(action);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LoadGif.this, TreeSelect.class);
                startActivity(i);
                finish();
            }
        },0);
    }

   void delayText(){
        textview.setVisibility(View.INVISIBLE);
        textview.postDelayed(new Runnable() {
            @Override
            public void run() {
                textview.setVisibility(View.VISIBLE);
                textview.setText("小樹爬呀爬");
            }
        }, 18000);
       return;
    }

    private View.OnClickListener action = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(LoadGif.this, TreeSelect.class);
            startActivity(i);
            finish();
        }
    };
}