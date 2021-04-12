package shradha.com.finalloginsignupauthproj.signin;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import shradha.com.finalloginsignupauthproj.forgetpassword.ForgotPasswordActivity;
import shradha.com.finalloginsignupauthproj.R;
import shradha.com.finalloginsignupauthproj.signup.SignUpActivity;
import shradha.com.finalloginsignupauthproj.user.UserActivity;
import shradha.com.finalloginsignupauthproj.model.UserManager;
import shradha.com.finalloginsignupauthproj.admin.AdminActivity;
import shradha.com.finalloginsignupauthproj.util.Constants;

public class SignInActivity extends AppCompatActivity {
    EditText log_EmailAddress;
    EditText log_Password;
    Button btn_LogIn;
    TextView log_Tv_ForgotPassword;
    TextView log_Btn_Register;
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        log_EmailAddress = findViewById(R.id.log_EmailAddress);
        log_Password = findViewById(R.id.log_Password);
        btn_LogIn = findViewById(R.id.btn_LogIn);
        log_Tv_ForgotPassword = findViewById(R.id.log_Tv_ForgotPassword);
        log_Btn_Register = findViewById(R.id.log_Btn_Register);

        if (!UserManager.getInstance(SignInActivity.this).getUserNameAndPassword().first.isEmpty()
                && !UserManager.getInstance(SignInActivity.this).getUserNameAndPassword().second.isEmpty()) {
            if (UserManager.getInstance(SignInActivity.this).isAdmin()) {
                startActivity(new Intent(SignInActivity.this, AdminActivity.class));
                finish();
            } else {
                startActivity(new Intent(SignInActivity.this, UserActivity.class));
                finish();
            }
        }
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        log_Btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.log_Btn_Register) {
                    startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                }
            }
        });
        log_Tv_ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
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

        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(SignInActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                UserManager.getInstance(SignInActivity.this).saveUserNamePassword(email, password);
                Toast.makeText(SignInActivity.this, "LogIn Successfully", Toast.LENGTH_SHORT).show();
                checkUserAccessLevel(authResult.getUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void checkUserAccessLevel(String uId) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DocumentReference documentReference = firestore.collection("User").document(firebaseUser.getUid());
        documentReference.get().addOnSuccessListener(SignInActivity.this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(SignInActivity.class.getSimpleName(), "SuccessFull" + documentSnapshot.getData());

                if (documentSnapshot.getData() != null) {
                    String isAdmin = (String) documentSnapshot.getData().get("isAdmin");
                    if (isAdmin != null && isAdmin.equals("1")) {
                        UserManager.getInstance(SignInActivity.this).saveIsAdmin(true);
                        startActivity(new Intent(SignInActivity.this, AdminActivity.class));
                    } else {
                        goToUserActivity(uId);
                    }
                } else {
                    goToUserActivity(uId);

                }
                finish();
            }
        });

    }

    private void goToUserActivity(String uuid) {
        UserManager.getInstance(SignInActivity.this).saveIsAdmin(false);
       Intent intent = new Intent(SignInActivity.this, UserActivity.class);
       intent.putExtra(Constants.KEY_UID,uuid);
        startActivity(intent);
    }

}
