package com.altever.audiodrivingcompanion.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import com.altever.audiodrivingcompanion.LocationAddress;
import com.altever.audiodrivingcompanion.MainActivity;
import com.altever.audiodrivingcompanion.R;
import com.altever.audiodrivingcompanion.database.DatabaseHelper;
import com.altever.audiodrivingcompanion.database.SharedPrefUtil;
import com.altever.audiodrivingcompanion.database.content_provider.ContentProviderSpeed;
import com.altever.audiodrivingcompanion.database.table.TableLocationLog;
import com.altever.audiodrivingcompanion.textToSpeech.TextSpeaker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.text.DecimalFormat;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.altever.audiodrivingcompanion.MainActivity.swichButton;
import static com.altever.audiodrivingcompanion.MainActivity.tvAddressValue;
import static com.altever.audiodrivingcompanion.MainActivity.tvLocationValue;
import static com.altever.audiodrivingcompanion.MainActivity.tvSpeedUnit;

public class LocationService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long INTERVAL = 10000 * 1;
    private static final long FASTEST_INTERVAL = 10000 * 1;

    private static final long INTERVAL_FIVE_MINUTE = 60000 * 1;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation, lStart, lEnd;
    static double distance = 0;
    double speed,speedMiles,locLat,locLong;
    Context context = this;
    TextSpeaker tts1;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    Cursor getSpeedCursor;
    DatabaseHelper db;
    TextToSpeech tts;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText("Location service started")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            serviceChannel.setSound(null,null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        distance = 0;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //MainActivity.locate.dismiss();
        mCurrentLocation = location;
        if (lStart == null) {
            lStart = mCurrentLocation;
            lEnd = mCurrentLocation;
        } else
            lEnd = mCurrentLocation;

        speed = location.getSpeed() * 18 / 5;
        speedMiles = location.getSpeed()*2.2369;

        locLat = location.getLatitude();
        locLong = location.getLongitude();

        if(speed>0.0) speed=speed;
        else speed = 0.0;

        if(speedMiles>0.0) speedMiles=speedMiles;
        else speedMiles = 0;

        updateUI();

        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(locLat, locLong,
                context, new GeocoderHandler());

        saveDataInDb();
    }

    @Override
    public void onCreate()
    {
        db = new DatabaseHelper(context);
        tts1 = new TextSpeaker(context);

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        checkSpeedFiveMinute();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    private void updateUI() {
        if (MainActivity.p == 0) {

            tvLocationValue.setText("Latitude : " + locLat + ", Longitude : "+locLong);

            if(tvSpeedUnit.getText().toString() == "mph")
            {
                MainActivity.tvSpeedValue.setText( new DecimalFormat("#.##").format(speedMiles));
            }
            else
            {
                MainActivity.tvSpeedValue.setText( new DecimalFormat("#.##").format(speed));
            }

            /*if (speed > 0.0)
                MainActivity.tvSpeedValue.setText( new DecimalFormat("#.##").format(speed));
            else
                MainActivity.tvSpeedValue.setText("0");*/
            lStart = lEnd;
        }
    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        lStart = null;
        lEnd = null;
        distance = 0;
        super.onDestroy();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            tvAddressValue.setText(locationAddress);
        }
    }

    private void saveDataInDb()
    {
        ContentValues values = new ContentValues();
        values.put(TableLocationLog.LATITUDE, locLat);
        values.put(TableLocationLog.LONGITUDE, locLong);
        values.put(TableLocationLog.SPEED, speed);
        values.put(TableLocationLog.MODESTATUS, SharedPrefUtil.getStatusMode(this, "SP_STATUS_MODE", "Off"));

        getContentResolver().insert(ContentProviderSpeed.ALL_SPEED_URI, values);

    }

    protected void checkSpeedFiveMinute() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    getSpeedCursor = db.getSpeedDetails();
                    if (getSpeedCursor != null && getSpeedCursor.moveToFirst())
                    {
                        if (getSpeedCursor.getCount() > 0)
                        {
                            Double sumOfSpeed = Double.parseDouble(getSpeedCursor.getString(getSpeedCursor.getColumnIndex("speed")));
                            Double countOfSpeed = Double.parseDouble(getSpeedCursor.getString(getSpeedCursor.getColumnIndex("count")));

                            Double avgSpeed = sumOfSpeed/countOfSpeed;

                            if(countOfSpeed>=6)
                            {
                                if(avgSpeed >= 0.5)
                                {
                                    if(!(SharedPrefUtil.getStatusMode(context,"SP_STATUS_MODE", "On").equals("On")))
                                    {
                                        SharedPrefUtil.setStatusMode(context,"SP_STATUS_MODE", "On");
                                        tts.speak("You are in Driving Mode", TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                    swichButton.setChecked(true);
                                }
                                else
                                {
                                    SharedPrefUtil.setStatusMode(context,"SP_STATUS_MODE", "Off");
                                    swichButton.setChecked(false);
                                }
                            }

                        }
                    }
                }
                catch (Exception e){

                }
                handler.postDelayed(this, INTERVAL_FIVE_MINUTE); //now is every 2 minutes
            }
        }, INTERVAL_FIVE_MINUTE);
    }

}
