package main.java.org.FLUXPAY.Utils;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class BlockchainUtils {
    private static final String ETHEREUM_ADDRESS_PATTERN = "^0x[a-fA-F0-9]{40}$";
    private static final String BITCOIN_ADDRESS_PATTERN = "^[13][a-km-zA-HJ-NP-Z1-9]{25,34}$";
    private static final String XRP_ADDRESS_PATTERN = "^r[0-9a-zA-Z]{24,34}$";
    
    public static boolean isValidEthereumAddress(String address) {
        return Pattern.matches(ETHEREUM_ADDRESS_PATTERN, address);
    }
    
    public static boolean isValidBitcoinAddress(String address) {
        return Pattern.matches(BITCOIN_ADDRESS_PATTERN, address);
    }
    
    public static boolean isValidXrpAddress(String address) {
        return Pattern.matches(XRP_ADDRESS_PATTERN, address);
    }
    
    public static String generateMnemonic() {
        // This is a simplified example - in a real app, use a proper BIP39 implementation
        SecureRandom random = new SecureRandom();
        byte[] entropy = new byte[16];
        random.nextBytes(entropy);
        
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha.digest(entropy);
            
            // First byte of hash determines the checksum bits
            byte checksumByte = hash[0];
            
            // Combine entropy with checksum
            byte[] entropyWithChecksum = new byte[entropy.length + 1];
            System.arraycopy(entropy, 0, entropyWithChecksum, 0, entropy.length);
            entropyWithChecksum[entropy.length] = checksumByte;
            
            return Base64.encodeToString(entropyWithChecksum, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String hashPassword(String password, byte[] salt) {
        try {
            int iterations = 10000;
            char[] chars = password.toCharArray();
            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}