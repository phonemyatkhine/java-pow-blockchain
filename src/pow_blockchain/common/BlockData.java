package pow_blockchain.common;

public class BlockData {
    private String mqttTopic;
    private String consumption;
    private String deviceId;
    private String timestamp;

    // Constructor
    public BlockData(String mqttTopic, String consumption, String deviceId, String timestamp) {
        this.mqttTopic = mqttTopic;
        this.consumption = consumption;
        this.deviceId = deviceId;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getMqttTopic() {
        return mqttTopic;
    }

    public void setMqttTopic(String mqttTopic) {
        this.mqttTopic = mqttTopic;
    }


    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
