package com.example.amiramaulina.gpstrackerapptrial1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class LiveMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    LatLng LatLng1, LatLng2;
    String latitude,longitude,name,ip,Image;
    Toolbar toolbar;
    Marker marker;
    Configuration conf = new Configuration();
    Handler handler;
    RequestQueue queue;

    ArrayList<String> mKeys;
    MarkerOptions myOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_map);

        toolbar = findViewById(R.id.toolbar22);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (intent != null) {
            name = extras.getString("name");
            ip = extras.getString("ip");
            latitude = extras.getString("latitude");
            longitude = extras.getString("longitude");
        }
        toolbar.setTitle(name + "'s Location");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                queue = Volley.newRequestQueue(LiveMapActivity.this);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://"+ip+".ngrok.io/location2", null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    latitude = response.getString("lat");
                                    longitude = response.getString("lon");
                                    LatLng2 = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                    marker.setPosition(LatLng2);
                                } catch (Exception e) { e.printStackTrace(); }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {error.printStackTrace();}
                });
                request.setTag("Map");
                queue.add(request);
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View row = getLayoutInflater().inflate(R.layout.custom_snippet,null);
                TextView nameTxt = row.findViewById(R.id.snippetName);
                TextView dateTxt = row.findViewById(R.id.snippetDate);
                nameTxt.setText(name);
                //dateTxt.setText(getDate());
                CircleImageView imageTxt = row.findViewById(R.id.snippetImage);
                Picasso.with(getApplicationContext()).load(Image).placeholder(R.drawable.defaultprofile).into(imageTxt);
                return row;
            }
        });
        LatLng1 = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
        MarkerOptions optionsnew = new MarkerOptions();
        optionsnew.position(LatLng1);
        optionsnew.title(name);
        optionsnew.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        marker = mMap.addMarker(optionsnew);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng1,15));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent myIntent = new Intent(LiveMapActivity.this, MenuActivityUser.class);
                startActivity(myIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }
}