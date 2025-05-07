package main.java.org.FLUXPAY.CommonCompo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.fluxpay.android.data.local.SharedPrefsManager;
import com.fluxpay.android.data.network.ApiService;
import com.fluxpay.android.data.network.BlockchainService;
import com.fluxpay.android.data.repository.UserRepository;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T binding;
    protected UserRepository userRepository;
    protected SharedPrefsManager sharedPrefsManager;
    protected ApiService apiService;
    protected BlockchainService blockchainService;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize shared dependencies
        sharedPrefsManager = SharedPrefsManager.getInstance(this);
        apiService = ApiService.getInstance();
        blockchainService = BlockchainService.getInstance();
        userRepository = new UserRepository(apiService, sharedPrefsManager);
        
        // Setup view binding
        binding = getViewBinding();
        setContentView(binding.getRoot());
        
        setupViews();
        observeViewModel();
    }
    
    protected abstract T getViewBinding();
    
    protected abstract void setupViews();
    
    protected abstract void observeViewModel();
    
    protected boolean isUserLoggedIn() {
        return sharedPrefsManager.isLoggedIn();
    }
}