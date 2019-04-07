package com.example.czatfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class StartChat extends AppCompatActivity {
    private static final String TAG = "StartChat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_chat);
        Log.d(TAG, "onCreate: started");
        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);

        //getResources().getDrawable(R.drawable.avatar,null);
        imageView2.setImageResource(R.drawable.avatar);
    }
}
