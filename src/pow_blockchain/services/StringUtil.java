package pow_blockchain.services;

import java.util.ArrayList;
import java.security.MessageDigest;
import pow_blockchain.common.BlockData;

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

    public static String blockchainToJson(ArrayList<Block> blockchain) {
        String json = "[";
        for (int i = 0; i < blockchain.size(); i++) {
            json += "{";
            json += "\"index\": " + blockchain.get(i).getIndexHash() + ",";
            json += "\"mqttTopic\": \"" + blockchain.get(i).getMqttTopic() + "\",";
            json += "\"consumption\": " + blockchain.get(i).getConsumption() + ",";
            json += "\"deviceId\": \"" + blockchain.get(i).getDeviceId() + "\",";
            json += "\"previousHash\": \"" + blockchain.get(i).getPreviousHash() + "\",";
            json += "\"hash\": \"" + blockchain.get(i).getHash() + "\",";
            json += "\"nonce\": " + blockchain.get(i).getNonce();
            json += "}";
            if (i != blockchain.size() - 1) json += ",";
        }
        json += "]";
        return json;
    }

    public static String blockDataToJson(BlockData data) {

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"mqttTopic\":\"").append(data.getMqttTopic()).append("\",");
        sb.append("\"consumption\":").append(data.getConsumption()).append(",");
        sb.append("\"deviceId\":\"").append(data.getDeviceId()).append("\"");
        sb.append("\"timestamp\":\"").append(data.getTimestamp()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    public static BlockData jsonToBlockData(String json) {
        String[] parts = json.split(",");
        String mqttTopic = parts[0].split(":")[1];
        String consumption = parts[1].split(":")[1];
        String deviceId = parts[2].split(":")[1];
        String timestamp = parts[3].split(":")[1];
        return new BlockData(mqttTopic, consumption, deviceId, timestamp);
    }
    
    
}
