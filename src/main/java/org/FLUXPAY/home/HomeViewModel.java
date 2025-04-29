package main.java.org.FLUXPAY.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fluxpay.data.model.Transaction;
import com.fluxpay.data.model.User;
import com.fluxpay.data.repository.TransactionRepository;
import com.fluxpay.data.repository.UserRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<List<Transaction>> recentTransactions = new MutableLiveData<>();
    private MutableLiveData<LoadingState> loadingState = new MutableLiveData<>();
    
    public enum LoadingState {
        LOADING, SUCCESS, ERROR
    }
    
    public HomeViewModel() {
        userRepository = new UserRepository();
        transactionRepository = new TransactionRepository();
        loadingState.setValue(LoadingState.LOADING);
    }
    
    public void loadUserData() {
        loadingState.setValue(LoadingState.LOADING);
        
        // Simulate network call
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                User currentUser = userRepository.getCurrentUser();
                user.postValue(currentUser);
                loadingState.postValue(LoadingState.SUCCESS);
            } catch (Exception e) {
                loadingState.postValue(LoadingState.ERROR);
            }
        }).start();
    }
    
    public void loadRecentTransactions() {
        // Simulate network call
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                List<Transaction> transactions = transactionRepository.getRecentTransactions(5);
                recentTransactions.postValue(transactions);
            } catch (Exception e) {
                // Error handled by user data loading
            }
        }).start();
    }
    
    public LiveData<User> getUser() {
        return user;
    }
    
    public LiveData<List<Transaction>> getRecentTransactions() {
        return recentTransactions;
    }
    
    public LiveData<LoadingState> getLoadingState() {
        return loadingState;
    }
}
