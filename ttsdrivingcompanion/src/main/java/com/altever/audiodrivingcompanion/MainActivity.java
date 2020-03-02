package com.altever.audiodrivingcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextToSpeech t1;
    EditText ed1;
    Button b1,b2;
    TextView unit;
   // SimpleLocation location;
    String coordinates[];
    Double lattitude;
    Double longtitude;

    private Object View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1=(EditText)findViewById(R.id.editText);
        b1=(Button)findViewById(R.id.buttonTTS);
        b2=(Button)findViewById(R.id.button2);
        unit=(TextView)findViewById(R.id.unitTextView);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        Toast.makeText(
                getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                String toSpeak = ed1.getText().toString() + " " + unit.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(unit.getText().toString() != "mph") {
                    unit.setText("mph");
                } else {
                    unit.setText("km/h");
                }
            }
        });

    }
}
