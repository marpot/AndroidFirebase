package com.example.czatfirebase;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity {

    public static int SIGN_IN_REQUEST_CODE = 4;
    private FirebaseListAdapter<ChatMessage> adapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private StorageReference mStorageRef;
    //zmiany
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        mStorageRef = FirebaseStorage.getInstance().getReference();

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

         FloatingActionButton fab =
                 (FloatingActionButton)findViewById(R.id.fab);

         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 EditText input = (EditText)findViewById(R.id.input);

                 // Read the input field and push a new instance
                 // of ChatMessage to the Firebase database
                 FirebaseDatabase.getInstance()
                         .getReference()
                         .push()
                         .setValue(new ChatMessage(input.getText().toString(),
                                 FirebaseAuth.getInstance()
                                         .getCurrentUser()
                                         .getDisplayName())
                         );

                 // Clear the input
                 input.setText("");
             }
         });
     }
    }


    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText(messageText));
                messageUser.setText(model.getMessageUser(messageUser));

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

   // @Override
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    public boolean OnOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.menu_sign_out){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this,
                            "You have been signed out.",
                            Toast.LENGTH_LONG).show();

                    //Close activity
                    finish();
                }
            });
        }
        return true;
    }


}
