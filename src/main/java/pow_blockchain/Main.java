package pow_blockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class Main {

    private final BlockChain blockchain = new BlockChain(1);

    @PostMapping("/addBlock")
    public String addBlock(@RequestBody BlockData blockData) {
        Block newBlock = new Block(blockchain.size(), blockData.getData(), blockchain.getLatestBlock().getHash());
        blockchain.addBlock(newBlock);
        return "Block added successfully.";
    }

    @GetMapping("/getBlockchain")
    public String getBlockchain() {
        return StringUtil.toJson(blockchain.getBlockchain());
    }

    @GetMapping("/isValid")
    public String isValid() {
        return "Is Blockchain Valid? " + blockchain.isValidChain();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    // Inner class to represent the data for adding a new block
    static class BlockData {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
