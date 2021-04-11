package shradha.com.finalloginsignupauthproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity {
    Button btn_LogOut;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    RecyclerView recyclerView;
    UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        recyclerView = findViewById(R.id.list);
        userListAdapter = new UserListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userListAdapter);
        Toast.makeText(this, "Welcome Admin!!!!", Toast.LENGTH_SHORT).show();
        btn_LogOut = findViewById(R.id.btn_LogOut);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null && firebaseUser.getUid() != null) {
            documentReference = firestore.collection("User").document(firebaseUser.getUid());

        }

        btn_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(Admin.this).logOut();
                startActivity(new Intent(Admin.this, MainActivity.class));
                finish();
            }
        });


        firestore.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documentSnapshots1 = task.getResult().getDocuments();
                            userListAdapter.setList(documentSnapshots1);

                        } else {
                            Log.d(Admin.class.getSimpleName(), "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}