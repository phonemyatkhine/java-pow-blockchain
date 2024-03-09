package pow_blockchain.services;

import java.util.Date;
import java.io.Serializable;

public class Block implements Serializable {
    private String indexHash;
    private String mqttTopic;
    private String consumption;
    private String deviceId;
    private long timestamp;
    private String previousHash;
    private String hash;
    private int nonce; // Proof-of-Work variable

    // public Block(int index, String mqttTopic, String consumption, String deviceId, String previousHash) {
    //     this.indexHash = StringUtil.applySha256(String.valueOf(new Date().getTime() + index));
    //     this.mqttTopic = mqttTopic;
    //     this.consumption = consumption;
    //     this.deviceId = deviceId;       
    //     this.timestamp = new Date().getTime();
    //     this.previousHash = previousHash;
    //     this.nonce = 0;
    //     this.hash = calculateHash();
    // }

    public Block(int index, String mqttTopic, String consumption, String deviceId, String previousHash, String timestamp) {
        this.indexHash = StringUtil.applySha256(String.valueOf(new Date().getTime() + index));
        this.mqttTopic = mqttTopic;
        this.consumption = consumption;
        this.deviceId = deviceId;
        this.timestamp = Long.parseLong(timestamp);
        this.previousHash = previousHash;
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                indexHash +
                timestamp +
                mqttTopic +
                consumption +
                deviceId +
                previousHash +
                nonce
        );
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); // Create a string with difficulty * "0"
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getIndexHash() {
        return indexHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMqttTopic() {
        return mqttTopic;
    }

    public String getConsumption() {
        return consumption;
    }

    public String getDeviceId() {
        return deviceId;
    }


    public int getNonce() {
        return nonce;
    }

}
