package main.java.org.FLUXPAY.model;

import java.math.BigDecimal;

public class Currency {
    private String code;
    private String name;
    private String symbol;
    private BigDecimal exchangeRateToUSD;
    private boolean isSupported;
    private String countryCode;
    private String flagEmoji;

    // Constructor
    public Currency(String code, String name, String symbol, BigDecimal exchangeRateToUSD, 
                   boolean isSupported, String countryCode, String flagEmoji) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.exchangeRateToUSD = exchangeRateToUSD;
        this.isSupported = isSupported;
        this.countryCode = countryCode;
        this.flagEmoji = flagEmoji;
    }

    // Getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getExchangeRateToUSD() {
        return exchangeRateToUSD;
    }

    public void setExchangeRateToUSD(BigDecimal exchangeRateToUSD) {
        this.exchangeRateToUSD = exchangeRateToUSD;
    }

    public boolean isSupported() {
        return isSupported;
    }

    public void setSupported(boolean supported) {
        isSupported = supported;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFlagEmoji() {
        return flagEmoji;
    }

    public void setFlagEmoji(String flagEmoji) {
        this.flagEmoji = flagEmoji;
    }
}