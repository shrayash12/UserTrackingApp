package shradha.com.finalloginsignupauthproj.admin;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LocationTrackingService  extends Service {
    private FusedLocationProviderClient mLocationProviderClient;
    private LocationCallback locationUpdatesCallback;
    private LocationRequest locationRequest;
    public LocationTrackingService() {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setUpLocationRequest();
    }


    private void setUpLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String keyValue = intent.getStringExtra("key");
        if(keyValue!=null && keyValue.equals("stop")){
            stopSelf();
        }else {
            setUpLocationUpdatesCallback();
            mLocationProviderClient.requestLocationUpdates(locationRequest, locationUpdatesCallback, null);
        }
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationNotification.cancel(this);
        mLocationProviderClient.removeLocationUpdates(locationUpdatesCallback);
    }
    private void setUpLocationUpdatesCallback() {
        locationUpdatesCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult!=null){

                    Location lastLocation = locationResult.getLastLocation();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("location").child("device1");


                    Map<String, Object> data = new HashMap<>();
                    data.put("latitude", lastLocation.getLatitude());
                    data.put("longitude", lastLocation.getLongitude());
                    data.put("time", lastLocation.getTime());
                    ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //
                            Log.i("tag", "Location update saved");
                        }
                    });
                    LocationNotification.notify(LocationTrackingService.this, "Location Tracking",
                            "Lat:" + lastLocation.getLatitude() + " - Lng:" + lastLocation.getLongitude());
                }else{
                    Log.i("tag", "Location null");
                }
            }
        };
    }
}