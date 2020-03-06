package com.altever.audiodrivingcompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.altever.audiodrivingcompanion.service.LocationService;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextToSpeech t1;
    SimpleLocation location;
    String coordinates[];
    Double latitude = 49.1212122;
    Double longitude = 121.1231223;

    LocationService myService;
    static boolean status;
    LocationManager locationManager;
    static long startTime;
    public static int p = 0;

    private Object View;
    public static TextView tvSpeed,tvSpeedValue,tvSpeedUnit,tvLocation,tvLocationValue,tvAddressValue;
    Button buttonTTSCurrentSpeed,buttonTTSCurrentLocation,buttonChangeUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grantLocationPermission();

        // ui components
        initUi();

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        buttonTTSCurrentSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                String toSpeak = tvSpeedValue.getText().toString() + " " + tvSpeedUnit.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        buttonTTSCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                String toSpeak = tvAddressValue.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        buttonChangeUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(tvSpeedUnit.getText().toString() != "mph") {
                    tvSpeedUnit.setText("mph");
                } else {
                    tvSpeedUnit.setText("km/h");
                }
            }
        });
    }

    public void initUi()
    {
        tvSpeed         = findViewById(R.id.tvSpeed);
        tvSpeedValue    = findViewById(R.id.tvSpeedValue);
        tvSpeedUnit     = findViewById(R.id.tvSpeedUnit);
        tvLocation      = findViewById(R.id.tvLocation);
        tvLocationValue = findViewById(R.id.tvLocationValue);
        tvAddressValue  = findViewById(R.id.tvAddressValue);

        buttonChangeUnit = findViewById(R.id.buttonChangeUnit);
        buttonTTSCurrentLocation = findViewById(R.id.buttonTTSCurrentLocation);
        buttonTTSCurrentSpeed = findViewById(R.id.buttonTTSCurrentSpeed);
    }

    public void grantLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        else{
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                return;
            }
            startService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            return;
                        }
                        startService();
                    }
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    public void startService() {
        Intent serviceIntent = new Intent(this, LocationService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, LocationService.class);
        stopService(serviceIntent);
    }

    @Override
    public void onDestroy(){
        stopService();
        super.onDestroy();
    }
}