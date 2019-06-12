package com.example.amiramaulina.gpstrackerapptrial1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class IPName extends AppCompatActivity {
    public static final String mypreference = "My_Pref";
    public static final String inputIP = "input_IP";
    public static final String inputName = "input_Name";
    EditText IP, Name;
    Button btnStore;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipage);

        IP = (EditText) findViewById(R.id.editIP);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches ("^\\d{1,4}(\\.(\\d{1,4}(\\.(\\d{1,4}(\\.(\\d{1,4})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i=0; i<splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }

        };
        IP.setFilters(filters);
        Name = (EditText) findViewById(R.id.editName);
        btnStore = (Button) findViewById(R.id.buttStore);

        btnStore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if ((IP.getText().toString().length() == 0) && (Name.getText().toString().length() == 0)) {
                    Toast.makeText(getApplicationContext(), "Please fill both forms", Toast.LENGTH_SHORT).show();
                } else if ((IP.getText().toString().length() == 0) && (Name.getText().toString().length() != 0)) {
                    Toast.makeText(getApplicationContext(), "Please fill your IP Address", Toast.LENGTH_SHORT).show();
                } else if ((Name.getText().toString().length() == 0) && (IP.getText().toString().length() != 0)) {
                    Toast.makeText(getApplicationContext(), "Please fill your Name", Toast.LENGTH_SHORT).show();
                } else {
                    sharedpreferences = getSharedPreferences(mypreference,
                            Context.MODE_PRIVATE);
                    String n = IP.getText().toString();
                    String e = Name.getText().toString();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(inputIP, n);
                    editor.putString(inputName, e);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Data Stored Successfuly!", Toast.LENGTH_SHORT).show();
                    // go to menu
                    Intent myIntent = new Intent(IPName.this, MainActivity.class);
                    startActivity(myIntent);
                }
            }
        });
    }
}