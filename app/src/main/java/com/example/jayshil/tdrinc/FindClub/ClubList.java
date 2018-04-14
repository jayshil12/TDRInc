package com.example.jayshil.tdrinc.FindClub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jayshil.tdrinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.jayshil.tdrinc.R.id.image;
import static com.example.jayshil.tdrinc.R.id.textView;

public class ClubList extends AppCompatActivity {

    String url_club="http://thedancersresourceinc-com.stackstaging.com/getClub.php?city=";
    String city;
    String name;
    private ProgressDialog dialog;
    RequestQueue queue;
    int[] image_id = {R.drawable.club_test};
    ArrayList<club_image_name> club_list = new ArrayList<club_image_name>();
    TextView clubName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);
        Intent intent = getIntent();
        city = intent.getStringExtra("city_name");
        dialog = ProgressDialog.show(this, "Please Wait", "Getting Clubs", false, false);
        String get_club_url = url_club+city.replaceAll(" ","%20");
        final TextView textView = (TextView)findViewById(R.id.club_name);
        queue = Volley.newRequestQueue(this);
        clubName = (TextView)findViewById(R.id.textView5);

        JsonObjectRequest objectRequest_clubs = new JsonObjectRequest(Request.Method.GET, get_club_url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        try {
                            JSONArray array = response.getJSONArray("clubs");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject clubs = array.getJSONObject(i);

                                name = clubs.getString("club");
                                //club_image_name object = new club_image_name(image_id[i], name);
                                //club_list.add(object);
                                clubName.setText(name);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ClubList.this,"Error getting clubs", Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });


        //clubName.setText(name);
        queue.add(objectRequest_clubs);
    }
}
