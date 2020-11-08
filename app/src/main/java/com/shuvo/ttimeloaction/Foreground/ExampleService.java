package com.shuvo.ttimeloaction.Foreground;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.shuvo.ttimeloaction.MainActivity;
import com.shuvo.ttimeloaction.R;
import com.shuvo.ttimeloaction.Volly.MySingleton;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.shuvo.ttimeloaction.Foreground.App.CHANNEL_ID;


public class ExampleService extends Service{
    private static final long START_TIME_IN_MILLIS = 15000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    Location mlocation;
    SupportMapFragment supportMapFragment;
    MapFragment mapFragment;
    String address;
    int Request_code = 101;
    FusedLocationProviderClient client;
    private GoogleMap mMap;
    String currentDate, currentTime,currentUser;
    FirebaseAuth auth;

    @Override
    public void onCreate() {


        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser().getUid();
        client = LocationServices.getFusedLocationProviderClient(this);






        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = intent.getStringExtra("inputExtra");

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        if (mTimerRunning) {
            pauseTimer();
        } else {
            startTimer();
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Start services")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_baseline_edit_location_24)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        return START_NOT_STICKY;
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                timeRetrun();
            }
        }.start();
        mTimerRunning = true;
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;

    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;

    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        Log.d(TAG, "updateCountDownText: " + timeLeftFormatted);
    }

    private void timeRetrun() {

        getCurrentLocation();
        resetTimer();
        currentDate();
        currentTime();
        startTimer();


    }

    private void currentTime() {
        Calendar calendar1=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm:ss a");
        currentTime=simpleDateFormat.format(calendar1.getTime());
    }

    private void currentDate() {
        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
    }


    private void getCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    mlocation = location;
                    String lotin = mlocation.getLatitude() + " \n" + mlocation.getLongitude();
                    LatLng latLng = new LatLng(mlocation.getLatitude(), mlocation.getLongitude());
                    String cityName = getCityName(latLng);
                    Log.d(TAG, "onSuccess: "+cityName);


                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private String getCityName(LatLng latLng) {

        String mycity = " ";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            mycity = addresses.get(0).getLocality();
            dataSave();
            Toast.makeText(this, address, Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

        }

        return mycity;
    }

    private void dataSave()
    {


        StringRequest request=new StringRequest(Request.Method.POST, "https://newhmanagement.000webhostapp.com/userLoactionSave.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(getApplicationContext(), "Location save successful", Toast.LENGTH_SHORT).show();
                address="";


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> data=new HashMap<>();
                data.put("firebase_id",currentUser);
                data.put("loaction",address);
                data.put("date",currentDate);
                data.put("time",currentTime);
                return data;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);



    }
}
