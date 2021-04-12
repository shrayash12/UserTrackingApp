package shradha.com.finalloginsignupauthproj.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import shradha.com.finalloginsignupauthproj.R;

public class DisplayTrackingActivity extends AppCompatActivity {
    EditText et_Source;
    EditText et_Destination;
    Button btn_Map_Track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tracking);

        et_Source = findViewById(R.id.et_Source);
        et_Destination = findViewById(R.id.et_Destination);
        btn_Map_Track = findViewById(R.id.btn_Map_Track);

        btn_Map_Track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceLocation = et_Source.getText().toString();
                String destinationLocation = et_Destination.getText().toString();
                if (sourceLocation == "" && destinationLocation == ""){
                    Toast.makeText(DisplayTrackingActivity.this,"Enter both Locations",Toast.LENGTH_LONG).show();
                }
                else {
                    displayTrack(sourceLocation,destinationLocation);
                }
            }
        });
    }

    private void displayTrack(String sourceLocation, String destinationLocation) {
    }
}