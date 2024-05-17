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

public class sidentifier extends AppCompatActivity {
    private EditText mail,password;
    private TextView connect;
    private String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidentifier);

        mail = findViewById(R.id.mail);
        password = findViewById(R.id.pass);
        connect = findViewById(R.id.connect);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    perforLogin();
            }
        private  void perforLogin(){
                String email = mail.getText().toString();
                String pwd = password.getText().toString();
            if(!email.matches(emailPattern)){
                mail.setError("Enter Correct Email");
            }else if(pwd.length()<6){
                password.setError("Enter une mot de pass valide");
            }else if(email.isEmpty() || pwd.isEmpty()){
                Toast.makeText(sidentifier.this, "veiller  remplir tout les champs", Toast.LENGTH_SHORT).show();
            }else{
                progressDialog.setMessage("Please wait while Login");
                progressDialog.setTitle("Login");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(sidentifier.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(sidentifier.this, HomePage.class));
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(sidentifier.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }


        });

    }
}