package pow_blockchain.services;

import pow_blockchain.services.Block;
import java.util.ArrayList;

public class BlockChain {
    public ArrayList<Block> chain;
    public int difficulty;

    public BlockChain(int difficulty) {
        this.chain = new ArrayList<>();
        this.difficulty = difficulty;
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }

    public boolean isValidChain() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash()) ||
                    !currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> getChainString() {
        //return chain as string array
        ArrayList<String> chainString = new ArrayList<>();
        for (Block block : chain) {
            chainString.add(block.toString());
        }
        return chainString;
    }

    public void setBlockchain(ArrayList<Block> blockchain) {
        this.chain = blockchain;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int size() {
        return chain.size();
    }
    
    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }
}
