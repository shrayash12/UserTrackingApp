package shradha.com.finalloginsignupauthproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Admin extends AppCompatActivity {
    Button btn_LogOut;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toast.makeText(this, "Welcome Admin!!!!", Toast.LENGTH_SHORT).show();
        btn_LogOut = findViewById(R.id.btn_LogOut);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
         documentReference = firestore.collection("User").document(firebaseUser.getUid());

        btn_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Admin.this, MainActivity.class));
            }
        });


        firestore.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documentSnapshots1 =  task.getResult().getDocuments();
                            for (int i  = 0; i < documentSnapshots1.size();i++) {
                                Log.d(Admin.class.getSimpleName(), documentSnapshots1.get(i).getId() + " => " + documentSnapshots1.get(i).getData());
                            }

                        } else {
                            Log.d(Admin.class.getSimpleName(), "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}