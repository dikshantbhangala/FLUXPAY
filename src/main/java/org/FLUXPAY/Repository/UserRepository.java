package main.java.org.FLUXPAY.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fluxpay.android.data.local.SharedPrefsManager;
import com.fluxpay.android.data.model.User;
import com.fluxpay.android.data.network.ApiService;
import com.fluxpay.android.data.network.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";
    
    private final ApiService apiService;
    private final SharedPrefsManager sharedPrefsManager;
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public UserRepository(ApiService apiService, SharedPrefsManager sharedPrefsManager) {
        this.apiService = apiService;
        this.sharedPrefsManager = sharedPrefsManager;
        
        // Try to load cached user
        String cachedUserJson = sharedPrefsManager.getUserData();
        if (cachedUserJson != null && !cachedUserJson.isEmpty()) {
            try {
                User user = parseUserFromJson(new JSONObject(cachedUserJson));
                currentUser.setValue(user);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing cached user data", e);
            }
        }
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(String email, String password) {
        if (!NetworkUtils.isNetworkAvailable()) {
            errorMessage.setValue("No internet connection available");
            return;
        }

        isLoading.setValue(true);
        
        // API call for login would go here
        // For demonstration, we'll create a mock user
        
        // Simulate network delay
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                // Create mock user
                User mockUser = new User(
                    "user123",
                    "Jane",
                    "Doe",
                    email,
                    "+1234567890",
                    "STUDENT",
                    "USA"
                );
                
                // Cache user data
                try {
                    JSONObject userJson = new JSONObject();
                    userJson.put("userId", mockUser.getUserId());
                    userJson.put("firstName", mockUser.getFirstName());
                    userJson.put("lastName", mockUser.getLastName());
                    userJson.put("email", mockUser.getEmail());
                    userJson.put("phone", mockUser.getPhone());
                    userJson.put("userType", mockUser.getUserType());
                    userJson.put("country", mockUser.getCountry());
                    
                    sharedPrefsManager.saveUserData(userJson.toString());
                    sharedPrefsManager.saveAuthToken("mock_auth_token");
                    
                    currentUser.postValue(mockUser);
                } catch (JSONException e) {
                    errorMessage.postValue("Error processing user data");
                    Log.e(TAG, "Error creating user JSON", e);
                }
                
                isLoading.postValue(false);
            } catch (InterruptedException e) {
                errorMessage.postValue("Login process interrupted");
                isLoading.postValue(false);
            }
        }).start();
    }

    public void register(String firstName, String lastName, String email, 
                        String password, String phone, String userType, String country) {
        if (!NetworkUtils.isNetworkAvailable()) {
            errorMessage.setValue("No internet connection available");
            return;
        }

        isLoading.setValue(true);
        
        // API call for registration would go here
        // For demonstration, we'll create a mock user
        
        // Simulate network delay
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                // Create mock user
                User mockUser = new User(
                    "user123",
                    firstName,
                    lastName,
                    email,
                    phone,
                    userType,
                    country
                );
                
                // Cache user data
                try {
                    JSONObject userJson = new JSONObject();
                    userJson.put("userId", mockUser.getUserId());
                    userJson.put("firstName", mockUser.getFirstName());
                    userJson.put("lastName", mockUser.getLastName());
                    userJson.put("email", mockUser.getEmail());
                    userJson.put("phone", mockUser.getPhone());
                    userJson.put("userType", mockUser.getUserType());
                    userJson.put("country", mockUser.getCountry());
                    
                    sharedPrefsManager.saveUserData(userJson.toString());
                    sharedPrefsManager.saveAuthToken("mock_auth_token");
                    
                    currentUser.postValue(mockUser);
                } catch (JSONException e) {
                    errorMessage.postValue("Error processing user data");
                    Log.e(TAG, "Error creating user JSON", e);
                }
                
                isLoading.postValue(false);
            } catch (InterruptedException e) {
                errorMessage.postValue("Registration process interrupted");
                isLoading.postValue(false);
            }
        }).start();
    }

    public void logout() {
        sharedPrefsManager.clearUserData();
        sharedPrefsManager.clearAuthToken();
        currentUser.setValue(null);
    }

    public void updateUserProfile(User updatedUser) {
        if (!NetworkUtils.isNetworkAvailable()) {
            errorMessage.setValue("No internet connection available");
            return;
        }

        isLoading.setValue(true);
        
        // API call for profile update would go here
        // For demonstration, we'll update the mock user
        
        // Simulate network delay
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                // Cache updated user data
                try {
                    JSONObject userJson = new JSONObject();
                    userJson.put("userId", updatedUser.getUserId());
                    userJson.put("firstName", updatedUser.getFirstName());
                    userJson.put("lastName", updatedUser.getLastName());
                    userJson.put("email", updatedUser.getEmail());
                    userJson.put("phone", updatedUser.getPhone());
                    userJson.put("userType", updatedUser.getUserType());
                    userJson.put("country", updatedUser.getCountry());
                    
                    sharedPrefsManager.saveUserData(userJson.toString());
                    
                    currentUser.postValue(updatedUser);
                } catch (JSONException e) {
                    errorMessage.postValue("Error processing user data");
                    Log.e(TAG, "Error creating user JSON", e);
                }
                
                isLoading.postValue(false);
            } catch (InterruptedException e) {
                errorMessage.postValue("Profile update process interrupted");
                isLoading.postValue(false);
            }
        }).start();
    }

    private User parseUserFromJson(JSONObject json) throws JSONException {
        String userId = json.getString("userId");
        String firstName = json.getString("firstName");
        String lastName = json.getString("lastName");
        String email = json.getString("email");
        String phone = json.getString("phone");
        String userType = json.getString("userType");
        String country = json.getString("country");
        
        User user = new User(userId, firstName, lastName, email, phone, userType, country);
        
        if (json.has("profileImageUrl")) {
            user.setProfileImageUrl(json.getString("profileImageUrl"));
        }
        
        if (json.has("kycVerified")) {
            user.setKycVerified(json.getBoolean("kycVerified"));
        }
        
        if (json.has("createdTimestamp")) {
            user.setCreatedTimestamp(json.getLong("createdTimestamp"));
        }
        
        return user;
    }
}