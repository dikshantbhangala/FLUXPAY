package main.java.org.FLUXPAY.Networkservice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String BASE_URL = "https://api.fluxpay.com/v1/";
    
    private static ApiService instance;
    private final Retrofit retrofit;
    
    private ApiService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    
    public static synchronized ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }
    
    public Retrofit getRetrofit() {
        return retrofit;
    }
    
    // Additional methods for API calls would be defined here
}