package shradha.com.finalloginsignupauthproj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText resetPassword_Email;
    Button btn_ResetPassword;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        resetPassword_Email = findViewById(R.id.resetPassword_Email);
        btn_ResetPassword = findViewById(R.id.btn_ResetPassword);
    }
}