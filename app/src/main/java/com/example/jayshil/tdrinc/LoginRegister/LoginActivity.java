package com.example.jayshil.tdrinc.LoginRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jayshil.tdrinc.Profile.ProfileActivity;
import com.example.jayshil.tdrinc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText Email_log, Password_log;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email_log = (EditText)findViewById(R.id.email_login);
        Password_log = (EditText)findViewById(R.id.password_login);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!= null){

            finish();
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(intent);

        }
    }

    public void signinClick(View v){

        String email = Email_log.getText().toString().trim();
        String password = Password_log.getText().toString().trim();

        if(TextUtils.isEmpty(email)){

            Toast.makeText(LoginActivity.this, "Please enter the email", Toast.LENGTH_LONG).show();

        }
        else if(TextUtils.isEmpty(password)){

            Toast.makeText(LoginActivity.this, "Please enter the password",Toast.LENGTH_LONG).show();

            //-------------Login in user code Starts------------------
        }else {

            progressDialog.setMessage("Signing in....");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (task.isSuccessful()) {

                                try {

                                    if(user.isEmailVerified()){
                                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {

                                        Toast.makeText(LoginActivity.this, "Email is not verified, Please Verify your Email", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                        firebaseAuth.signOut();
                                    }

                                }catch (NullPointerException e){

                                }


                            } else {

                                Toast.makeText(LoginActivity.this, "Login error occurred", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }

                        }
                    });
        }
        //-------------Login in user code Ends------------------

    }



    public void register(View v){

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }
}
