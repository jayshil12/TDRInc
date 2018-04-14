package com.example.jayshil.tdrinc.FindClub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jayshil.tdrinc.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FindClubFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private SearchableSpinner state_selector, county_selector;
    private SearchableSpinner city_selector;
    String url_county="http://thedancersresourceinc-com.stackstaging.com/getCounty.php?state=";
    String url_city="http://thedancersresourceinc-com.stackstaging.com/getCity.php?county=";
    private ProgressDialog dialog;
    ArrayList<String> county_list;
    ArrayList<String> city_list;
    String selected_state, selected_county, selected_city;
    Button find_club;

/* -----------------------------------Main_Method_Starts---------------------------------------------------------- */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//----Inflate the layout for this fragment--------------------------------

        final View v = inflater.inflate(R.layout.fragment_find_club, container, false);

//-------------------------Setting up JSON Object in the request que---------------------
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

//------------------------------------Widgets Definitions-------------------------------------------------------
        state_selector = (SearchableSpinner)v.findViewById(R.id.state_choice);
        county_selector = (SearchableSpinner)v.findViewById(R.id.county_choice);
        city_selector = (SearchableSpinner)v.findViewById(R.id.city_Spin_);
        find_club = (Button)v.findViewById(R.id.find_club_b);
        find_club.setEnabled(false);
        find_club.setOnClickListener(this);

//------------------------------------Array List Definitions------------------------------------------------------
        county_list = new ArrayList<>();
        city_list = new ArrayList<>();

//------------------------------------Array Adapter for states-------------------------------------------------------
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.states_, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_selector.setAdapter(adapter);

//------------------------------------OnClick Method for state spinner item----------------------------------------------
        state_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //-----Clearing County/City List------
                county_list.clear();

                //-------Getting Selected State as a string------------------------------
                selected_state = parent.getItemAtPosition(position).toString().trim();

                //-----------State selector url--------------
                String state_url = url_county + parent.getItemAtPosition(position).toString().trim().replaceAll(" ","%20");

                    //------Progress Dialog starts----------------
                    dialog = ProgressDialog.show(getActivity(), "Please Wait", "Getting Counties", false, false);
                    //-------------------------JSON Request starts for Getting County-------------------------
                    JsonObjectRequest objectRequest_county = new JsonObjectRequest(Request.Method.GET, state_url,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    dialog.dismiss();
                                    try {
                                        JSONArray array = response.getJSONArray("county");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject county = array.getJSONObject(i);

                                            String name = county.getString("county");

                                            county_list.add(name);

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getActivity(), "Error Fetching counties", Toast.LENGTH_LONG).show();
                            dialog.dismiss();

                        }
                    });

                    //-------------------------JSON Request ends for Getting County-------------------------

                    //-------------------------Array adapter for county spinner-----------------------------
                    ArrayAdapter<String> adapter_county = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, county_list);
                    adapter_county.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    county_selector.setAdapter(adapter_county);

                    requestQueue.add(objectRequest_county);


                }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

//------------------------------------OnClick Method for county spinner item----------------------------------------------
        county_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //-----Clearing City List------
                city_list.clear();

                //-------Getting Selected County as a string------------------------------
                selected_county = parent.getItemAtPosition(position).toString().trim();

                //-----------County selector url--------------
                String url_get_city = url_city+parent.getItemAtPosition(position).toString().trim().replaceAll(" ","%20");

                //------Progress Dialog starts----------------
                dialog = ProgressDialog.show(getActivity(), "Please Wait","Getting Cities",false,false);

                //-------------------------JSON Request starts for Getting City-------------------------
                JsonObjectRequest object_city = new JsonObjectRequest(Request.Method.GET, url_get_city,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                dialog.dismiss();
                                try {
                                    JSONArray city_array = response.getJSONArray("city");
                                    for(int i=0; i<city_array.length(); i++){

                                        JSONObject city = city_array.getJSONObject(i);
                                        String city_name = city.getString("city");

                                        city_list.add(city_name);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), "Error Fetching Cities", Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                });

                //-------------------------JSON Request ends for Getting City-------------------------


                //-------------------------Array adapter for city spinner-----------------------------
                ArrayAdapter<String> adpater_city = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, city_list);
                adpater_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                city_selector.setAdapter(adpater_city);
                adpater_city.notifyDataSetChanged();
                requestQueue.add(object_city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//------------------------------------OnClick Method for city spinner item----------------------------------------------
        city_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //-------Getting Selected City as a string------------------------------
                selected_city = parent.getItemAtPosition(position).toString().trim();
                find_club.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }



//------------------------------Button_Click_Method_Starts----------------------------------------------
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getActivity().getBaseContext(), ClubList.class);

        intent.putExtra("city_name", selected_city);
        startActivity(intent);
    }

}
