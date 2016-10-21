package cache;

public class Cache {

    Cache(int blockSize, int cacheSize, Boolean writeBack, Boolean direct, Boolean fully, int set, Boolean allocate, Boolean split) {
       int index = log2(cacheSize);
        int offset= log2(blockSize);
        int tag=32-index-offset;
    }

    void write(Boolean isWriteAllocate, Boolean isWriteBack){    
    }
    
    void read(){
    }
    
    public static int log2(int n){
        if(n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }
    
    
    
}


