package pow_blockchain.services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import pow_blockchain.interfaces.RemoteInterface;
import pow_blockchain.services.BlockChain;
import pow_blockchain.services.Block;
import java.util.List;
import pow_blockchain.services.Participants;
import java.rmi.Naming;
import java.io.Serializable;
import pow_blockchain.common.BlockData;
/**
 *
 * @author Thibault Debatty // Phone Myat Khine
 */
public class RemoteHandler extends UnicastRemoteObject implements RemoteInterface, Serializable {

    private final int PORT;
    private BlockChain blockchain;
    public List<String> participants;
    public String PARTICIPANTS_FILE;
    public Participants participantsObj;

    public RemoteHandler(BlockChain blockchain, int Port) throws RemoteException {
        super();
        this.PORT = Port;
        this.PARTICIPANTS_FILE = "participants_" + Port + ".ser";
        this.blockchain = blockchain;
        this.participantsObj = new Participants(this.PARTICIPANTS_FILE);
        this.participants = this.participantsObj.getParticipants();
        this.participantsObj.saveParticipants(this.participants);
    }   


    @Override
    public void addBlock(Block block) throws RemoteException {
        this.blockchain.addBlock(block);
    }

    @Override
    public String getChainString() throws RemoteException {
        return  StringUtil.blockchainToJson(this.blockchain.getBlockchain());
    }

    @Override
    public void setBlockChain(BlockChain blockchain) throws RemoteException {
        this.blockchain = blockchain;
    }

    @Override
    public BlockChain getBlockChain() throws RemoteException {
        return this.blockchain;
    }

    @Override 
    public List<String> getParticipants() throws RemoteException {
        return this.participantsObj.getParticipants();
    }

    @Override
    public void updateParticipants(List<String> participants) throws RemoteException {
        this.participantsObj.saveParticipants(participants);
        System.out.println("Updating participants <<REMOTE HANDLER>> : " + participants);
    }

    @Override
    public void broadcastParticipants(List<String> participants) throws RemoteException {
        for (String participant : participants) {
            if (participant.equals("rmi://127.0.0.1:"+ this.PORT+"/blockchain")) {
                continue;
            }
            try {
                RemoteInterface remote = (RemoteInterface) Naming.lookup(
                        participant);
                remote.updateParticipants(participants);
                System.out.println("Broadcasted participants to " + participant);
            } catch (Exception ex) {
                System.err.println("Failed to send to " + participant + "! Moving on...");
            }
        }
    }

    @Override
    public List<BlockData> getTopicConsumption(String topic) throws RemoteException {
        return this.blockchain.getTopicConsumption(topic);
    }

    @Override
    public List<BlockData> getTopicDailyConsumption(String topic, String date) throws RemoteException {
        return this.blockchain.getTopicDailyConsumption(topic, date);
    }

    @Override
    public List<BlockData> getTopicMonthlyConsumption(String topic, String date) throws RemoteException {
        return this.blockchain.getTopicMonthlyConsumption(topic, date);
    }

    @Override
    public List<BlockData> getDeviceConsumption(String deviceId) throws RemoteException {
        return this.blockchain.getDeviceConsumption(deviceId);
    }

    @Override
    public List<BlockData> getDeviceDailyConsumption(String deviceId, String date) throws RemoteException {
        return this.blockchain.getDeviceDailyConsumption(deviceId, date);
    }

    @Override
    public List<BlockData> getDeviceMonthlyConsumption(String deviceId, String date) throws RemoteException {
        return this.blockchain.getDeviceMonthlyConsumption(deviceId, date);
    }
    
}
