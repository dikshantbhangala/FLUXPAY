package main.java.org.FLUXPAY.Storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fluxpay.db";
    private static final int DATABASE_VERSION = 1;
    
    // Transaction table
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_TRANSACTION_ID = "transaction_id";
    public static final String COLUMN_SENDER_ID = "sender_id";
    public static final String COLUMN_RECIPIENT_ID = "recipient_id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_SOURCE_CURRENCY = "source_currency";
    public static final String COLUMN_TARGET_CURRENCY = "target_currency";
    public static final String COLUMN_EXCHANGE_RATE = "exchange_rate";
    public static final String COLUMN_FEE = "fee";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_BLOCKCHAIN_TX_HASH = "blockchain_tx_hash";
    public static final String COLUMN_CREATED_TIMESTAMP = "created_timestamp";
    public static final String COLUMN_UPDATED_TIMESTAMP = "updated_timestamp";
    public static final String COLUMN_PAYMENT_METHOD = "payment_method";
    public static final String COLUMN_PURPOSE = "purpose";
    public static final String COLUMN_NOTES = "notes";
    
    // Recipient table
    public static final String TABLE_RECIPIENTS = "recipients";
    public static final String COLUMN_RECIPIENT_NAME = "name";
    public static final String COLUMN_RECIPIENT_EMAIL = "email";
    public static final String COLUMN_RECIPIENT_ACCOUNT = "account";
    public static final String COLUMN_RECIPIENT_BANK = "bank";
    public static final String COLUMN_RECIPIENT_COUNTRY = "country";
    public static final String COLUMN_IS_FAVORITE = "is_favorite";
    
    private static DatabaseHelper instance;
    
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create transactions table
        String createTransactionsTable = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COLUMN_TRANSACTION_ID + " TEXT PRIMARY KEY, " +
                COLUMN_SENDER_ID + " TEXT, " +
                COLUMN_RECIPIENT_ID + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_SOURCE_CURRENCY + " TEXT, " +
                COLUMN_TARGET_CURRENCY + " TEXT, " +
                COLUMN_EXCHANGE_RATE + " REAL, " +
                COLUMN_FEE + " REAL, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_BLOCKCHAIN_TX_HASH + " TEXT, " +
                COLUMN_CREATED_TIMESTAMP + " INTEGER, " +
                COLUMN_UPDATED_TIMESTAMP + " INTEGER, " +
                COLUMN_PAYMENT_METHOD + " TEXT, " +
                COLUMN_PURPOSE + " TEXT, " +
                COLUMN_NOTES + " TEXT" +
                ")";
        db.execSQL(createTransactionsTable);
        
        // Create recipients table
        String createRecipientsTable = "CREATE TABLE " + TABLE_RECIPIENTS + " (" +
                COLUMN_RECIPIENT_ID + " TEXT PRIMARY KEY, " +
                COLUMN_RECIPIENT_NAME + " TEXT, " +
                COLUMN_RECIPIENT_EMAIL + " TEXT, " +
                COLUMN_RECIPIENT_ACCOUNT + " TEXT, " +
                COLUMN_RECIPIENT_BANK + " TEXT, " +
                COLUMN_RECIPIENT_COUNTRY + " TEXT, " +
                COLUMN_IS_FAVORITE + " INTEGER" +
                ")";
        db.execSQL(createRecipientsTable);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For simplicity, drop and recreate tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPIENTS);
        onCreate(db);
    }
}
