package pow_blockchain.services;

import java.util.List;
import java.util.ArrayList;
import pow_blockchain.services.BlockChain;
import pow_blockchain.services.Block;
import pow_blockchain.services.RemoteHandler;
import pow_blockchain.interfaces.RemoteInterface;
import pow_blockchain.services.Participants;
import pow_blockchain.common.BlockData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Date;

public class RMIBasedP2PService{
    
    private BlockChain blockchain = new BlockChain(1);
    private Participants participantsObj;
    private List<String> participants;
    private String PARTICIPANTS_FILE;
    private int port;
    private RemoteHandler remoteHandler;
    private String selfRmi;
    
    public RMIBasedP2PService(int port) {
        this.port = port;
        this.PARTICIPANTS_FILE = "participants_" + port + ".ser";
        this.participantsObj = new Participants(this.PARTICIPANTS_FILE);
        this.selfRmi = "rmi://127.0.0.1:"+port+"/blockchain";
        this.participants = new ArrayList<>();
        this.participants.add(this.selfRmi); 
        this.participantsObj.saveParticipants(this.participants);

        System.out.println("Participants: " + this.participants);
        System.out.println("P2P Service started on port " + port);

        try {
            startRMIHandler(port);
        } catch (Exception e) {
            System.err.println(e);        
        }
        
    }   
    
