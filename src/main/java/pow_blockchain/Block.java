package pow_blockchain;
import java.util.Date;

public class Block {
    private String indexHash;
    private long timestamp;
    private String data;
    private String previousHash;
    private String hash;
    private int nonce; // Proof-of-Work variable

    public Block(int index, String data, String previousHash) {
        this.indexHash = StringUtil.applySha256(String.valueOf(new Date().getTime() + index));
        this.timestamp = new Date().getTime();
        this.data = data;
        this.previousHash = previousHash;
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                indexHash +
                timestamp +
                data +
                previousHash +
                nonce
        );
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); // Create a string with difficulty * "0"
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getIndexHash() {
        return indexHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public int getNonce() {
        return nonce;
    }

}
