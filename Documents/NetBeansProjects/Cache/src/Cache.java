public class Cache {
    private int index;
    private int offset;
    private int set;
    private int tag;
    private Linea[] lineas;
    private Linea[][] setLineas;

    public Cache(int blockSize, int cacheSize, Boolean direct, Boolean fully, int setSize, Boolean split) {
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
        this.index = log2(cacheSize);
        this.offset = log2(blockSize);
        
        this.set=0;       
        if (setSize>0){
            this.set = cacheSize/setSize;
        }
       
        this.tag = 32-offset;
        if (setSize>0){
            this.tag -= this.set;
        }
        else{
            this.tag -= this.index;
        }
        
        this.lineas = new Linea[cacheSize];
        
        //setLineas[set][lineas del set]
        if(setSize>0){
            int k=0;
            for (int i=0; i<(cacheSize/this.set); i++){
                for (int j=0; j<this.set ; j++){
                    this.setLineas[i][j] = this.lineas[k];
                    k++;
                }
            }
        }
       
    }
    
    private int log2(int n){
        if(n <= 0) throw new IllegalArgumentException();
        return (int) Math.ceil((Math.log(n)/Math.log(2)));
    }

    public void write(int t, String address, Boolean writeBack, Boolean allocate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}


