package main.java.org.FLUXPAY.model;

public class PaymentMethod {
    public static final String TYPE_BANK = "BANK";
    public static final String TYPE_CARD = "CARD";
    public static final String TYPE_CRYPTO_WALLET = "CRYPTO_WALLET";
    public static final String TYPE_MOBILE_MONEY = "MOBILE_MONEY";
    
    private String id;
    private String userId;
    private String type;
    private String name;
    private boolean isDefault;
    private String details; // JSON serialized details specific to the payment method type
    private long createdTimestamp;

    // Constructor
    public PaymentMethod(String id, String userId, String type, String name, 
                        boolean isDefault, String details) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.name = name;
        this.isDefault = isDefault;
        this.details = details;
        this.createdTimestamp = System.currentTimeMillis();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
}