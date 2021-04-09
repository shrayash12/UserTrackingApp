package shradha.com.finalloginsignupauthproj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class UserClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_client);
        Toast.makeText(this, "Welcome User", Toast.LENGTH_SHORT).show();
    }
}