package shradha.com.finalloginsignupauthproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserClientActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    TextView tv_UserName;
    // TextView tv_UserAge;
    // TextView tv_UserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tv_UserName = findViewById(R.id.tv_UserName);
        // tv_UserAge = findViewById(R.id.tv_UserAge);
        //  tv_UserEmail = findViewById(R.id.tv_UserEmail);

        Toast.makeText(this, "Welcome User", Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DocumentReference documentReference = firestore.collection("User").document(firebaseUser.getUid());

        documentReference.get().addOnCompleteListener(UserClientActivity.this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (task.isSuccessful()) {
                    if (documentSnapshot.exists()) {
                        // Log.d(UserClientActivity.class.getSimpleName(), "DocumentSnapshot data: " + documentSnapshot.getData());
                        String userName = documentSnapshot.getString(Constants.KEY_NAME);
                        String userAge = documentSnapshot.getString(Constants.KEY_AGE);
                        String userEmail = documentSnapshot.getString(Constants.KEY_EMAIL);

                        tv_UserName.setText("Hello " + userName + ", Welcome to Scarlet It Solution");
                        //tv_UserAge.setText(""+userAge);
                        //  tv_UserEmail.setText(userEmail);


                    } else {
                        Log.d(UserClientActivity.class.getSimpleName(), "No such document");
                    }
                } else {
                    Log.d(UserClientActivity.class.getSimpleName(), "get failed with ", task.getException());

                }

            }
        }).addOnFailureListener(UserClientActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserClientActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}