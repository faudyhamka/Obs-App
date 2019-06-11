package com.example.amiramaulina.gpstrackerapptrial1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String mypreference = "My_Pref";
    public static final String inputIP = "input_IP";
    public static final String inputName = "input_Name";
    Configuration conf = new Configuration();
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        String ip = sharedpreferences.getString(inputIP, "");
        String name = sharedpreferences.getString(inputName, "");
        conf.setIP(ip); conf.setName(name);
    }

    public void goToMenuActivity(View view) {
        if ((conf.getName() != null) && (conf.getIP() != null)) {
            Intent myintent = new Intent(MainActivity.this, MenuActivityUser.class);
            startActivity(myintent);
        } else {
            Toast.makeText(getApplicationContext(), "Please go to configuration page first", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToConfig(View view) {
        Intent myIntent = new Intent(MainActivity.this, IPName.class);
        startActivity(myIntent);
    }

    public void goToENum(View view) {
        Intent myIntent = new Intent(MainActivity.this, ENumber.class);
        startActivity(myIntent);
    }
}