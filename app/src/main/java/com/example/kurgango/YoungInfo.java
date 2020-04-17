package com.example.kurgango;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class YoungInfo extends AppCompatActivity {

    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_young_info);

        Intent intent = getIntent();
        String idExtra = intent.getStringExtra("id");
        id = Integer.valueOf( idExtra );

        try{
            JSONArray youngHeroArr = new JSONArray(loadJSONFromAsset());
            JSONObject youngHeroObj = new JSONObject(youngHeroArr.getString(id));

            TextView FIO = (TextView) findViewById(R.id.Fio);
            FIO.setText(youngHeroObj.getString("name"));

            TextView info = (TextView) findViewById(R.id.infoHero);
            info.setText(youngHeroObj.getString("info"));

            Resources res = getResources();
            String mDrawableName = youngHeroObj.getString("fullImage");
            int resID = res.getIdentifier(mDrawableName , "mipmap", getPackageName());
            Drawable drawable = res.getDrawable(resID );

            ImageView image = (ImageView) findViewById(R.id.youngImage);
            image.setImageDrawable(drawable);


        }catch (JSONException ex){
            Log.d("Error GSON", ex.getMessage());
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
