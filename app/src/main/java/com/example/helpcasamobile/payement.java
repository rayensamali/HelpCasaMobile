package com.example.helpcasamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class payement extends AppCompatActivity {
    private Intent i;
    private RadioGroup paymentMethodGroup;
    private RadioButton radioCash;
    private RadioButton radioCheque;
    private EditText paymentDate;
    private EditText chequeNumber;
    private EditText bankName;
    private Button confirmPaymentButton;
    private TextView priceText;
    private TextView commissionText;
    private TextView totalAmountText;

    private double prix, commission;
    private String typannonce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payement);
        i = getIntent();
        prix = Double.parseDouble(i.getStringExtra("prix"));
        typannonce = i.getStringExtra("typ");

        priceText = findViewById(R.id.prix);
        priceText.setText("Prix: " + prix + " DT");

        commissionText = findViewById(R.id.commissionText);
        totalAmountText = findViewById(R.id.totalAmountText);

        calculateCommission();

        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        radioCash = findViewById(R.id.radioCash);
        radioCheque = findViewById(R.id.radioCheque);
        paymentDate = findViewById(R.id.paymentDate);
        chequeNumber = findViewById(R.id.chequeNumber);
        bankName = findViewById(R.id.bankName);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCheque) {
                paymentDate.setVisibility(View.VISIBLE);
                chequeNumber.setVisibility(View.VISIBLE);
                bankName.setVisibility(View.VISIBLE);
            } else {
                paymentDate.setVisibility(View.GONE);
                chequeNumber.setVisibility(View.GONE);
                bankName.setVisibility(View.GONE);
            }
        });

        confirmPaymentButton.setOnClickListener(v -> {
            int selectedPaymentMethodId = paymentMethodGroup.getCheckedRadioButtonId();
            if (selectedPaymentMethodId == R.id.radioCheque) {
                String paymentDt = paymentDate.getText().toString().trim();
                String chequeNum = chequeNumber.getText().toString().trim();
                String bank = bankName.getText().toString().trim();
                if (paymentDt.isEmpty() || chequeNum.isEmpty() || bank.isEmpty()) {
                    Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle cheque payment
                    Toast.makeText(this, "Paiement par chèque confirmé", Toast.LENGTH_SHORT).show();
                }
            } else {
                String paymentDt = paymentDate.getText().toString().trim();
                if (paymentDt.isEmpty()) {
                    Toast.makeText(this, "Veuillez entrer la date du paiement", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle cash payment
                    Toast.makeText(this, "Paiement en espèces confirmé", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void calculateCommission() {
        if (typannonce.equals("Location")) {
            if (prix < 300) {
                commission = prix * 0.05;
            } else if (prix >= 300 && prix < 500) {
                commission = prix * 0.1;
            } else if (prix >= 500 && prix < 700) {
                commission = prix * 0.15;
            } else {
                commission = prix * 0.20;
            }
        } else if (typannonce.equals("Vente")) {
            if (prix < 100000) {
                commission = prix * 0.01;
            } else if (prix >= 100000 && prix < 300000) {
                commission = prix * 0.02;
            } else {
                commission = prix * 0.03;
            }
        }

        commissionText.setText("Commission: " + commission + " DT");

        double totalAmount = prix + commission;
        totalAmountText.setText("Montant total: " + totalAmount + " DT");
    }
}
