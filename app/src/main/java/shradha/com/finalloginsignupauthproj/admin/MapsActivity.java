package shradha.com.finalloginsignupauthproj.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;



import android.util.Log;

import com.airbnb.lottie.model.Marker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import shradha.com.finalloginsignupauthproj.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng previousLatLng;
    LatLng currentLatLng;
    private Polyline polyline1;

    private List<LatLng> polylinePoints = new ArrayList<>();
    private Marker mCurrLocationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker  and move the camera
        polyline1 = mMap.addPolyline(new PolylineOptions().addAll(polylinePoints));
        fetchLocationUpdates();
    }

    private void fetchLocationUpdates() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("location").child("device1");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.i("tag", "New location updated:" + dataSnapshot.getKey());
                updateMap(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateMap(DataSnapshot dataSnapshot) {
        double latitude = 0, longitude = 0;

        Iterable<DataSnapshot> data = dataSnapshot.getChildren();
        for(DataSnapshot d: data){
            if(d.getKey().equals("latitude")){
                latitude = (Double) d.getValue();
            }else if(d.getKey().equals("longitude")){
                longitude = (Double) d.getValue();
            }
        }

        currentLatLng = new LatLng(latitude, longitude);

        if(previousLatLng ==null || previousLatLng != currentLatLng){
            // add marker line
            if(mMap!=null) {
                previousLatLng  = currentLatLng;
                polylinePoints.add(currentLatLng);
                polyline1.setPoints(polylinePoints);
                Log.w(MapsActivity.class.getSimpleName(), "Key:" + currentLatLng);
                mMap.addMarker(new MarkerOptions()
                        .position(currentLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .title("Delivery"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
            }

        }
    }
}