package pow_blockchain.components;

import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class ParticipantsCleanUp {

    public static final String PARTICIPANTS_FILE = "participants_";

    public void performCleanup(int port) {
        System.out.println("Cleaning up participants...");
        //delete file from path PARTICIPANTS_FILE
        System.out.println("Deleting file: " + PARTICIPANTS_FILE + port + ".ser");
        File fileToDelete = new File(PARTICIPANTS_FILE + port + ".ser");

        if(!fileToDelete.exists()) {
            System.out.println("File does not exist");
        }

        try {
            boolean deleted = fileToDelete.delete();
            if (deleted) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
        } catch (Exception e) {
            System.err.println("Failed to delete the file");
        }

    }
}
