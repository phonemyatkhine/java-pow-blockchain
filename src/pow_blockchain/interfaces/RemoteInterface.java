package pow_blockchain.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import pow_blockchain.services.BlockChain;
import pow_blockchain.services.Block;
import java.util.List;
import pow_blockchain.common.BlockData;
/**
 *
 * @author Thibault Debatty // Phone Myat Khine
 */
public interface RemoteInterface extends Remote{
    public void addBlock(Block block) throws RemoteException;
    public String getChainString() throws RemoteException;
    public void setBlockChain(BlockChain blockchain) throws RemoteException;
    public BlockChain getBlockChain() throws RemoteException;
    public void updateParticipants(List<String> participants) throws RemoteException;
    public List<String> getParticipants() throws RemoteException;
    public void broadcastParticipants(List<String> participants) throws RemoteException;
    public List<BlockData> getTopicConsumption(String topic) throws RemoteException;
    public List<BlockData> getTopicDailyConsumption(String topic, String date) throws RemoteException;
    public List<BlockData> getTopicMonthlyConsumption(String topic, String date) throws RemoteException;
}
