
public class Linea {

    private Boolean valid;
    private Boolean dirty;
    private int blockSize;
    private int tagSize;
    private String tag;
    private int lru;

    public Boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Boolean getDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getLru() {
        return this.lru;
    }

    public void setLru(int lru) {
        this.lru = lru;
    }

    public Linea(int blockSize, int tagSize) {
        this.valid = false;
        this.dirty = false;
        this.blockSize = blockSize;
        this.tagSize = tagSize;
        this.tag = "";
        this.lru = 0;
    }

}
