package com.example.jayshil.tdrinc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private String user_ID;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        text = (TextView)findViewById(R.id.textView4);
        firebaseAuth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        user_ID = user.getUid();

        if(firebaseAuth.getCurrentUser()==null){

            finish();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);

        }


        text.setText("Welcome "+ user.getEmail());

    }


    public void signOut(View v){

        firebaseAuth.signOut();
        finish();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);

    }
}
