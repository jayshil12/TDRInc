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

import com.example.jayshil.tdrinc.Profile.Image_Upload;
import com.example.jayshil.tdrinc.Profile.ProfileActivity;
import com.example.jayshil.tdrinc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText Email, Password, Con_Password, U_Name;
    private ProgressDialog progressDialog;
    final String defaultImageUrl ="https://firebasestorage.googleapis.com/v0/b/tdr-inc-4c9a7.appspot.com/o/Camera-icon.png?alt=media&token=e0f1240b-da4f-45c0-8b0c-6c8f30f7bd5e";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference ref2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Email = (EditText)findViewById(R.id.email_register);
        Password = (EditText)findViewById(R.id.password_register);
        Con_Password = (EditText)findViewById(R.id.con_pass_register);
        U_Name = (EditText)findViewById(R.id.name_register);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("userinfo");
        ref2 = FirebaseDatabase.getInstance().getReference("userProfileImages");
        if(user != null){

            firebaseAuth.signOut();

        }

    }

    public void signIn(View v){

        finish();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);

    }



    public void registerClick(View v){

        //-------Check Credentials code Starts---------------------
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String confirm_pass = Con_Password.getText().toString().trim();
        String name = U_Name.getText().toString().trim();

        final User_info user_info = new User_info(name);
        final Image_Upload image1 = new Image_Upload(defaultImageUrl);

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm_pass) || TextUtils.isEmpty(name)){

            Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_LONG).show();

        }else if(!(TextUtils.equals(password, confirm_pass))){

            Toast.makeText(RegisterActivity.this, "Passwords are not matching",Toast.LENGTH_LONG).show();
            //----------Check Credential Code Ends------------

            //------------register new user code starts------------------
        }else{

            progressDialog.setMessage("Registering User....");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                finish();
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Registration Successful \n Sending Verification Email", Toast.LENGTH_LONG).show();
                                /*Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent1);*/

                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                //---------Save User Information---------------
                                databaseReference.child(user.getUid()).setValue(user_info);
                                ref2.child(user.getUid()).setValue(image1);

                                //------Email Verification code starts------------
                                if(user != null){

                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()){

                                                        Toast.makeText(RegisterActivity.this, "Please Verify your email", Toast.LENGTH_LONG).show();

                                                        firebaseAuth.signOut();

                                                    }else {

                                                        Toast.makeText(RegisterActivity.this, "Verification Email is not successfully sent", Toast.LENGTH_LONG).show();

                                                    }

                                                }
                                            });
                                }
                                //------Email Verification Code Ends----------------
                            }
                            else{

                                Toast.makeText(RegisterActivity.this, "Register Error", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }
                        }
                    });


        }
        //----------Register New user Code Ends------------------
    }

}
