package com.example.kurgango;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class YoungHero extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_young_hero);

        setNavigation();
    }



    //Навигация
    private void setNavigation(){

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.younghero);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dialog:
                        startActivity(new Intent(getApplicationContext(), Dialog.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.younghero:
                        return true;
                }
                return false;
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int widthImg = 245;

        int margins = Integer.valueOf((width - (widthImg * 2)) / 3);

        try {
            JSONArray youngHeroesArr = new JSONArray(loadJSONFromAsset());
            LinearLayout listHero = (LinearLayout) findViewById(R.id.listHero);

            LinearLayout LineHero =  null;
            for(int i = 0; i < youngHeroesArr.length(); i++){
                //Каждый 2 элемент создавать новый
                if(i % 2 == 0){
                    LineHero = new LinearLayout(getApplicationContext());
                    LineHero.setOrientation(LinearLayout.HORIZONTAL);
                    LineHero.setPadding(margins, 20,0 ,20);
                }

                JSONObject youngHero = new JSONObject(youngHeroesArr.getString(i));

                Resources res = getResources();
                String mDrawableName = youngHero.getString("image");
                int resID = res.getIdentifier(mDrawableName , "mipmap", getPackageName());
                Drawable drawable = res.getDrawable(resID );

                ImageView image = new ImageView(getApplicationContext());
                image.setLayoutParams(new LinearLayout.LayoutParams(widthImg,300));
                image.setImageDrawable(drawable);



                TextView name = new TextView(getApplicationContext());
                name.setTextSize(16);
                name.setTypeface(null, Typeface.BOLD);
                name.setGravity(Gravity.CENTER);
                name.setMinHeight(100);
                name.setText(youngHero.getString("name"));



                LinearLayout HeroElement = new LinearLayout(getApplicationContext());
                HeroElement.setOrientation(LinearLayout.VERTICAL);
                HeroElement.setPadding(0, 0, margins, 0);

                HeroElement.addView(image);
                HeroElement.addView(name, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                final String id = String.valueOf(i);

                HeroElement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), YoungInfo.class).putExtra("id", id));

                    }
                });

                LineHero.addView(HeroElement);



                if(i % 2 == 1 || i == youngHeroesArr.length() - 1){
                    listHero.addView(LineHero, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }
            }
        }catch (JSONException ex){
            Log.d("ErrorGSON", ex.getMessage());
        }
    }


    public String loadJSONFromAsset() {
        String json = "";

        try{
            InputStream ims = getResources().openRawResource(R.raw.youngheroes);
            DataInputStream dis = new DataInputStream(ims);

            byte[] data = new byte[dis.available()];

            dis.read(data);
            dis.close();
            json = new String(data, "UTF-8");
        }catch (IOException e){
            Log.d("ErrorGSON", e.getMessage());
        }

        return json;
    }
}
