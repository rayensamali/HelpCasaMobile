package com.example.helpcasamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class sidentifier extends AppCompatActivity {
    private EditText mail, password;
    private TextView connect;
    private String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidentifier);

        mail = findViewById(R.id.mail);
        password = findViewById(R.id.pass);
        connect = findViewById(R.id.connect);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }

            private void performLogin() {
                String email = mail.getText().toString();
                String pwd = password.getText().toString();

                if (!email.matches(emailPattern)) {
                    mail.setError("Enter Correct Email");
                } else if (pwd.length() < 6) {
                    password.setError("Enter a valid password");
                } else if (email.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(sidentifier.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Please wait while Login");
                    progressDialog.setTitle("Login");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(sidentifier.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                mUser = mAuth.getCurrentUser();
                                if (mUser != null) {
                                    String userId = mUser.getUid();
                                    // Retrieve user type from Firestore
                                    getUserType(userId);
                                }
                            } else {
                                Toast.makeText(sidentifier.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            private void getUserType(String userId) {
                DocumentReference userDocRef = db.collection("users").document(userId);
                userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String userType = document.getString("type");
                                if("agent".equals(userType)){
                                    startActivity(new Intent(sidentifier.this,homeAgent.class));
                                    finish();
                                }
                                if("PROPRIETAIRE".equals(userType)){
                                    startActivity(new Intent(sidentifier.this,home_CliPro.class));
                                    finish();
                                }
                                // Handle the retrieved user type as needed
                            } else {
                                Toast.makeText(sidentifier.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(sidentifier.this, "Failed to retrieve user type: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
