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

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Port number is missing!");
            System.err.println("Usage : mvn spring-boot:run -Dspring-boot.run.arguments=<port>");
            System.exit(1);
        }

        int port = Integer.valueOf(args[0]);

        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        ShutdownHook shutdownHook = context.getBean(ShutdownHook.class);
        shutdownHook.registerShutdownHook(port);
        new RMIBasedP2PService(port);

    }
      // private final BlockChain blockchain = new BlockChain(1);

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

    // // Inner class to represent the data for adding a new block
    // static class BlockData {
    //     private String data;

    //     public String getData() {
    //         return data;
    //     }

    //     public void setData(String data) {
    //         this.data = data;
    //     }
    // }
}
