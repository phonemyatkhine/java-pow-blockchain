package pow_blockchain;

public class Main {
    public static void main(String[] args) {
        // Create a blockchain with difficulty level 4
        BlockChain blockchain = new BlockChain(1);

        // Add some blocks to the blockchain
        for (int i = 0; i < 50; i++) {
         blockchain.addBlock(new Block(i, "Block "+i+" Data", blockchain.getBlockchain().get(blockchain.getBlockchain().size() - 1).getHash()));     
        }

        // Print the blockchain and check its validity
        System.out.println("Blockchain:");
        System.out.println(StringUtil.toJson(blockchain.getBlockchain())); // Assuming you have a class to convert the blockchain to JSON
        System.out.println("Is Blockchain Valid? " + blockchain.isValidChain());
    } 
}
