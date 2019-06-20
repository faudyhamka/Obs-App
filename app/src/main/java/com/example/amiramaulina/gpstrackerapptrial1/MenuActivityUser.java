package com.example.amiramaulina.gpstrackerapptrial1;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class MenuActivityUser extends AppCompatActivity {
    public static final String mypreference = "My_Pref";
    public static final String myPreference = "my__pref";
    public static final String inputIP = "input_IP";
    public static final String inputName = "input_Name";
    Toolbar toolbar;
    String latitude,longitude,name,ip;
    String fallstateTimestamp, hstateValueTimestamp;
    String fallcheck, hrcheck;
    Handler handler;
    RequestQueue queue;
    TextView f, hr;
    SharedPreferences sharedpreferences, sharedPreferences;
    public String eN1, eN2, eN3, eN4, eN5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        queue = Volley.newRequestQueue(this);
        f = (TextView) findViewById(R.id.ts1);
        hr = (TextView) findViewById(R.id.ts2);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);
        ip = sharedpreferences.getString(inputIP, "");
        name = sharedpreferences.getString(inputName, "");
        eN1 = sharedPreferences.getString("number0", "");
        eN2 = sharedPreferences.getString("number1", "");
        eN3 = sharedPreferences.getString("number2", "");
        eN4 = sharedPreferences.getString("number3", "");
        eN5 = sharedPreferences.getString("number4", "");
    }

    @Override
    protected void onStart() {
        final ProgressBar pbar = (ProgressBar) findViewById(R.id.pb);
        pbar.setVisibility(View.VISIBLE);
        super.onStart();
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                GETB("http://"+ip+ ":3000/fstate2");
                GETC("http://"+ip+ ":3000/hstate2");
                GETA("http://"+ip+ ":3000/location2");
                if (fallstateTimestamp != null) {
                    if (fallcheck == null) {
                        fallcheck = fallstateTimestamp;
                        Log.i("tes", "tes fallstring " + fallcheck);
                        f.setText(fallstateTimestamp);
                    } else if (!fallstateTimestamp.equals(fallcheck)) {
                        Log.i("tes", "tes fallcheck akhir " + fallcheck);
                        Log.i("tes", "tes fall timestamp " + fallstateTimestamp);
                        showNotificationFall();
                        fallcheck = fallstateTimestamp;
                        f.setText(fallstateTimestamp);
                    }
                }
                if (hstateValueTimestamp != null) {
                    if (hrcheck == null) {
                        hrcheck = hstateValueTimestamp;
                        Log.i("tes", "tes hrstring " + hrcheck);
                        hr.setText(hstateValueTimestamp);
                    } else if (!hstateValueTimestamp.equals(hrcheck)) {
                        Log.i("tes", "tes hrcheck akhir " + hrcheck);
                        Log.i("tes", "tes hr timestamp " + hstateValueTimestamp);
                        showNotificationHR();
                        hrcheck = hstateValueTimestamp;
                        hr.setText(hstateValueTimestamp);
                    }
                }
                handler.postDelayed(this, 1000);
                pbar.setVisibility(View.GONE);
            }
        }, 1000);
    }

    public void GETA(String URLA) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URLA, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            latitude = response.getString("lat");
                            longitude = response.getString("lon");
                        } catch (Exception e) { e.printStackTrace(); }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setTag("Menu");
        queue.add(request);
    }

    public void GETB(String URLA) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URLA, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            fallstateTimestamp = response.getString("fs");
                        } catch (Exception e) { e.printStackTrace(); }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setTag("Menu");
        queue.add(request);
    }

    public void GETC(String URLA) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URLA, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            hstateValueTimestamp = response.getString("hs");
                            if (hstateValueTimestamp.length() > 8) {
                                hstateValueTimestamp = hstateValueTimestamp.split("H")[1];
                            }
                        } catch (Exception e) { e.printStackTrace(); }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setTag("Menu");
        queue.add(request);
    }

    public void showNotificationFall() {
        final NotificationManager mgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder note = new NotificationCompat.Builder(getApplicationContext());
        note.setContentTitle("FALL DETECTED ON YOUR FAMILY MEMBER!");
        note.setContentText("Click here to see their location!");
        note.setTicker("FALL DETECTED ON YOUR FAMILY MEMBER!");
        note.setAutoCancel(true);
        note.setPriority(Notification.PRIORITY_HIGH);
        note.setVibrate(new long[] {0, 100, 100, 100});
        note.setDefaults(Notification.DEFAULT_SOUND);
        note.setSmallIcon(R.mipmap.ic_launcher);
        Intent notificationIntent = new Intent(getApplicationContext(), LiveMapActivity.class);
        notificationIntent.putExtra("latitude", latitude);
        notificationIntent.putExtra("longitude", longitude);
        notificationIntent.putExtra("name", name);
        notificationIntent.putExtra("ip", ip);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        note.setContentIntent(pi);
        mgr.notify(692030, note.build());
    }

    public void showNotificationHR() {
        final NotificationManager mgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder note = new NotificationCompat.Builder(getApplicationContext());
        note.setContentTitle("HEART RATE ABNORMALITY ON YOUR FAMILY MEMBER!");
        note.setContentText("Click here to check their location!");
        note.setTicker("HEART RATE ABNORMALITY ON YOUR FAMILY MEMBER!");
        note.setAutoCancel(true);
        note.setPriority(Notification.PRIORITY_HIGH);
        note.setVibrate(new long[] {0, 100, 100, 100});
        note.setDefaults(Notification.DEFAULT_SOUND);
        note.setSmallIcon(R.mipmap.ic_launcher);
        Intent notificationIntent = new Intent(getApplicationContext(), LiveMapActivity.class);
        notificationIntent.putExtra("latitude", latitude);
        notificationIntent.putExtra("longitude", longitude);
        notificationIntent.putExtra("name", name);
        notificationIntent.putExtra("ip", ip);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        note.setContentIntent(pi);
        mgr.notify(692030, note.build());
    }

    public void gotoLocation(View v){
        Intent myIntent = new Intent(MenuActivityUser.this,LiveMapActivity.class);
        myIntent.putExtra("latitude", latitude);
        myIntent.putExtra("longitude", longitude);
        myIntent.putExtra("name", name);
        myIntent.putExtra("ip", ip);
        startActivity(myIntent);
    }

    //grafik hrstate
    public void gotoHRStateHistory(View v){
        Intent myIntent = new Intent(MenuActivityUser.this,HRStateHistory.class);
        myIntent.putExtra("name", name);
        myIntent.putExtra("ip", ip);
        startActivity(myIntent);
    }

    //grafik fstate
    public void gotoFallHistory(View v){
        Intent myIntent = new Intent(MenuActivityUser.this,FStateHistory.class);
        myIntent.putExtra("name", name);
        myIntent.putExtra("ip", ip);
        startActivity(myIntent);
    }

    // grafik hrvalue
    public void gotoHRStatistics(View v){
        Intent myIntent = new Intent(MenuActivityUser.this,HRStatistics.class);
        myIntent.putExtra("name", name);
        myIntent.putExtra("ip", ip);
        startActivity(myIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }
}