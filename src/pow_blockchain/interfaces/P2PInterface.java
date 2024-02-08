package pow_blockchain.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import pow_blockchain.services.Block;

/**
 *
 * @author Thibault Debatty // Phone Myat Khine
 */

public interface P2PInterface extends Remote {
    //two methods to be implemented by the server broadcastBlock and getBlockchain
    void broadcastBlock(Block block) throws RemoteException;
    ArrayList<String> getCurrentChain() throws RemoteException;

}
