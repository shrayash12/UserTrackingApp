package shradha.com.finalloginsignupauthproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;

    String userId;
    TextView tv_Name;
    TextView tv_Age;
    TextView tv_EmailId;
    TextView tvTitle_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        tv_Name = findViewById(R.id.tv_Name);
        tv_Age = findViewById(R.id.tv_Age);
        tv_EmailId = findViewById(R.id.tv_EmailId);
        tvTitle_User = findViewById(R.id.tvTitle_User);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        documentReference = firestore.collection("User").document(firebaseUser.getUid());

        documentReference.get().addOnSuccessListener(UserProfile.this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


            }
        }).addOnFailureListener(UserProfile.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}