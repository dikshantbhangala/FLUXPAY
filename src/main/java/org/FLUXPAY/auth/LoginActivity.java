package main.java.org.FLUXPAY.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.fluxpay.R;
import com.fluxpay.ui.MainActivity;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private CircularProgressIndicator progressIndicator;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize views
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        progressIndicator = findViewById(R.id.progress_indicator);
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        
        // Observe login result
        viewModel.getLoginResult().observe(this, result -> {
            progressIndicator.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            
            if (result.isSuccess()) {
                // Login successful
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                // Login failed
                String errorMessage = result.getErrorMessage();
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        
        // Set click listeners
        btnLogin.setOnClickListener(v -> attemptLogin());
        
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
    
    private void attemptLogin() {
        // Reset errors
        tilEmail.setError(null);
        tilPassword.setError(null);
        
        // Get values
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        
        boolean cancel = false;
        View focusView = null;
        
        // Check password
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError(getString(R.string.error_field_required));
            focusView = etPassword;
            cancel = true;
        } else if (password.length() < 6) {
            tilPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }
        
        // Check email
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_field_required));
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        }
        
        if (cancel) {
            // There was an error; focus the first form field with an error
            focusView.requestFocus();
        } else {
            // Show progress indicator and attempt login
            progressIndicator.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            viewModel.login(email, password);
        }
    }
    
    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }
}