package com.example.jayshil.tdrinc.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jayshil.tdrinc.ContactUs.ContactUsFragment;
import com.example.jayshil.tdrinc.FindClub.FindClubFragment;
import com.example.jayshil.tdrinc.R;

public class ProfileActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.ic_profile_1:
                    transaction.replace(R.id.content, new ProfileFragment()).commit();
                    return true;

                case R.id.ic_club_1:
                    transaction.replace(R.id.content, new FindClubFragment()).commit();
                    return true;

                case R.id.ic_contact_1:
                    transaction.replace(R.id.content, new ContactUsFragment()).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, new ProfileFragment()).commit();
    }

}
