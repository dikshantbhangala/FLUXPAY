package main.java.org.FLUXPAY.payment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.fluxpay.R;
import com.fluxpay.data.model.Currency;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AutoCompleteTextView actvRecipient;
    private AutoCompleteTextView actvSourceCurrency;
    private AutoCompleteTextView actvTargetCurrency;
    private EditText etAmount;
    private TextView tvConvertedAmount;
    private TextView tvFee;
    private TextView tvTotalCost;
    private TextView tvBlockchainFee;
    private Button btnContinue;
    private LinearProgressIndicator progressIndicator;
    private TextInputLayout tilRecipient;
    private TextInputLayout tilAmount;
    
    private PaymentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        
        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        actvRecipient = findViewById(R.id.actv_recipient);
        actvSourceCurrency = findViewById(R.id.actv_source_currency);
        actvTargetCurrency = findViewById(R.id.actv_target_currency);
        etAmount = findViewById(R.id.et_amount);
        tvConvertedAmount = findViewById(R.id.tv_converted_amount);
        tvFee = findViewById(R.id.tv_fee);
        tvTotalCost = findViewById(R.id.tv_total_cost);
        tvBlockchainFee = findViewById(R.id.tv_blockchain_fee);
        btnContinue = findViewById(R.id.btn_continue);
        progressIndicator = findViewById(R.id.progress_indicator);
        tilRecipient = findViewById(R.id.til_recipient);
        tilAmount = findViewById(R.id.til_amount);
        
        // Setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.send_money);
        
        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        
        // Setup dropdown menus
        setupDropdowns();
        
        // Setup observers
        setupObservers();
        
        // Setup click listeners
        setupClickListeners();
    }
    
    private void setupDropdowns() {
        // Setup recipients dropdown
        viewModel.loadRecentRecipients();
        
        // Setup currency dropdowns
        viewModel.loadAvailableCurrencies();
    }
    
    private void setupObservers() {
        // Observe recipients
        viewModel.getRecentRecipients().observe(this, recipients -> {
            if (recipients != null && !recipients.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                        R.layout.item_dropdown, recipients);
                actvRecipient.setAdapter(adapter);
            }
        });
        
        // Observe currencies
        viewModel.getAvailableCurrencies().observe(this, currencies -> {
            if (currencies != null && !currencies.isEmpty()) {
                String[] currencyNames = new String[currencies.size()];
                
                for (int i = 0; i < currencies.size(); i++) {
                    Currency currency = currencies.get(i);
                    currencyNames[i] = currency.getCode() + " - " + currency.getName();
                }
                
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                        R.layout.item_dropdown, currencyNames);
                        
                actvSourceCurrency.setAdapter(adapter);
                actvTargetCurrency.setAdapter(adapter);
                
                // Set default values
                actvSourceCurrency.setText(currencyNames[0], false);
                actvTargetCurrency.setText(currencyNames[1], false);
            }
        });
        
        // Observe converted amount
        viewModel.getConvertedAmount().observe(this, amount -> {
            if (amount > 0) {
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String targetCurrency = actvTargetCurrency.getText().toString().split(" - ")[0];
                tvConvertedAmount.setText(getString(R.string.converted_amount_format, 
                        targetCurrency, df.format(amount)));
            } else {
                tvConvertedAmount.setText(R.string.enter_amount_to_see_conversion);
            }
        });
        
        // Observe fee
        viewModel.getFee().observe(this, fee -> {
            if (fee > 0) {
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String sourceCurrency = actvSourceCurrency.getText().toString().split(" - ")[0];
                tvFee.setText(getString(R.string.fee_format, 
                        sourceCurrency, df.format(fee)));
            } else {
                tvFee.setText(R.string.enter_amount_to_see_fee);
            }
        });
        
        // Observe blockchain fee
        viewModel.getBlockchainFee().observe(this, fee -> {
            if (fee > 0) {
                DecimalFormat df = new DecimalFormat("#,##0.00");
                tvBlockchainFee.setText(getString(R.string.blockchain_fee_format, df.format(fee)));
            } else {
                tvBlockchainFee.setText(R.string.blockchain_fee_explanation);
            }
        });
        
        // Observe total cost
        viewModel.getTotalCost().observe(this, total -> {
            if (total > 0) {
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String sourceCurrency = actvSourceCurrency.getText().toString().split(" - ")[0];
                tvTotalCost.setText(getString(R.string.total_cost_format, 
                        sourceCurrency, df.format(total)));
            } else {
                tvTotalCost.setText(R.string.enter_amount_to_see_total);
            }
        });
        
        // Observe payment result
        viewModel.getPaymentResult().observe(this, success -> {
            progressIndicator.setVisibility(View.INVISIBLE);
            btnContinue.setEnabled(true);
            
            if (success) {
                // Show success screen
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new PaymentConfirmationFragment())
                        .commit();
            } else {
                // Show error
                Toast.makeText(this, R.string.payment_error, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void setupClickListeners() {
        // Calculate conversion when amount changes
        etAmount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                calculateConversion();
            }
        });
        
        // Calculate conversion when currencies change
        actvSourceCurrency.setOnItemClickListener((parent, view, position, id) -> {
            calculateConversion();
        });
        
        actvTargetCurrency.setOnItemClickListener((parent, view, position, id) -> {
            calculateConversion();
        });
        
        // Process payment when continue is clicked
        btnContinue.setOnClickListener(v -> {
            if (validateInputs()) {
                progressIndicator.setVisibility(View.VISIBLE);
                btnContinue.setEnabled(false);
                
                String recipient = actvRecipient.getText().toString();
                String sourceCurrency = actvSourceCurrency.getText().toString().split(" - ")[0];
                String targetCurrency = actvTargetCurrency.getText().toString().split(" - ")[0];
                double amount = Double.parseDouble(etAmount.getText().toString());
                
                viewModel.processPayment(recipient, sourceCurrency, targetCurrency, amount);
            }
        });
    }
    
    private void calculateConversion() {
        String amountStr = etAmount.getText().toString();
        if (!TextUtils.isEmpty(amountStr)) {
            try {
                double amount = Double.parseDouble(amountStr);
                String sourceCurrency = actvSourceCurrency.getText().toString().split(" - ")[0];
                String targetCurrency = actvTargetCurrency.getText().toString().split(" - ")[0];
                
                viewModel.calculateConversion(amount, sourceCurrency, targetCurrency);
            } catch (NumberFormatException e) {
                // Invalid number format
                Toast.makeText(this, R.string.invalid_amount, Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private boolean validateInputs() {
        boolean isValid = true;
        
        // Validate recipient
        String recipient = actvRecipient.getText().toString();
        if (TextUtils.isEmpty(recipient)) {
            tilRecipient.setError(getString(R.string.error_field_required));
            isValid = false;
        } else {
            tilRecipient.setError(null);
        }
        
        // Validate amount
        String amountStr = etAmount.getText().toString();
        if (TextUtils.isEmpty(amountStr)) {
            tilAmount.setError(getString(R.string.error_field_required));
            isValid = false;
        } else {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    tilAmount.setError(getString(R.string.error_invalid_amount));
                    isValid = false;
                } else {
                    tilAmount.setError(null);
                }
            } catch (NumberFormatException e) {
                tilAmount.setError(getString(R.string.error_invalid_amount));
                isValid = false;
            }
        }
        
        return isValid;
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}