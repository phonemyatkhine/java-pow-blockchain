package pow_blockchain.services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import pow_blockchain.interfaces.RemoteInterface;
import pow_blockchain.services.BlockChain;
import pow_blockchain.services.Block;

/**
 *
 * @author Thibault Debatty // Phone Myat Khine
 */
public class RemoteHandler extends UnicastRemoteObject implements RemoteInterface {

    private final BlockChain blockchain;

    public RemoteHandler(BlockChain blockchain) throws RemoteException {
        super();
        this.blockchain = blockchain;
    }


    @Override
    public void addBlock(Block block) throws RemoteException {
        blockchain.addBlock(block);
    }

    @Override
    public ArrayList<String> getCurrentChain() throws RemoteException {
        return blockchain.getChainString();
    }

}
