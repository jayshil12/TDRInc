package com.example.jayshil.tdrinc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText Email, Password, Con_Password, U_Name;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

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

        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    public void signIn(View v){

        finish();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);

    }



    public void registerClick(View v){

        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String confirm_pass = Con_Password.getText().toString().trim();
        String name = U_Name.getText().toString().trim();

        final User_info user_info = new User_info(name);

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm_pass) || TextUtils.isEmpty(name)){

            Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_LONG).show();

        }else if(!(TextUtils.equals(password, confirm_pass))){

            Toast.makeText(RegisterActivity.this, "Passwords are not matching",Toast.LENGTH_LONG).show();

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
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                                startActivity(intent);

                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                databaseReference.child(user.getUid()).setValue(user_info);


                            }
                            else{

                                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();

                            }
                        }
                    });


        }

    }
}
