package main.java.org.FLUXPAY.CommonCompo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.fluxpay.android.data.local.SharedPrefsManager;
import com.fluxpay.android.data.network.ApiService;
import com.fluxpay.android.data.network.BlockchainService;

public abstract class BaseFragment<T extends ViewBinding> extends Fragment {
    protected T binding;
    protected SharedPrefsManager sharedPrefsManager;
    protected ApiService apiService;
    protected BlockchainService blockchainService;
    
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        
        // Initialize shared dependencies
        sharedPrefsManager = SharedPrefsManager.getInstance(context);
        apiService = ApiService.getInstance();
        blockchainService = BlockchainService.getInstance();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getViewBinding(inflater, container);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupViews();
        observeViewModel();
    }
    
    protected abstract T getViewBinding(LayoutInflater inflater, ViewGroup container);
    
    protected abstract void setupViews();
    
    protected abstract void observeViewModel();
    
    protected boolean isUserLoggedIn() {
        return sharedPrefsManager.isLoggedIn();
    }
}
