package pow_blockchain.services;

import java.util.List;
import java.util.ArrayList;
import pow_blockchain.services.BlockChain;
import pow_blockchain.services.Block;
import pow_blockchain.services.RemoteHandler;
import pow_blockchain.interfaces.RemoteInterface;
import pow_blockchain.services.Participants;

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

public class RMIBasedP2PService{
    
    private BlockChain blockchain = new BlockChain(1);
    private Participants participantsObj;
    private List<String> participants;
    private String PARTICIPANTS_FILE;
    private int port;

    public RMIBasedP2PService(int port) {
        this.port = port;
        this.PARTICIPANTS_FILE = "participants_" + port + ".ser";
        this.participantsObj = new Participants(this.PARTICIPANTS_FILE);
        
        this.participants = new ArrayList<>();
        this.participants.add("rmi://127.0.0.1:"+port+"/blockchain"); 
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

        System.out.println("Broadcast <" + block + "> to " + this.participants);

        for (String participant : this.participants) {
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

    public ArrayList<String> getCurrentChain() {
        try {
            RemoteInterface remote = (RemoteInterface) Naming.lookup(this.participants.get(0));

            ArrayList<String> chain = remote.getCurrentChain();
            if (chain == null) {
                // The chain has not started yet...
                // return initialChainLink();
            }

            return chain;

        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            throw new RuntimeException(ex);
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
            RemoteHandler rh = new RemoteHandler(this.blockchain, port);
            Registry rgsty = LocateRegistry.createRegistry(port);
            rgsty.rebind("blockchain", rh);
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

            System.out.println("Blockchain: " + this.blockchain);
            System.out.println("Genesis Node Blockchain: " + genesisNode.getCurrentChain());
            System.out.println("Genesis Node Blockchain: " + genesisNode.getBlockChain());
            // this.blockchain = genesisNode.getBlockChain();
            // System.out.println("Blockchain: " + this.blockchain);

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
        this.blockchain.addBlock(new Block(1, "Genesis Block", "0"));
        System.out.println("Mining Genesis Block...");
        System.out.println(this.blockchain.getChainString());
        return this.blockchain;
    }
}   
