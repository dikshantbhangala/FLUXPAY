package main.java.org.FLUXPAY.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fluxpay.android.data.model.Transaction;
import com.fluxpay.android.data.network.ApiService;
import com.fluxpay.android.data.network.NetworkUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionRepository {
    private static final String TAG = "TransactionRepository";
    
    private final ApiService apiService;
    private final MutableLiveData<List<Transaction>> userTransactions = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Transaction> currentTransaction = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public TransactionRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<List<Transaction>> getUserTransactions() {
        return userTransactions;
    }

    public LiveData<Transaction> getCurrentTransaction() {
        return currentTransaction;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchUserTransactions(String userId) {
        if (!NetworkUtils.isNetworkAvailable()) {
            errorMessage.setValue("No internet connection available");
            return;
        }

        isLoading.setValue(true);
        
        // API call would go here
        // For demonstration, we'll create mock transactions
        
        // Simulate network delay
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                List<Transaction> mockTransactions = new ArrayList<>();
                
                // Add some mock transactions
                Transaction t1 = new Transaction(
                    userId,
                    "recipient1",
                    new BigDecimal("500.00"),
                    "USD",
                    "EUR"
                );
                t1.setStatus(Transaction.STATUS_COMPLETED);
                t1.setExchangeRate(new BigDecimal("0.92"));
                t1.setFee(new BigDecimal("4.50"));
                t1.setBlockchainTxHash("0x123456789abcdef");
                t1.setPurpose("EDUCATION");
                mockTransactions.add(t1);
                
                Transaction t2 = new Transaction(
                    "sender2",
                    userId,
                    new BigDecimal("1000.00"),
                    "GBP",
                    "USD"
                );
                t2.setStatus(Transaction.STATUS_COMPLETED);
                t2.setExchangeRate(new BigDecimal("1.25"));
                t2.setFee(new BigDecimal("8.75"));
                t2.setBlockchainTxHash("0xabcdef123456789");
                t2.setPurpose("IMPORT");
                mockTransactions.add(t2);
                
                Transaction t3 = new Transaction(
                    userId,
                    "recipient3",
                    new BigDecimal("250.00"),
                    "USD",
                    "INR"
                );
                t3.setStatus(Transaction.STATUS_PENDING);
                t3.setExchangeRate(new BigDecimal("83.25"));
                t3.setFee(new BigDecimal("2.25"));
                t3.setPurpose("EDUCATION");
                mockTransactions.add(t3);
                
                userTransactions.postValue(mockTransactions);
                isLoading.postValue(false);
            } catch (InterruptedException e) {
                errorMessage.postValue("Fetching transactions interrupted");
                isLoading.postValue(false);
            }
        }).start();
    }

    public void getTransactionDetails(String transactionId) {
        if (!NetworkUtils.isNetworkAvailable()) {
            errorMessage.setValue("No internet connection available");
            return;
        }

        isLoading.setValue(true);
        
        // API call would go here
        // For demonstration, we'll create a mock transaction
        
        // Simulate network delay
        new Thread(() -> {
            try {
                Thread.sleep(500);
                
                // Create mock transaction
                Transaction mockTransaction = new Transaction(
                    "user123",
                    "recipient456",
                    new BigDecimal("750.00"),
                    "USD",
                    "EUR"
                );
                mockTransaction.setTransactionId(transactionId);
                mockTransaction.setStatus(Transaction.STATUS_COMPLETED);
                mockTransaction.setExchangeRate(new BigDecimal("0.92"));
                mockTransaction.setFee(new BigDecimal("6.75"));
                mockTransaction.setBlockchainTxHash("0x9876543210abcdef");
                mockTransaction.setPurpose("EDUCATION");
                mockTransaction.setNotes("Tuition payment for Spring semester");
                
                currentTransaction.postValue(mockTransaction);
                isLoading.postValue(false);
            } catch (InterruptedException e) {
                errorMessage.postValue("Fetching transaction details interrupted");
                isLoading.postValue(false);
            }
        }).start();
    }

    public void createTransaction(String senderId, String recipientId, 
                                BigDecimal amount, String sourceCurrency, 
                                String targetCurrency, String purpose, String notes) {
        if (!NetworkUtils.isNetworkAvailable()) {
            errorMessage.setValue("No internet connection available");
            return;
        }

        isLoading.setValue(true);
        
        // API call would go here
        // For demonstration, we'll create a new transaction
        
        // Simulate network delay
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                
                // Create new transaction
                Transaction newTransaction = new Transaction(
                    senderId,
                    recipientId,
                    amount,
                    sourceCurrency,
                    targetCurrency
                );
                
                // Set exchange rate based on currency pair
                if (sourceCurrency.equals("USD") && targetCurrency.equals("EUR")) {
                    newTransaction.setExchangeRate(new BigDecimal("0.92"));
                } else if (sourceCurrency.equals("USD") && targetCurrency.equals("GBP")) {
                    newTransaction.setExchangeRate(new BigDecimal("0.80"));
                } else if (sourceCurrency.equals("USD") && targetCurrency.equals("INR")) {
                    newTransaction.setExchangeRate(new BigDecimal("83.25"));
                } else {
                    newTransaction.setExchangeRate(new BigDecimal("1.00"));
                }
                
                // Calculate fee (simplified as 1% of amount)
                BigDecimal fee = amount.multiply(new BigDecimal("0.01"));
                newTransaction.setFee(fee);
                
                newTransaction.setPurpose(purpose);
                newTransaction.setNotes(notes);
                
                // Set blockchain transaction hash (would be set after blockchain confirmation)
                newTransaction.setBlockchainTxHash("0x" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
                
                // Update status to PROCESSING
                newTransaction.setStatus(Transaction.STATUS_PROCESSING);
                
                // Update current transaction
                currentTransaction.postValue(newTransaction);
                
                // Add to user transactions list
                List<Transaction> currentList = userTransactions.getValue();
                if (currentList != null) {
                    currentList.add(0, newTransaction); // Add at beginning
                    userTransactions.postValue(new ArrayList<>(currentList));
                }
                
                isLoading.postValue(false);
                
                // Simulate transaction completion after some time
                Thread.sleep(3000);
                
                newTransaction.setStatus(Transaction.STATUS_COMPLETED);
                newTransaction.setUpdatedTimestamp(System.currentTimeMillis());
                
                // Update current transaction
                currentTransaction.postValue(newTransaction);
                
                // Update in transactions list
                currentList = userTransactions.getValue();
                if (currentList != null) {
                    int index = -1;
                    for (int i = 0; i < currentList.size(); i++) {
                        if (currentList.get(i).getTransactionId().equals(newTransaction.getTransactionId())) {
                            index = i;
                            break;
                        }
                    }
                    
                    if (index >= 0) {
                        currentList.set(index, newTransaction);
                        userTransactions.postValue(new ArrayList<>(currentList));
                    }
                }
                
            } catch (InterruptedException e) {
                errorMessage.postValue("Transaction creation interrupted");
                isLoading.postValue(false);
            }
        }).start();
    }
}
