package shradha.com.finalloginsignupauthproj.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import shradha.com.finalloginsignupauthproj.admin.LocationTrackingService;
import shradha.com.finalloginsignupauthproj.util.Constants;
import shradha.com.finalloginsignupauthproj.R;
import shradha.com.finalloginsignupauthproj.model.UserManager;
import shradha.com.finalloginsignupauthproj.signin.SignInActivity;

public class UserActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore fireStore;
    TextView tv_UserName;
    Button btn_User_LogOut;
    LottieAnimationView lottieAnimationView;
    LinearLayout shiftLayout;
    Button shiftButton;
    String uuid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uuid = getIntent().getStringExtra(Constants.KEY_UID);

        setContentView(R.layout.activity_user);
        tv_UserName = findViewById(R.id.tv_UserName);
        btn_User_LogOut = findViewById(R.id.btn_User_LogOut);
        lottieAnimationView = findViewById(R.id.animationView);
        shiftLayout = findViewById(R.id.shiftLayout);
        shiftButton = findViewById(R.id.shiftBUtton);

        if(UserManager.getInstance(UserActivity.this).isShiftStarted()) {
            lottieAnimationView.setVisibility(View.VISIBLE);
            shiftLayout.setVisibility(View.GONE);
        }

        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DocumentReference documentReference = fireStore.collection("User").document(firebaseUser.getUid());

        documentReference.get().addOnCompleteListener(UserActivity.this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (task.isSuccessful()) {
                    if (documentSnapshot.exists()) {

                        String userName = documentSnapshot.getString(Constants.KEY_NAME);
                        String userEmail = documentSnapshot.getString(Constants.KEY_EMAIL);

                        Intent intent = new Intent(UserActivity.this, LocationTrackingService.class);
                        intent.putExtra(Constants.KEY_EMAIL,userEmail);
                        intent.putExtra(Constants.KEY_UID,uuid);
                        startService(intent);
                        String userAge = documentSnapshot.getString(Constants.KEY_AGE);
                        tv_UserName.setText("Hello " + userName + ", Welcome to Scarlet It Solution");
                    } else {
                        Log.d(UserActivity.class.getSimpleName(), "No such document");
                    }
                } else {
                    Log.d(UserActivity.class.getSimpleName(), "get failed with ", task.getException());

                }
            }
        }).addOnFailureListener(UserActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        btn_User_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(UserActivity.this).logOut();
                finishService();
                startActivity(new Intent(UserActivity.this, SignInActivity.class));
            }
        });

        shiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(UserActivity.this).saveIsShiftStarted(true);
                lottieAnimationView.setVisibility(View.VISIBLE);
                shiftLayout.setVisibility(View.GONE);
            }
        });
    }

    public void finishService() {
        stopService(new Intent(UserActivity.this,LocationTrackingService.class));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishService();

    }

}