    public void broadcastBlock(Block block) {
        try {
            this.participants = this.remoteHandler.getParticipants();
            System.out.println("Participantz: " + participants);
        } catch (Exception e) {
            System.err.println(e);
        }
        System.out.println("Broadcast <" + block + "> to " +  this.participants);
        for (String participant :  this.participants) {
            System.out.println("Self RMI: " + this.selfRmi + ".Participant: " + participant);
            if (!participant.equals(this.selfRmi)) {
                System.out.println("Sending <"+ block+ "> to " + participant);
                try {
                    RemoteInterface remote = (RemoteInterface) Naming.lookup(
                            participant);
                    remote.addBlock(block);
                } catch (RemoteException ex) {
                    System.err.println("Failed to send to " + participant + "! Moving on...");
                } catch (NotBoundException | MalformedURLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * Start the RMI server and listen on mentioned port.
     * @param port
     * @throws RemoteException
     */
    private void startRMIHandler(final int port) throws RemoteException, java.rmi.NotBoundException, java.net.MalformedURLException{

        if (port == 10051) { // if this is the genesis node, create the genesis block
           this.blockchain = mineGenesisBlock();
        } 
        try {
            this.remoteHandler = new RemoteHandler(this.blockchain, port);
            Registry rgsty = LocateRegistry.createRegistry(port);
            rgsty.rebind("blockchain", remoteHandler);
            System.out.println("Registry created and bound successfully at port: " + port);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.err.println("Failed to create registry: " + e.getMessage());
        } 

        try {
            if (port == 10051) { return; }
            registerOnGenesisNode("rmi://127.0.0.1:10051/blockchain", port);
        } catch (java.rmi.NotBoundException e) {
            System.err.println("Failed to create registry: " + e.getMessage());
            throw e; // Optionally, rethrow if you want the caller to handle it
        } catch (java.net.MalformedURLException e) {
            // Handle MalformedURLException
            throw e; // Optionally, rethrow if you want the caller to handle it
        }

    }

    public void registerOnGenesisNode(String genesisNodePath, int port) throws java.rmi.NotBoundException, java.net.MalformedURLException, java.rmi.RemoteException {
        try {
            RemoteInterface genesisNode = (RemoteInterface) Naming.lookup(genesisNodePath);
            System.out.println("Registering to genesisNodePath : " + genesisNodePath);
            System.out.println("Current Node Participants: " + this.participants);

            List<String> genesisParticipants = genesisNode.getParticipants();
            System.out.println("Genesis Participants: " + genesisParticipants);
            this.participants.addAll(genesisParticipants);
            System.out.println("Updated Participants: " + this.participants);
            
            genesisNode.updateParticipants(this.participants);
            genesisNode.broadcastParticipants(this.participants);

            try {
                this.blockchain = genesisNode.getBlockChain();
                this.remoteHandler.setBlockChain(this.blockchain);
                System.out.println("Blockchain: "+ this.blockchain.getChainString());
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (java.rmi.RemoteException e) {
            System.err.println("Failed to send to " + genesisNodePath + "! Can't register...");
        } catch (Exception e) {
            System.err.println(e);
            throw e;
        }
    }

    public List<String> getParticipants() {
        return this.participantsObj.getParticipants();
    }

    public BlockChain mineGenesisBlock() {
        this.blockchain.addBlock(new Block(1,"GenesisBlock", "0", "0", "0", new Date().getTime() + ""));
        // this.blockchain.addBlock(new Block(this.blockchain.size(), "testTopic", "1.5", "1A", this.blockchain.getLatestBlock().getHash()));
        // this.blockchain.addBlock(new Block(this.blockchain.size(), "testTopic", "2.5", "1A", this.blockchain.getLatestBlock().getHash()));
        // this.blockchain.addBlock(new Block(this.blockchain.size(), "testTopic", "3.5", "1A", this.blockchain.getLatestBlock().getHash()));
        // this.blockchain.addBlock(new Block(this.blockchain.size(), "testTopic", "3.5", "1A", this.blockchain.getLatestBlock().getHash()));
        System.out.println("Mining Genesis Block...");
        System.out.println(this.blockchain.getChainString());
        return this.blockchain;
    }
    
    public void mineBlock(String blockData) {
        BlockData data = StringUtil.jsonToBlockData(blockData);
        String mqttTopic = data.getMqttTopic();
        String consumption = data.getConsumption();
        String deviceId = data.getDeviceId();
        //if timestamp exists in data, use it. else use current time
        String timestamp = data.getTimestamp() != null ? data.getTimestamp() : new Date().getTime() + "";
        Block newBlock = new Block(this.blockchain.size(), mqttTopic, consumption, deviceId, this.blockchain.getLatestBlock().getHash(), timestamp);
        this.blockchain.addBlock(newBlock);
        broadcastBlock(newBlock);
    }

    public String getChainString() {
        try {
            return this.remoteHandler.getChainString();
        } catch (Exception e) {
            System.err.println(e);
        }
        return "Error"; 
    }

    public List<Block> getBlockchain() {
        return this.blockchain.getBlockchain();
    }

    public List<BlockData> getTopicConsumption(String topic) {
        try {
            return this.remoteHandler.getTopicConsumption(topic);
        } catch (Exception e) {
            System.err.println(e);
        }
        return new ArrayList<BlockData>();
    }

    public List<BlockData> getTopicDailyConsumption(String topic, String date) {
        try {
            return this.remoteHandler.getTopicDailyConsumption(topic, date);
        } catch (Exception e) {
            System.err.println(e);
        }
        return new ArrayList<BlockData>();
    }

    public List<BlockData> getTopicMonthlyConsumption(String topic, String date) {
        try {
            return this.remoteHandler.getTopicMonthlyConsumption(topic, date);
        } catch (Exception e) {
            System.err.println(e);
        }
        return new ArrayList<BlockData>();
    }

    public List<BlockData> getDeviceConsumption(String deviceId) {
        try {
            return this.remoteHandler.getDeviceConsumption(deviceId);
        } catch (Exception e) {
            System.err.println(e);
        }
        return new ArrayList<BlockData>();
    }

    public List<BlockData> getDeviceDailyConsumption(String deviceId, String date) {
        try {
            return this.remoteHandler.getDeviceDailyConsumption(deviceId, date);
        } catch (Exception e) {
            System.err.println(e);
        }
        return new ArrayList<BlockData>();
    }

    public List<BlockData> getDeviceMonthlyConsumption(String deviceId, String date) {
        try {
            return this.remoteHandler.getDeviceMonthlyConsumption(deviceId, date);
        } catch (Exception e) {
            System.err.println(e);
        }
        return new ArrayList<BlockData>();
    }
}   
