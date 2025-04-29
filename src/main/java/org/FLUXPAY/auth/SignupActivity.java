
package main.java.org.FLUXPAY.auth;

import android.content.Intent;
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
import androidx.lifecycle.ViewModelProvider;

import com.fluxpay.R;
import com.fluxpay.ui.MainActivity;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;

public class SignupActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private AutoCompleteTextView actvUserType;
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputLayout tilConfirmPassword;
    private TextInputLayout tilUserType;
    private Button btnSignup;
    private TextView tvLogin;
    private CircularProgressIndicator progressIndicator;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        // Initialize views
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        actvUserType = findViewById(R.id.actv_user_type);
        tilName = findViewById(R.id.til_name);
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        tilConfirmPassword = findViewById(R.id.til_confirm_password);
        tilUserType = findViewById(R.id.til_user_type);
        btnSignup = findViewById(R.id.btn_signup);
        tvLogin = findViewById(R.id.tv_login);
        progressIndicator = findViewById(R.id.progress_indicator);
        
        // Setup user type dropdown
        String[] userTypes = new String[] {"Student", "Exporter", "Importer", "Individual"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_dropdown, userTypes);
        actvUserType.setAdapter(adapter);
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        
        // Observe signup result
        viewModel.getSignupResult().observe(this, result -> {
            progressIndicator.setVisibility(View.GONE);
            btnSignup.setEnabled(true);
            
            if (result.isSuccess()) {
                // Signup successful
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            } else {
                // Signup failed
                String errorMessage = result.getErrorMessage();
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        
        // Set click listeners
        btnSignup.setOnClickListener(v -> attemptSignup());
        
        tvLogin.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
    
    private void attemptSignup() {
        // Reset errors
        tilName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);
        tilUserType.setError(null);
        
        // Get values
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String userType = actvUserType.getText().toString().trim();
        
        boolean cancel = false;
        View focusView = null;
        
        // Check user type
        if (TextUtils.isEmpty(userType)) {
            tilUserType.setError(getString(R.string.error_field_required));
            focusView = actvUserType;
            cancel = true;
        }
        
        // Check confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.error_field_required));
            focusView = etConfirmPassword;
            cancel = true;
        } else if (!confirmPassword.equals(password)) {
            tilConfirmPassword.setError(getString(R.string.error_passwords_dont_match));
            focusView = etConfirmPassword;
            cancel = true;
        }
        
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
        
        // Check name
        if (TextUtils.isEmpty(name)) {
            tilName.setError(getString(R.string.error_field_required));
            focusView = etName;
            cancel = true;
        }
        
        if (cancel) {
            // There was an error; focus the first form field with an error
            focusView.requestFocus();
        } else {
            // Show progress indicator and attempt signup
            progressIndicator.setVisibility(View.VISIBLE);
            btnSignup.setEnabled(false);
            viewModel.signup(name, email, password, userType);
        }
    }
    
    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }
}
