package com.example.kurgango;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DialogHero extends AppCompatActivity {

    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_hero);


        Intent intent = getIntent();
        String idExtra = intent.getStringExtra("id");
        id = Integer.valueOf( idExtra );
        try {
            JSONArray elements = new JSONArray(loadJSONFromAsset());
            JSONObject element = new JSONObject(elements.getString(id));

            Resources res = getResources();
            String mDrawableName = element.getString("image");
            int resID = res.getIdentifier(mDrawableName , "mipmap", getPackageName());
            Drawable drawable = res.getDrawable(resID );


            ImageView image = (ImageView) findViewById(R.id.imageHero);
            image.setImageDrawable(drawable);


        }
        catch (JSONException e){
            Log.d("Error GSON", e.getMessage());
        }

        render("start");
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

    private void render(String layout){
        try{
            JSONArray elements = new JSONArray(loadJSONFromAsset());

            JSONObject element = new JSONObject(elements.getString(id));
            JSONObject dialogs = new JSONObject(element.getString("dialog"));
            JSONObject dialog = new JSONObject(dialogs.getString(layout));

            TextView infoContent = (TextView) findViewById(R.id.infoContent);
            infoContent.setText(dialog.getString("info"));

            LinearLayout LayoutAnswer = (LinearLayout) findViewById(R.id.answer);
            LayoutAnswer.removeAllViews();
            JSONArray btnArray = new JSONArray(dialog.getString("btn"));

            for(int i = 0; i < btnArray.length(); i++){
                final JSONObject JSONbtn = new JSONObject(btnArray.getString(i));

                Button btn = new Button(getApplicationContext());
                btn.setText(JSONbtn.getString("name"));
                final String next = JSONbtn.getString("next");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        render(next);
                    }
                });
                LayoutAnswer.addView(btn);
            }


        }catch (JSONException ex){
            Log.d("Error GSON", ex.getMessage());
        }
    }
}
