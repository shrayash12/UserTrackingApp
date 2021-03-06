package shradha.com.finalloginsignupauthproj.admin;

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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import shradha.com.finalloginsignupauthproj.R;
import shradha.com.finalloginsignupauthproj.signin.SignInActivity;
import shradha.com.finalloginsignupauthproj.model.UserManager;

public class AdminActivity extends AppCompatActivity {
    Button btn_LogOut;
    FirebaseAuth mAuth;
    FirebaseFirestore fireStore;
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
        fireStore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null && firebaseUser.getUid() != null) {
            documentReference = fireStore.collection("User").document(firebaseUser.getUid());

        }

        btn_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(AdminActivity.this).logOut();
                startActivity(new Intent(AdminActivity.this, SignInActivity.class));
                finish();
            }
        });


        fireStore.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documentSnapshots1 = task.getResult().getDocuments();
                            userListAdapter.setList(documentSnapshots1);

                        } else {
                            Log.d(AdminActivity.class.getSimpleName(), "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}