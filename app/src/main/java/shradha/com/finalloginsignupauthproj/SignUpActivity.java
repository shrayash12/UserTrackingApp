package shradha.com.finalloginsignupauthproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText reg_Name;
    EditText reg_Age;
    EditText reg_Email;
    EditText reg_Password;
    Button btn_SignUp;

    FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        User user;
        btn_SignUp = findViewById(R.id.btn_SignUp);
        firestore = FirebaseFirestore.getInstance();


        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegistration();
            }

        });
    }

    private void userRegistration() {
        mAuth = FirebaseAuth.getInstance();
        reg_Name = findViewById(R.id.reg_Name);
        reg_Age = findViewById(R.id.reg_Age);
        reg_Email = findViewById(R.id.reg_Email);
        reg_Password = findViewById(R.id.reg_Password);

        String userName = reg_Name.getText().toString();
        String userAge = reg_Age.getText().toString();
        String userEmail = reg_Email.getText().toString();
        String userPassword = reg_Password.getText().toString();

        if (userName.isEmpty()) {
            reg_Name.setError("full name is require");
            reg_Name.requestFocus();
            return;
        }
        if (userAge.isEmpty()) {
            reg_Age.setError("Age is require");
            reg_Age.requestFocus();
            return;
        }
        if (userEmail.isEmpty()) {
            reg_Email.setError("Email is require");
            reg_Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            reg_Email.setError("Please provide valid Email");
            reg_Email.requestFocus();
            return;
        }

        if (userPassword.isEmpty()) {
            reg_Password.setError("Password is require");
            reg_Password.requestFocus();
            return;
        }
        if (userPassword.length() < 6) {
            reg_Password.setError("Minimum password length should be 6 characters");
            reg_Password.requestFocus();
            return;
        }

        // Log.d(SignUpActivity.class.getSimpleName(), "SignUp Button Clicked");
        mAuth.createUserWithEmailAndPassword(reg_Email.getText().toString(),
                reg_Password.getText().toString()).addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                DocumentReference documentReference = firestore.collection("User").document(firebaseUser.getUid());
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("UseName", reg_Name.getText().toString());
                userInfo.put("UseAge", reg_Age.getText().toString());
                userInfo.put("UserEmail", reg_Email.getText().toString());
                userInfo.put("User", 1);
                documentReference.set(userInfo);
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Fail to create account", Toast.LENGTH_SHORT).show();
            }
        });

    }


}