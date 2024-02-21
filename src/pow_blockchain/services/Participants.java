package pow_blockchain.services;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Participants {
    private String PARTICIPANTS_FILE;

    public Participants(String FILE_NAME) {
        this.PARTICIPANTS_FILE = FILE_NAME;
    }

    public List<String> getParticipants() {
        try {
            FileInputStream file = new FileInputStream(this.PARTICIPANTS_FILE);
            ObjectInputStream objectIn = new ObjectInputStream(file);
            List<String> participants = (List<String>) objectIn.readObject();
            System.out.println(
                "Read participants from file: " + participants
            );
            objectIn.close();
            return participants;
        } catch (FileNotFoundException ex) {
            return new LinkedList<>();
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void saveParticipants(List<String> participants) {
        //do not overwrite participants. append into participants file
        try {
            FileOutputStream fileOut = new FileOutputStream(this.PARTICIPANTS_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(participants);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
        // try {
        //     FileOutputStream fileOut = new FileOutputStream(PARTICIPANTS_FILE);
        //     ObjectOutputStream out = new ObjectOutputStream(fileOut);
        //     out.writeObject(participants);
        //     out.close();
        //     fileOut.close();
        // } catch (IOException i) {
        //     i.printStackTrace();
        // }
    }
}