package pow_blockchain.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import pow_blockchain.services.BlockChain;
import pow_blockchain.services.Block;

/**
 *
 * @author Thibault Debatty // Phone Myat Khine
 */
public interface RemoteInterface extends Remote{
    public void addBlock(Block block) throws RemoteException;
    public ArrayList<String> getCurrentChain() throws RemoteException;
}
