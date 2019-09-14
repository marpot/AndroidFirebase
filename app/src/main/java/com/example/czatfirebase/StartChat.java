package com.example.czatfirebase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class StartChat extends AppCompatActivity {
    private static final String TAG = "StartChat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_chat);
        Log.d(TAG, "onCreate: started");
        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);

        //getResources().getDrawable(R.drawable.avatar,null);
        //imageView2.setImageResource(R.drawable.avatar);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getDisplayName())
                        );

                input.setText("");

            }
        });
    }
}
