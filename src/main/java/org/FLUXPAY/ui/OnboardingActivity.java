package main.java.org.FLUXPAY.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.fluxpay.R;
import com.fluxpay.data.local.SharedPrefsManager;
import com.fluxpay.ui.auth.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnNext;
    private Button btnSkip;
    private TextView tvGetStarted;
    private TabLayout tabIndicator;
    private OnboardingPagerAdapter adapter;
    private SharedPrefsManager sharedPrefsManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        
        // Initialize views
        viewPager = findViewById(R.id.onboarding_viewpager);
        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);
        tvGetStarted = findViewById(R.id.tv_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        
        // Initialize shared preferences manager
        sharedPrefsManager = new SharedPrefsManager(this);
        
        // Setup onboarding pages
        List<OnboardingPage> pages = new ArrayList<>();
        pages.add(new OnboardingPage(
                R.drawable.onboarding_global_payments,
                "Global Payments Made Simple",
                "Send money across borders with minimal fees using blockchain technology"
        ));
        pages.add(new OnboardingPage(
                R.drawable.onboarding_secure_transactions,
                "Secure & Transparent",
                "Every transaction is encrypted and traceable on the blockchain"
        ));
        pages.add(new OnboardingPage(
                R.drawable.onboarding_student_features,
                "Student-Friendly Features",
                "Special rates for tuition payments and currency conversion for students abroad"
        ));
        pages.add(new OnboardingPage(
                R.drawable.onboarding_business_tools,
                "Business Tools",
                "Import-export payment solutions with customs documentation integration"
        ));
        
        // Set up adapter and viewpager
        adapter = new OnboardingPagerAdapter(this, pages);
        viewPager.setAdapter(adapter);
        
        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabIndicator, viewPager,
                (tab, position) -> {
                    // No title for tabs
                }).attach();
        
        // Handle button clicks
        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        
        btnSkip.setOnClickListener(v -> finishOnboarding());
        
        tvGetStarted.setOnClickListener(v -> finishOnboarding());
        
        // Page change callback
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == adapter.getItemCount() - 1) {
                    // Last page
                    btnNext.setVisibility(View.INVISIBLE);
                    btnSkip.setVisibility(View.INVISIBLE);
                    tvGetStarted.setVisibility(View.VISIBLE);
                } else {
                    btnNext.setVisibility(View.VISIBLE);
                    btnSkip.setVisibility(View.VISIBLE);
                    tvGetStarted.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    
    private void finishOnboarding() {
        sharedPrefsManager.setFirstLaunchCompleted();
        startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}