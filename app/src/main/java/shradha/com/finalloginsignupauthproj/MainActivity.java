package shradha.com.finalloginsignupauthproj;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    EditText log_EmailAddress;
    EditText log_Password;
    Button btn_LogIn;
    TextView log_Tv_ForgotPassword;
    Button log_Btn_Register;
    ProgressBar log_ProgressBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log_EmailAddress = findViewById(R.id.log_EmailAddress);
        log_Password = findViewById(R.id.log_Password);
        btn_LogIn = findViewById(R.id.btn_LogIn);
        log_Tv_ForgotPassword = findViewById(R.id.log_Tv_ForgotPassword);
        log_Btn_Register = findViewById(R.id.log_Btn_Register);
        log_ProgressBar = findViewById(R.id.log_ProgressBar);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
                // Log.d(MainActivity.class.getSimpleName(), "btn_LogIn Clicked");
            }
        });

        log_Btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.log_Btn_Register:
                        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                        //Log.d(MainActivity.class.getSimpleName(), "btn Register clicked");
                        break;
                }
            }
        });
        log_Tv_ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void signInUser() {
        String email = log_EmailAddress.getText().toString();
        String password = log_Password.getText().toString();
        if (email.isEmpty()) {
            log_EmailAddress.setError("Email is require");
            log_EmailAddress.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            log_EmailAddress.setError("please enter valid email");
            log_EmailAddress.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            log_Password.setError("Password is require");
            log_Password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            log_Password.setError("Minimum password length should be 6 characters");
            log_Password.requestFocus();
            return;
        }

        log_ProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(MainActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "LogIn Successfully", Toast.LENGTH_SHORT).show();
                checkUserAccessLevel(authResult.getUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void checkUserAccessLevel(String uId) {
        DocumentReference documentReference = firestore.collection("User").document(firebaseUser.getUid());
        documentReference.get().addOnSuccessListener(MainActivity.this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(MainActivity.class.getSimpleName(), "SuccessFull"+documentSnapshot.getData());
                if (documentSnapshot.getString("Admin") != null) ;
                //user is Admin
                startActivity(new Intent(MainActivity.this,Admin.class));
                finish();
                if (documentSnapshot.getString("user")!= null){
                    // this is User
                    startActivity(new Intent(MainActivity.this,UserProfile.class));

                }
            }
        });
    }

}
