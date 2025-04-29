package main.java.org.FLUXPAY.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fluxpay.data.model.User;
import com.fluxpay.data.repository.UserRepository;

public class AuthViewModel extends ViewModel {

    private UserRepository userRepository;
    private MutableLiveData<AuthResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<AuthResult> signupResult = new MutableLiveData<>();
    
    public AuthViewModel() {
        userRepository = new UserRepository();
    }
    
    public void login(String email, String password) {
        // Simulate network delay
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                // In a real app, this would call an API
                User user = userRepository.login(email, password);
                if (user != null) {
                    loginResult.postValue(new AuthResult(true, null));
                } else {
                    loginResult.postValue(new AuthResult(false, "Invalid email or password"));
                }
            } catch (Exception e) {
                loginResult.postValue(new AuthResult(false, e.getMessage()));
            }
        }).start();
    }
    
    public void signup(String name, String email, String password, String userType) {
        // Simulate network delay
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                // In a real app, this would call an API
                User user = userRepository.register(name, email, password, userType);
                if (user != null) {
                    signupResult.postValue(new AuthResult(true, null));
                } else {
                    signupResult.postValue(new AuthResult(false, "Email already registered"));
                }
            } catch (Exception e) {
                signupResult.postValue(new AuthResult(false, e.getMessage()));
            }
        }).start();
    }
    
    public LiveData<AuthResult> getLoginResult() {
        return loginResult;
    }
    
    public LiveData<AuthResult> getSignupResult() {
        return signupResult;
    }
    
    // Result class to hold auth operation results
    public static class AuthResult {
        private boolean success;
        private String errorMessage;
        
        public AuthResult(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
    }
}