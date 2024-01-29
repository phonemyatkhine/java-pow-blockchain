package pow_blockchain;

import java.util.ArrayList;

public class BlockChain {
    private ArrayList<Block> blockchain;
    private int difficulty;

    public BlockChain(int difficulty) {
        this.blockchain = new ArrayList<>();
        this.difficulty = difficulty;
        addBlock(new Block(0, "Genesis Block", "0"));
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public boolean isValidChain() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash()) ||
                    !currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Block> getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(ArrayList<Block> blockchain) {
        this.blockchain = blockchain;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int size() {
        return blockchain.size();
    }
    
    public Block getLatestBlock() {
        return blockchain.get(blockchain.size() - 1);
    }
}
