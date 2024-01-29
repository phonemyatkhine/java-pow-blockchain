package pow_blockchain;
import java.util.ArrayList;
import java.security.MessageDigest;

public class StringUtil {
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(ArrayList<Block> blockchain) {
        String json = "[";
        for (int i = 0; i < blockchain.size(); i++) {
            json += "{";
            json += "\"index\": " + blockchain.get(i).getIndexHash() + ",";
            json += "\"timestamp\": " + blockchain.get(i).getTimestamp() + ",";
            json += "\"data\": \"" + blockchain.get(i).getData() + "\",";
            json += "\"previousHash\": \"" + blockchain.get(i).getPreviousHash() + "\",";
            json += "\"hash\": \"" + blockchain.get(i).getHash() + "\",";
            json += "\"nonce\": " + blockchain.get(i).getNonce();
            json += "}";
            if (i != blockchain.size() - 1) json += ",";
        }
        json += "]";
        return json;
    }
    
}
