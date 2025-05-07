// PaymentViewModel.java
package main.java.org.FLUXPAY.payment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fluxpay.data.model.Currency;
import com.fluxpay.data.repository.BlockchainRepository;
import com.fluxpay.data.repository.TransactionRepository;
import com.fluxpay.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaymentViewModel extends ViewModel {

    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private BlockchainRepository blockchainRepository;
    
    private MutableLiveData<List<String>> recentRecipients = new MutableLiveData<>();
    private MutableLiveData<List<Currency>> availableCurrencies = new MutableLiveData<>();
    private MutableLiveData<Double> convertedAmount = new MutableLiveData<>(0.0);
    private MutableLiveData<Double> fee = new MutableLiveData<>(0.0);
    private MutableLiveData<Double> blockchainFee = new MutableLiveData<>(0.0);
    private MutableLiveData<Double> totalCost = new MutableLiveData<>(0.0);
    private MutableLiveData<Boolean> paymentResult = new MutableLiveData<>();
    
    public PaymentViewModel() {
        userRepository = new UserRepository();
        transactionRepository = new TransactionRepository();
        blockchainRepository = new BlockchainRepository();
    }
    
    public void loadRecentRecipients() {
        // In a real app, this would get data from the repository
        // For now, we'll use dummy data
        new Thread(() -> {
            try {
                Thread.sleep(500);
                List<String> recipients = Arrays.asList(
                        "john.doe@example.com",
                        "jane.smith@university.edu",
                        "global.imports@business.com",
                        "tech.exports@company.co.uk",
                        "maria.gonzalez@student.edu"
                );
                recentRecipients.postValue(recipients);
            } catch (Exception e) {
                // Handle error
            }
        }).start();
    }
    
    public void loadAvailableCurrencies() {
        // In a real app, this would get data from the repository
        // For now, we'll use dummy data
        new Thread(() -> {
            try {
                Thread.sleep(500);
                List<Currency> currencies = new ArrayList<>();
                currencies.add(new Currency("USD", "US Dollar", 1.0));
                currencies.add(new Currency("EUR", "Euro", 0.85));
                currencies.add(new Currency("GBP", "British Pound", 0.75));
                currencies.add(new Currency("JPY", "Japanese Yen", 110.0));
                currencies.add(new Currency("CNY", "Chinese Yuan", 6.5));
                currencies.add(new Currency("INR", "Indian Rupee", 75.0));
                availableCurrencies.postValue(currencies);
            } catch (Exception e) {
                // Handle error
            }
        }).start();
    }
    
    public void calculateConversion(double amount, String sourceCurrencyCode, String targetCurrencyCode) {
        // In a real app, this would calculate using real exchange rates
        // For now, we'll use a simplified calculation
        new Thread(() -> {
            try {
                Thread.sleep(300);
                
                // Find currency exchange rates
                Currency sourceCurrency = findCurrencyByCode(sourceCurrencyCode);
                Currency targetCurrency = findCurrencyByCode(targetCurrencyCode);
                
                if (sourceCurrency != null && targetCurrency != null) {
                    // Calculate conversion (simplified)
                    double conversionRate = targetCurrency.getRateToUSD() / sourceCurrency.getRateToUSD();
                    double converted = amount * conversionRate;
                    
                    // Calculate fee (0.5% of source amount)
                    double calculatedFee = amount * 0.005;
                    
                    // Calculate blockchain fee (fixed in USD, convert to source currency)
                    double blockchainFeeUSD = 0.10; // $0.10 USD
                    double calculatedBlockchainFee = blockchainFeeUSD / sourceCurrency.getRateToUSD();
                    
                    // Calculate total
                    double total = amount + calculatedFee + calculatedBlockchainFee;
                    
                    // Update LiveData
                    convertedAmount.postValue(converted);
                    fee.postValue(calculatedFee);
                    blockchainFee.postValue(calculatedBlockchainFee);
                    totalCost.postValue(total);
                }
            } catch (Exception e) {
                // Handle error
            }
        }).start();
    }
    
    private Currency findCurrencyByCode(String code) {
        List<Currency> currencies = availableCurrencies.getValue();
        if (currencies != null) {
            for (Currency currency : currencies) {
                if (currency.getCode().equals(code)) {
                    return currency;
                }
            }
        }
        return null;
    }
    
    public void processPayment(String recipient, String sourceCurrency, String targetCurrency, double amount) {
        // In a real app, this would process the payment through the blockchain
        // For now, we'll simulate a network call
        new Thread(() -> {
            try {
                // Simulate network delay
                Thread.sleep(2000);
                
                // Simulate payment processing via blockchain
                boolean result = blockchainRepository.processPayment(
                        userRepository.getCurrentUser().getId(),
                        recipient,
                        sourceCurrency,
                        targetCurrency,
                        amount,
                        fee.getValue(),
                        blockchainFee.getValue()
                );
                
                // Update LiveData with result
                paymentResult.postValue(result);
            } catch (Exception e) {
                // Handle error
                paymentResult.postValue(false);
            }
        }).start();
    }
    
    public LiveData<List<String>> getRecentRecipients() {
        return recentRecipients;
    }
    
    public LiveData<List<Currency>> getAvailableCurrencies() {
        return availableCurrencies;
    }
    
    public LiveData<Double> getConvertedAmount() {
        return convertedAmount;
    }
    
    public LiveData<Double> getFee() {
        return fee;
    }
    
    public LiveData<Double> getBlockchainFee() {
        return blockchainFee;
    }
    
    public LiveData<Double> getTotalCost() {
        return totalCost;
    }
    
    public LiveData<Boolean> getPaymentResult() {
        return paymentResult;
    }
}