package cache;

public class Cache {

    public Cache(int bs, int cs) {
        int index = log2(cs);
        int offset= log2(bs);
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


