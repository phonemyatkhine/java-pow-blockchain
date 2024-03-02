// package pow_blockchain.controllers;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.beans.factory.annotation.Autowired;
// import pow_blockchain.services.RMIBasedP2PService;
// import org.springframework.web.bind.annotation.PathVariable;
// import pow_blockchain.common.BlockData;
// import pow_blockchain.services.BlockChain;
// import pow_blockchain.services.Block;
// import pow_blockchain.services.StringUtil;

// @RestController
// @RequestMapping("/")
// public class BlockchainController {
//     @Autowired
//     private  RMIBasedP2PService P2PService;


//     // @GetMapping("/getTodayConsumptionWithTopic/{topic}")
//     // public String getTodayConsumptionWithTopic(@PathVariable String topic) {
//     //     return "Today's consumption with topic: " + P2PService.getTodayConsumptionWithTopic(topic);
//     // }

//     // @PostMapping("/addBlock")
//     // public String addBlock(@RequestBody BlockData blockData) {
//     //     Block newBlock = new Block(blockchain.size(), blockData.getData(), blockchain.getLatestBlock().getHash());
//     //     blockchain.addBlock(newBlock);
//     //     return "Block added successfully.";
//     // }

//     // @GetMapping("/getBlockchain")
//     // public String getBlockchain() {
//     //     return StringUtil.toJson(blockchain.getBlockchain());
//     // }

//     // @GetMapping("/isValid")
//     // public String isValid() {
//     //     return "Is Blockchain Valid? " + blockchain.isValidChain();
//     // }
// }    