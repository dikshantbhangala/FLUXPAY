package main.java.org.FLUXPAY.Networkservice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlockchainService {
    private static final String BASE_URL = "https://blockchain.fluxpay.com/v1/";
    
    private static BlockchainService instance;
    private final Retrofit retrofit;
    
    private BlockchainService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    
    public static synchronized BlockchainService getInstance() {
        if (instance == null) {
            instance = new BlockchainService();
        }
        return instance;
    }
    
    public Retrofit getRetrofit() {
        return retrofit;
    }
    
    // Additional methods for blockchain API calls would be defined here
}
