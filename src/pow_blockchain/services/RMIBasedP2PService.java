package pow_blockchain.services;

import java.util.List;
import java.util.ArrayList;
import pow_blockchain.interfaces.P2PInterface;
import pow_blockchain.services.BlockChain;
import pow_blockchain.services.Block;
import pow_blockchain.services.RemoteHandler;
import pow_blockchain.interfaces.RemoteInterface;

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


public class RMIBasedP2PService implements P2PInterface {
    
    public String PARTICIPANTS_FILE = "participants.ser";
    private BlockChain blockchain = new BlockChain(1);
    private List<String> participants;
    private int port;

    public RMIBasedP2PService(int port) {
        this.port = port;
        this.participants = getParticipants();
        this.PARTICIPANTS_FILE = PARTICIPANTS_FILE + "_" + port + ".ser";
        try {
            startRMIHandler(port);
        } catch (Exception e) {
            System.err.println(e);        
        }
        
        registerAsParticipant(port);
    }   
    

    @Override
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

    @Override
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

    private BlockChain initialChainLink() {
        // Block root = new Block();
        return blockchain;
    }

    /**
     * Start the RMI server and listen on mentioned port.
     * @param port
     * @throws RemoteException
     */
    private void startRMIHandler(final int port) throws RemoteException {
        RemoteHandler rh = new RemoteHandler(blockchain);
        try {
            Registry rgsty = LocateRegistry.createRegistry(port);
            rgsty.rebind("powchain", rh);
            System.out.println("Registry created and bound successfully.");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.err.println("Failed to create registry: " + e.getMessage());
        }
    }

    /**
     * Add myself (my port) to the list of participants.
     * @param port
     */
    private void registerAsParticipant(int port) {
        String myself = "rmi://127.0.0.1: ," + String.valueOf(port) + "/blockchain";
        List<String> participants = getParticipants();
        for (String participant : participants) {
            if (participant.equals(myself)) {
                // I'm already in the list, nothing to do...
                return;
            }
        }

        participants.add(myself);
        saveParticipants(participants);
    }

    /**
     * Read the list of participants from the shared file.
     * @return
     */
    private List<String> getParticipants() {
        try {
            FileInputStream file = new FileInputStream(PARTICIPANTS_FILE);
            ObjectInputStream objectIn = new ObjectInputStream(file);
            List<String> participants = (List<String>) objectIn.readObject();
            objectIn.close();
            return participants;

        } catch (FileNotFoundException ex) {
            // file does not exist => I'm the first participant
            return new LinkedList<>();

        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Save the list of participants to the shared file.
     * @param participants
     */
    private void saveParticipants(List<String> participants) {
        try {
            FileOutputStream fileOut = new FileOutputStream(PARTICIPANTS_FILE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(participants);
            objectOut.close();

        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}   
