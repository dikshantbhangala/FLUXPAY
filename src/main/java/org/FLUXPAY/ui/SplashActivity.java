package main.java.org.FLUXPAY.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.fluxpay.R;
import com.fluxpay.data.local.SharedPrefsManager;
import com.fluxpay.ui.auth.LoginActivity;
import com.fluxpay.ui.onboarding.OnboardingActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1500;
    private ImageView logoImageView;
    private SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Initialize components
        logoImageView = findViewById(R.id.iv_logo);
        sharedPrefsManager = new SharedPrefsManager(this);
        
        // Run animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        
        logoImageView.startAnimation(fadeIn);
        
        // Navigate to appropriate screen after splash duration
        new Handler().postDelayed(() -> {
            Intent intent;
            if (sharedPrefsManager.isFirstLaunch()) {
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            } else if (sharedPrefsManager.isLoggedIn()) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }, SPLASH_DURATION);
    }
}