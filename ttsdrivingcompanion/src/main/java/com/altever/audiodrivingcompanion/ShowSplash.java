package com.altever.audiodrivingcompanion;

import java.util.Timer;
import java.util.TimerTask;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ShowSplash extends AppCompatActivity {
   
    String coordinates[];
    Double lattitude;
    Double longtitude;
	 Timer t = new Timer();
    private Object View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
		TimerTask task= new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent in = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(in);
				finish();
			}
		};
	//	Timer t = new Timer();
		t.schedule(task,3000);
        
        Toast.makeText(
                getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();



    }
}
