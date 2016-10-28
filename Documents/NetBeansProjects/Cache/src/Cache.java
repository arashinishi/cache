public class Cache {
    private int indexSize;
    private int offsetSize;
    private int setSize;
    private int tagSize;
    private Linea[] lineas;
    private Linea[][] setLineas;
    private Boolean isDirect;
    private Boolean isSet;
    private Boolean isFully;
    private Boolean isSplit;
    //Si es split 
    private Linea[] datos;
    private Linea[] instrucciones;
    //Si es split y set associative
    private Linea[][] setDatos;
    private Linea[][] setInstrucciones;
    

    public Cache(int blockSize, int cacheSize, Boolean direct, Boolean fully, int numberOfSets, Boolean split) {
        //Comprobar si se especificó el block size y cache size
        if (blockSize == 0 && cacheSize == 0){
            System.out.println("Error: Block size and cache size aren't especified");
            System.exit(1);
        }
        else if(blockSize == 0){
            System.out.println("Error: Block Size isn't especified");
            System.exit(1);
        }
        else if(cacheSize == 0){
            System.out.println("Error: Cache size isn't especified");
            System.exit(1);
        }
        //Revisar que bs, cs y la cantidad de sets no son negativos
        else if(blockSize < 0){
            System.out.println("Error: The block size can´t be negative");
            System.exit(1);
        }
        else if(cacheSize < 0){
            System.out.println("Error: The cache size can't be negative");
            System.exit(1);
        }
        else if(numberOfSets < 0){
            System.out.println("Error: The number of sets can't be negative");
            System.exit(1);
        }
        
        //Tipo de cache
        this.isDirect = direct;
        this.isFully = fully;
        if (numberOfSets > 0)
            this.isSet = true;
        else
            this.isSet = false;
        
        
        //Calculo del largo de tag, index, set y tag
        this.indexSize = log2(cacheSize);
        this.offsetSize = log2(blockSize);
        this.setSize=0;  
        if (this.isSet){
            if(cacheSize%numberOfSets != 0){
                System.out.println("Error: The cache isn't divisible in the number of sets");
                System.exit(1);
            }
            this.setSize = cacheSize/numberOfSets;
        }
        this.tagSize = 32-offsetSize;
        if (this.isSet)
            this.tagSize -= this.setSize;             
        else
            this.tagSize -= this.indexSize;
        
        
        //Lineas del cache
        this.lineas = new Linea[cacheSize];
        
        //Si es set associative, dividir el cache en sets.
        //setLineas[set][lineas del set]
        if(this.isSet){
            int k=0;
            for (int i=0; i<(cacheSize/this.setSize); i++){
                for (int j=0; j<this.setSize ; j++){
                    this.setLineas[i][j] = this.lineas[k];
                    k++;
                }
            }
        }
        
        this.isSplit = split;
        //Si es split sin ser set associative, las lineas son individuales
        if (this.isSplit && !this.isSet){
            this.datos = new Linea[cacheSize/2];
            this.instrucciones = new Linea[cacheSize/2];            
        }
        //Si es split y set associative, se divide el cache en sets
        else if (this.isSplit && this.isSet){
            
        }
       
    }
    
    private int log2(int n){
        if(n <= 0) throw new IllegalArgumentException();
        
        int log = 31 - Integer.numberOfLeadingZeros(n);
        
        if (n%Math.pow(2,log) == 0)
            return log;
        
        else
            return log+1;
        
    }

    public void access(int t, String address, Boolean writeBack, Boolean allocate) {
        if (this.isDirect){
        }
        else if (this.isSet){
            //Obtener el set desde el address
            int set = 0; //Calcular
            int posicionHit = search(address, this.setLineas[set]);
        }
        else{
            int posicionHit = search(address, this.lineas);
        }
        
    }
    
    private int search(String address, Linea[] linea){        
        String addressTag = address.substring(0,this.tagSize);
        for (int i=0; i<linea.length; i++){
            if(addressTag.compareTo(linea[i].getTag())==0)
                return i;            
        }    
        return -1;
    }
    
    
    
}


