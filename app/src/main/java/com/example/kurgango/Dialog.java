package com.example.kurgango;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class Dialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        setNavigation();
    }


    //Навигация
    private void setNavigation(){

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dialog);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dialog:
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.younghero:
                        startActivity(new Intent(getApplicationContext(), YoungHero.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        try{
            JSONArray elements = new JSONArray(loadJSONFromAsset());

            for(int i = 0; i < elements.length(); i++){
                JSONObject heroes = new JSONObject(elements.getString(i));
                String fio = heroes.getString("fio");
                String mini_info = heroes.getString("mini_Info");
                String image = heroes.getString("image");
                String id = String.valueOf(i);

                generateHeroBlock(id, fio, mini_info, image);
            }
        }catch (JSONException ex){
            Log.d("Error GSON", ex.getMessage());
        }
    }


    private void generateHeroBlock(String id, String NameHero, String InfoHero, String imageHero){
        final String idHero = id;

        TextView FIO = new TextView(getApplicationContext());
        FIO.setText(NameHero);
        FIO.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        FIO.setTypeface(null, Typeface.BOLD);

        TextView miniInfo = new TextView(getApplicationContext());
        miniInfo.setText(InfoHero);

        /* Кнопка Начала разговора */
        Button btnInfo = new Button(getApplicationContext());
        btnInfo.setText("Начать разговор");

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DialogHero.class).putExtra("id", idHero));
            }
        });

        LinearLayout LayoutContent = new LinearLayout(getApplicationContext());

        LinearLayout.LayoutParams rightGravityParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rightGravityParams.gravity = Gravity.RIGHT;

        LayoutContent.setOrientation(LinearLayout.VERTICAL);
        LayoutContent.setPadding(10,10,10,2);
        LayoutContent.addView(FIO);
        LayoutContent.addView(miniInfo);
        LayoutContent.addView(btnInfo, rightGravityParams);

        Resources res = getResources();
        int resID = res.getIdentifier(imageHero , "mipmap", getPackageName());
        Drawable drawable = res.getDrawable(resID );

        ImageView image = new ImageView(getApplicationContext());
        image.setMinimumHeight(180);
        image.setImageDrawable(drawable);

        LinearLayout HeroBlock = new LinearLayout(getApplicationContext());
        HeroBlock.setPadding(0,10,0,0);
        HeroBlock.setOrientation(LinearLayout.HORIZONTAL);

        HeroBlock.addView(image);
        HeroBlock.addView(LayoutContent, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        LinearLayout HeroContent = (LinearLayout) findViewById(R.id.heroContent);
        HeroContent.addView(HeroBlock);
    }


    public String loadJSONFromAsset() {
        String json = "";

        try{
            InputStream ims = getResources().openRawResource(R.raw.data);
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
