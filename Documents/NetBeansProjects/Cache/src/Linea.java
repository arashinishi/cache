public class Linea {
    private Boolean valid;
    private Boolean dirty;
    private int blockSize;
    private int tagSize;
    private String tag;
    
    public Linea(int blockSize, int tag)
    {
        /* funciona igual para todos los casos*/
        this.valid = false;
        this.dirty = false;
        this.blockSize = blockSize;
        this.tagSize = tag;

    }
}
