package com.example.amiramaulina.gpstrackerapptrial1;

import android.content.Context;
import android.content.Intent;;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ENumber extends AppCompatActivity {
    public static final String myPreference = "my__pref";
    EditText enumber;
    Button butt;
    TextView num0, num1, num2, num3, num4;
    String en0, en1, en2, en3, en4;
    Integer i;
    Number n = new Number();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enumber);

        enumber = (EditText) findViewById(R.id.editText2);
        butt = (Button) findViewById(R.id.button);
        num0 = (TextView) findViewById(R.id.num0);
        num1 = (TextView) findViewById(R.id.num1);
        num2 = (TextView) findViewById(R.id.num2);
        num3 = (TextView) findViewById(R.id.num3);
        num4 = (TextView) findViewById(R.id.num4);

        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        en0 = sharedPreferences.getString("number0", "");
        en1 = sharedPreferences.getString("number1", "");
        en2 = sharedPreferences.getString("number2", "");
        en3 = sharedPreferences.getString("number3", "");
        en4 = sharedPreferences.getString("number4", "");
        num0.setText(en0); num1.setText(en1);
        num2.setText(en2); num3.setText(en3);
        num4.setText(en4);

        butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if ((enumber.getText().toString().length() == 0)) {
                    Toast.makeText(getApplicationContext(), "You must fill an emergency number", Toast.LENGTH_SHORT).show();
                } else {
                    i = 0;
                    while (true) {
                        String check = sharedPreferences.getString("number"+i, "");
                        if (check.length() < 3) {
                            String m = enumber.getText().toString();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("number"+Integer.toString(i), m);
                            editor.apply();
                            n.setNum(i, m);
                            break;
                        }
                        if (i==5) {
                            Toast.makeText(getApplicationContext(), "Emergency Number List is Full", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        i++;
                    }
                    Intent myIntent = new Intent(ENumber.this,MainActivity.class);
                    startActivity(myIntent);
                }
            }
        });
    }

    public void del1(View view) {
        if (en0.length()>3) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("number0", " ");
            editor.apply();
            num0.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "No emergency number in this row", Toast.LENGTH_SHORT).show();
        }
    }

    public void del2(View view) {
        if (en1.length()>3) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("number1", " ");
            editor.apply();
            num1.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "No emergency number in this row", Toast.LENGTH_SHORT).show();
        }
    }

    public void del3(View view) {
        if (en2.length()>3) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("number2", " ");
            editor.apply();
            num2.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "No emergency number in this row", Toast.LENGTH_SHORT).show();
        }
    }

    public void del4(View view) {
        if (en3.length()>3) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("number3", " ");
            editor.apply();
            num3.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "No emergency number in this row", Toast.LENGTH_SHORT).show();
        }
    }

    public void del5(View view) {
        if (en4.length()>3) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("number4", " ");
            editor.apply();
            num4.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "No emergency number in this row", Toast.LENGTH_SHORT).show();
        }
    }
}