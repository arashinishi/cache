package cache;

public class Linea {
    
    private int size;
    private int offsetSize;
    private int indexSize;
    private int tagSize;
    public int[] linea;
    
    public Linea(int set, int blockSize, int cacheSize)
    {
        /* funciona igual para todos los casos*/
        this.offsetSize = Cache.log2(blockSize);
        this.indexSize = Cache.log2(cacheSize);
        this.tagSize = 32 - this.indexSize - set - this.offsetSize;
        this.size = this.offsetSize + this.indexSize + this.tagSize;

        this.linea = new int[size];
    }
}
