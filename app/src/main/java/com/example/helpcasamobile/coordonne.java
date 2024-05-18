package com.example.helpcasamobile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class coordonne extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Spinner spinner, Countries, States;
    private EditText mail, password, name, lastname, adress, code_postal, num_mobile, numfixe, confpass;
    private TextView date, NextBTN;
    private Intent typeu;
    private String userType;
    private final String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String smail, spassword, sname, slastname, sexe, sadress, country, state, sconfpass,scode_postal,snum_mobile,snumfixe;
    private SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordonne);
        sh = getSharedPreferences("userType", Context.MODE_PRIVATE);

        initializeUIElements();
        initializeFirebase();

        typeu = getIntent();
        userType = typeu.getStringExtra("KEY");

        setDatePicker();
        setCiviliteSpinner();
        setCountriesSpinner();
        setStatesSpinner();

        NextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInputValues();

                if (validateInputs()) {
                    registerUser();
                }
            }
        });
    }

    private void initializeUIElements() {
        mail = findViewById(R.id.mail);
        password = findViewById(R.id.password);
        name = findViewById(R.id.nom);
        lastname = findViewById(R.id.prenom);
        adress = findViewById(R.id.adresse);
        code_postal = findViewById(R.id.Code_Postale);
        num_mobile = findViewById(R.id.NumMobile);
        numfixe = findViewById(R.id.NumFixe);
        date = findViewById(R.id.date);
        confpass = findViewById(R.id.Confirmpassword);
        NextBTN = findViewById(R.id.next);
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void setDatePicker() {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(coordonne.this,
                        android.R.style.Theme_DeviceDefault_Dialog,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String selectedDate = day + "/" + month + "/" + year;
                date.setText(selectedDate);
            }
        };
    }

    private void setCiviliteSpinner() {
        spinner = findViewById(R.id.civilité);
        String[] options = {"Mr", "Melle", "Mm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, options);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sexe = adapterView.getItemAtPosition(i).toString();
                Log.d("TAG", sexe);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setCountriesSpinner() {
        Countries = findViewById(R.id.spinner_gouvernorates);
        new CountryNameFetcher() {
            @Override
            protected void onPostExecute(ArrayList<String> countryNames) {
                if (countryNames != null) {
                    Collections.sort(countryNames);
                    ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(coordonne.this, R.layout.spinner_item_layout, countryNames);
                    countryAdapter.setDropDownViewResource(R.layout.spinner_item_layout);
                    Countries.setAdapter(countryAdapter);
                } else {
                    Toast.makeText(coordonne.this, "Probleme de connection", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();

        Countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                country = adapterView.getItemAtPosition(i).toString();
                if (!country.equals("Tunisia")) {
                    showOnlyTunisiaAlert();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setStatesSpinner() {
        States = findViewById(R.id.spinner_states);
        String[] cities = {
                "Ariana", "Béja", "Ben Arous", "Bizerte", "Gabès", "Gafsa", "Jendouba",
                "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine",
                "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse",
                "Tataouine", "Tozeur", "Tunis", "Zaghouan"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, cities);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        States.setAdapter(adapter);

        States.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                state = adapterView.getItemAtPosition(i).toString();
                Log.d("TAG", state);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void getInputValues() {
        smail = mail.getText().toString().trim();
        spassword = password.getText().toString().trim();
        sname = name.getText().toString().trim();
        slastname = lastname.getText().toString().trim();
        sadress = adress.getText().toString().trim();
        scode_postal = code_postal.getText().toString().trim();
        snum_mobile = num_mobile.getText().toString().trim();
        snumfixe = numfixe.getText().toString().trim();
        sconfpass = confpass.getText().toString().trim();
    }

    private boolean validateInputs() {
        if (smail.isEmpty() || spassword.isEmpty() || slastname.isEmpty() || sadress.isEmpty() || scode_postal.isEmpty()
                || snum_mobile.isEmpty() || date.getText().toString().trim().isEmpty() || sconfpass.isEmpty() || sexe.isEmpty()
                || country.isEmpty() || state.isEmpty()) {
            Toast.makeText(coordonne.this, "Tous les champs doivent être remplis", Toast.LENGTH_LONG).show();
            return false;
        } else if (!smail.matches(emailPattern)) {
            mail.setError("Email invalide");
            return false;
        } else if (!spassword.equals(sconfpass) || spassword.length() < 6) {
            confpass.setError("Les mots de passe ne correspondent pas");
            password.setError("Les mots de passe ne correspondent pas");
            return false;
        }
        return true;
    }

    private void registerUser() {
        ProgressDialog progressDialog = new ProgressDialog(coordonne.this);
        progressDialog.setMessage("Please wait while Registration");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(smail, spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();
                        String username = generateUsername(slastname, userId);
                        Map<String, Object> userdata = createUserdata(username);
                        saveUserDataToFirestore(userId, userdata);
                    }
                } else {
                    Toast.makeText(coordonne.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Map<String, Object> createUserdata(String username) {
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("adresse", sadress);
        userdata.put("code_postal", scode_postal);
        userdata.put("date_de_naissanse", date.getText().toString().trim());
        userdata.put("gouv", country);
        userdata.put("nom", sname);
        userdata.put("num_fixe", snumfixe);
        userdata.put("num_mob", snum_mobile);
        userdata.put("prenom", slastname);
        userdata.put("sexe", sexe);
        userdata.put("type", userType);
        userdata.put("ville", state);
        userdata.put("username", username);
        return userdata;
    }

    private void saveUserDataToFirestore(String userId, Map<String, Object> userdata) {
        db.collection("users").document(userId).set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(coordonne.this, welcome.class);
                intent.putExtra("username", userdata.get("username").toString());
                SharedPreferences.Editor editor = sh.edit();
                //put user type in shared preferences
                editor.putString("type", userType);
                editor.apply();
                startActivity(intent);
                finish();
            }
        });
    }

    private void showOnlyTunisiaAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seule la Tunisie est disponible");
        builder.setMessage("Désolé, seule la Tunisie est disponible pour le moment.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Countries.setSelection(getIndex(Countries, "Tunisia"));
            }
        });
        builder.create().show();
    }

    private int getIndex(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                return i;
            }
        }
        return 0;
    }

    private String generateUsername(String prenom, String userId) {
        return prenom + userId.substring(0, 4);
    }
}
