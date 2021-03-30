package shradha.com.finalloginsignupauthproj;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText log_EmailAddress;
    EditText log_Password;
    Button btn_LogIn;
    TextView log_Tv_ForgotPassword;
    Button log_Btn_Register;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log_EmailAddress = findViewById(R.id.log_EmailAddress);
        log_Password = findViewById(R.id.log_Password);
        btn_LogIn = findViewById(R.id.btn_LogIn);
        log_Tv_ForgotPassword = findViewById(R.id.log_Tv_ForgotPassword);
        log_Btn_Register = findViewById(R.id.log_Btn_Register);


    }
}