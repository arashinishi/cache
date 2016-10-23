public class Cache {

    Cache(int blockSize, int cacheSize, Boolean writeBack, Boolean direct, Boolean fully, int setSize, Boolean allocate, Boolean split) {
        //Comprobar si se especificó el block size y cache size
        if (blockSize == 0 && cacheSize == 0){
            System.out.println("Error: Block Size and Cache Size not especified");
            System.exit(1);
        }
        else if(blockSize == 0){
            System.out.println("Error: Block Size not especified");
            System.exit(1);
        }
        else if(cacheSize == 0){
            System.out.println("Error: Cache Size not especified");
            System.exit(1);
        }
        
        //Calculo del largo de tag, index, set y tag
        int index = log2(cacheSize);
        int offset = log2(blockSize);
        
        int set=0;       
        if (setSize>0)
            set = cacheSize/setSize;
       
        int tag = 32-offset;
        if (setSize>0){
            tag-=set;
        }
        else{
            tag-=index;
        }
       
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


