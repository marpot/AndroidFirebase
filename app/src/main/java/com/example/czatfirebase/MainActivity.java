package com.example.czatfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static int SIGN_IN_REQUEST_CODE = 4;

    private FirebaseAnalytics mFirebaseAnalytics;
    //zmiany
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();

     if (FirebaseAuth.getInstance().getCurrentUser() == null){

         //Start sign in/sign up activity

         startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE); // ??
     } else {
         //User się zalogował. Wyświetlmy mu powitanie
         Toast.makeText(this,"Welcome" + FirebaseAuth.getInstance().
                 getCurrentUser().getDisplayName(),
                 Toast.LENGTH_LONG).show();
         //Load chat room contents
         displayChatMessages();
     }
    }

    private void displayChatMessages() {

    }

    @Override
    protected void onAcitivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Toast.makeText(this,"Successfully signed in! Welcome!",Toast.LENGTH_LONG).show();

                displayChatMessages();
            } else {
                Toast.makeText(this, "We couldn't sign you in. Please try again later",
                        Toast.LENGTH_LONG).show();

                //ZAMYKANIE APKI
                finish();
            }
        }
    }


}
