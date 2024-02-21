package pow_blockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ConfigurableApplicationContext;

import pow_blockchain.hooks.ShutdownHook;
import pow_blockchain.services.RMIBasedP2PService;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class Main {
    private static RMIBasedP2PService P2PService;
    // private static RMIBasedP2PService P2PService;
    // private final BlockChain blockchain = new BlockChain(1);
    
    @GetMapping("/checkParticipants")
    public String checkParticipants() {
        return "Participants: " + P2PService.getParticipants();
    }


    // @PostMapping("/addBlock")
    // public String addBlock(@RequestBody BlockData blockData) {
    //     Block newBlock = new Block(blockchain.size(), blockData.getData(), blockchain.getLatestBlock().getHash());
    //     blockchain.addBlock(newBlock);
    //     return "Block added successfully.";
    // }

    // @GetMapping("/getBlockchain")
    // public String getBlockchain() {
    //     return StringUtil.toJson(blockchain.getBlockchain());
    // }

    // @GetMapping("/isValid")
    // public String isValid() {
    //     return "Is Blockchain Valid? " + blockchain.isValidChain();
    // }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Port number is missing!");
            System.err.println("Usage : mvn spring-boot:run -D spring-boot.run.arguments=\"<rmiPort> <port>\"");
            // System.exit(1);
        }
        int rmiPort = args[0].equals("null") ? 10051 : Integer.valueOf(args[0]);
        int webPort = args[1].equals("null") ? 8080 : Integer.valueOf(args[1]);
        System.out.println("RMI_PORT: " + rmiPort);
        System.out.println("PORT: " + webPort);
    
        System.getProperties().put( "server.port", webPort );
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        ShutdownHook shutdownHook = context.getBean(ShutdownHook.class);
        shutdownHook.registerShutdownHook(rmiPort);
    
        P2PService = new RMIBasedP2PService(rmiPort);
    }
   
}
