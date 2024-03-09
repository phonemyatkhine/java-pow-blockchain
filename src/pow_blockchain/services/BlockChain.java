package pow_blockchain.services;

import pow_blockchain.services.Block;
import java.util.ArrayList;
import pow_blockchain.services.StringUtil;
import java.io.Serializable;
import java.util.List;
import pow_blockchain.common.BlockData;

public class BlockChain implements Serializable {
    public ArrayList<Block> chain;
    public int difficulty;

    public BlockChain(int difficulty) {
        this.chain = new ArrayList<>();
        this.difficulty = difficulty;
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }

    public boolean isValidChain() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash()) ||
                    !currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    public String getChainString() {
        return StringUtil.blockchainToJson(this.chain);
    }

    public ArrayList<Block> getBlockchain() {
        return this.chain;
    }

    public void setBlockchain(ArrayList<Block> blockchain) {
        this.chain = blockchain;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int size() {
        return chain.size();
    }
    
    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public List<BlockData> getTopicConsumption(String topic) {
        List<BlockData> topicConsumption = new ArrayList<>();
        for (Block block : chain) {
            if (block.getMqttTopic().equals(topic)) {
                long timestamp = block.getTimestamp();
                String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));             
                topicConsumption.add(new BlockData(block.getMqttTopic(), block.getConsumption(), block.getDeviceId(), date));
            }
        }
        return topicConsumption;
    }

    public List<BlockData> getTopicDailyConsumption(String topic, String date) {
        System.out.println("Date: " + date);
        System.out.println("Topic: " + topic);
        List<BlockData> topicDailyConsumption = new ArrayList<>();
        for (Block block : chain) {
            System.out.println("Block: " + block.getMqttTopic() + " " + block.getConsumption() + " " + block.getDeviceId() + " " + new java.util.Date(block.getTimestamp()));
            long timestamp = block.getTimestamp();
            String dateBlock = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
            //block mqtt topic equals and date includes in dateblock
            if (block.getMqttTopic().equals(topic) && dateBlock.contains(date)) {
                topicDailyConsumption.add(new BlockData(block.getMqttTopic(), block.getConsumption(), block.getDeviceId(), dateBlock));
            }
        }
        return topicDailyConsumption;
    }

    public List<BlockData> getTopicMonthlyConsumption(String topic, String date) {
        System.out.println("Date: " + date);
        List<BlockData> topicMonthlyConsumption = new ArrayList<>();
        for (Block block : chain) {
            long timestamp = block.getTimestamp();
            String dateBlock = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
            System.out.println("Date: " + dateBlock);
            if (block.getMqttTopic().equals(topic) && dateBlock.contains(date)) {
                topicMonthlyConsumption.add(new BlockData(block.getMqttTopic(), block.getConsumption(), block.getDeviceId(), dateBlock));
            }
        }
        return topicMonthlyConsumption;
    }

    public List<BlockData> getDeviceConsumption(String deviceId) {
        List<BlockData> deviceConsumption = new ArrayList<>();
        for (Block block : chain) {
            if (block.getDeviceId().equals(deviceId)) {
                long timestamp = block.getTimestamp();
                String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
                deviceConsumption.add(new BlockData(block.getMqttTopic(), block.getConsumption(), block.getDeviceId(), date));
            }
        }
        return deviceConsumption;
    }

    public List<BlockData> getDeviceDailyConsumption(String deviceId, String date) {
        System.out.println("Date: " + date);
        List<BlockData> deviceDailyConsumption = new ArrayList<>();
        for (Block block : chain) {
            long timestamp = block.getTimestamp();
            String dateBlock = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
            if (block.getDeviceId().equals(deviceId) && dateBlock.contains(date)) {
                deviceDailyConsumption.add(new BlockData(block.getMqttTopic(), block.getConsumption(), block.getDeviceId(), dateBlock));
            }
        }
        return deviceDailyConsumption;
    }

    public List<BlockData> getDeviceMonthlyConsumption(String deviceId, String date) {
        System.out.println("Date: " + date);
        List<BlockData> deviceMonthlyConsumption = new ArrayList<>();
        for (Block block : chain) {
            long timestamp = block.getTimestamp();
            String dateBlock = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
            if (block.getDeviceId().equals(deviceId) && dateBlock.contains(date)) {
                deviceMonthlyConsumption.add(new BlockData(block.getMqttTopic(), block.getConsumption(), block.getDeviceId(), dateBlock));
            }
        }
        return deviceMonthlyConsumption;
    }
    
}

