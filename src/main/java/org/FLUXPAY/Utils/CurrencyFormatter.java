package main.java.org.FLUXPAY.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyFormatter {
    
    public static String formatAmount(BigDecimal amount, String currencyCode) {
        try {
            Currency currency = Currency.getInstance(currencyCode);
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            formatter.setCurrency(currency);
            return formatter.format(amount);
        } catch (IllegalArgumentException e) {
            // Fallback for unknown currency codes
            return String.format("%s %s", currencyCode, amount.setScale(2, RoundingMode.HALF_UP).toString());
        }
    }
    
    public static String formatExchangeRate(BigDecimal rate, String fromCurrency, String toCurrency) {
        return String.format("1 %s = %s %s", 
                            fromCurrency,
                            rate.setScale(4, RoundingMode.HALF_UP).toString(),
                            toCurrency);
    }
    
    public static BigDecimal calculateTargetAmount(BigDecimal sourceAmount, BigDecimal exchangeRate) {
        return sourceAmount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal calculateFee(BigDecimal amount, BigDecimal feePercentage) {
        return amount.multiply(feePercentage.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP))
                    .setScale(2, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal calculateTotalCost(BigDecimal amount, BigDecimal fee) {
        return amount.add(fee).setScale(2, RoundingMode.HALF_UP);
    }
}
