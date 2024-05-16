package com.example.helpcasamobile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class coordonne extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private Spinner spinner, Countries,States;
    private EditText mail,password,name,lastname,adress,code_postal,num_mobile,numfixe,confpass;
    private  TextView date;
    private TextView NextBTN;
    private Intent typeu;
    private String userType;
    private String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String smail,spassword,sname,slastname,ssexe,sadress,sgouvernorat,sville,scode_postal,snum_mobile,snumfixe,sdate,sexe,country,state,sconfpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordonne);
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
                String dateselectionné = day + "/" + month + "/" + year;
                 date.setText(dateselectionné);
            }
        };


        // affichage de liste de civilité
        typeu = getIntent();
        userType = typeu.getStringExtra("KEY");

        spinner = findViewById(R.id.civilité);
        String[] options = {"Mr", "Melle", "Mm"};
        String[] cities = {
                "Ariana",
                "Béja",
                "Ben Arous",
                "Bizerte",
                "Gabès",
                "Gafsa",
                "Jendouba",
                "Kairouan",
                "Kasserine",
                "Kebili",
                "Kef",
                "Mahdia",
                "Manouba",
                "Medenine",
                "Monastir",
                "Nabeul",
                "Sfax",
                "Sidi Bouzid",
                "Siliana",
                "Sousse",
                "Tataouine",
                "Tozeur",
                "Tunis",
                "Zaghouan"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, options);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spinner.setAdapter(adapter);
        Countries = findViewById(R.id.spinner_gouvernorates);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sexe = adapterView.getItemAtPosition(i).toString();
                Log.d("TAG", sexe);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        new CountryNameFetcher() {
            @Override
            protected void onPostExecute(ArrayList<String> countryNames) {
                if (countryNames != null) {
                    // Log the number of countries
                    Collections.sort(countryNames);
                    // Set the adapter for the Countries spinner
                    ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(coordonne.this, R.layout.spinner_item_layout, countryNames);
                    countryAdapter.setDropDownViewResource(R.layout.spinner_item_layout);
                    Countries.setAdapter(countryAdapter);
                    // Do something with the country names ArrayList
                } else {
                    // Handle error
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
                    spinner.setSelection(0); // Reset spinner to first item (Tunisia)
                    Log.d("log_d", country);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        States = findViewById(R.id.spinner_states);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item_layout, cities);
        adapter1.setDropDownViewResource(R.layout.spinner_item_layout);
        States.setAdapter(adapter1);
        States.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                state = adapterView.getItemAtPosition(i).toString();
                Log.d("my-tag", state);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        NextBTN = findViewById(R.id.next);
        NextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smail = mail.getText().toString().trim();
                spassword = password.getText().toString().trim();
                sname = name.getText().toString().trim();
                slastname = lastname.getText().toString().trim();
                sadress = adress.getText().toString().trim();
                scode_postal = code_postal.getText().toString().trim();
                snum_mobile = num_mobile.getText().toString().trim();
                snumfixe = numfixe.getText().toString().trim();
                sdate = date.getText().toString().trim();
                sconfpass = confpass.getText().toString().trim();

                Log.d("mylog", "onClick: ");

                if (smail.equals("") || spassword.equals("") || slastname.equals("") || sadress.equals("") || scode_postal.equals("")
                        || snum_mobile.equals("") || sdate.equals("") || sconfpass.equals("") || sexe.equals("")
                        || country.equals("") || state.equals("")) {
                    Log.d("arrorlog", "every thing not ok ");
                    Toast.makeText(coordonne.this, "Tous les champs doivent être remplis", Toast.LENGTH_LONG).show();
                } else if (!smail.matches(emailPattern)) {
                    mail.setError("Email invalide");
                } else if (!spassword.equals(sconfpass)) {
                    confpass.setError("Les mots de passe ne correspondent pas");
                } else {
                    // Validation passed, proceed to next activity
                    startActivity(new Intent(coordonne.this, welcome.class));
                }
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
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int getIndex(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                return i;
            }

        }
        return 0;
    }
}

