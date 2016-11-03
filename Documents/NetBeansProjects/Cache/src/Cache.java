
public class Cache {

    private int indexSize;
    private int offsetSize;
    private int numberOfSets;
    private int setNWays;
    private int setSize;
    private int tagSize;
    private int blockSize;
    private int cacheSize;

    //Retornos
    public int nMissInstrucciones;
    public int nMissDatos;
    public int nWordsDesdeMemoria;
    public int nWordsAMemoria;

    //Tipo de cache
    private Boolean isDirect;
    private Boolean isSet;
    private Boolean isFully;

    private Boolean isSplit;
    //Si no es split y no es set associative
    private Linea[] lineas;
    //Si no es split y es set associative
    private Linea[][] setLineas;
    //Si es split y no es set associative
    private Linea[] datos;
    private Linea[] instrucciones;
    //Si es split y set associative
    private Linea[][] setDatos;
    private Linea[][] setInstrucciones;

    public Cache(int blockSize, int cacheSize, Boolean direct, Boolean fully, int numberOfSets, Boolean split) {
        this.blockSize = blockSize;
        this.cacheSize = cacheSize;
        this.numberOfSets = numberOfSets;
        //Comprobar si se especificó el block size y cache size
        if (this.blockSize == 0 && this.cacheSize == 0) {
            System.out.println("Error: Block size and cache size aren't specified");
            System.exit(1);
        }
        else if (this.blockSize == 0) {
            System.out.println("Error: Block Size isn't specified");
            System.exit(1);
        }
        else if (this.cacheSize == 0) {
            System.out.println("Error: Cache size isn't specified");
            System.exit(1);
        } //Revisar que bs, cs y la cantidad de sets no son negativos
        else if (this.blockSize < 0) {
            System.out.println("Error: The block size can´t be negative");
            System.exit(1);
        }
        else if (this.cacheSize < 0) {
            System.out.println("Error: The cache size can't be negative");
            System.exit(1);
        }
        else if (this.numberOfSets < 0) {
            System.out.println("Error: The number of sets can't be negative");
            System.exit(1);
        }

        isPowerOf2(this.cacheSize, "The cache size isn't a power of 2");

        //Setear datos a 0
        this.nWordsAMemoria = 0;
        this.nWordsDesdeMemoria = 0;
        this.nMissDatos = 0;
        this.nMissInstrucciones = 0;

        //Tipo de cache
        this.isDirect = direct;
        this.isFully = fully;
        if (this.numberOfSets > 0) {
            this.isSet = true;
        }
        else {
            this.isSet = false;
        }

        //Calculo del largo de tag, index, set y tag
        this.indexSize = log2(this.cacheSize);
        this.offsetSize = log2(this.blockSize);
        this.setSize = 0;
        if (this.isSet) {
            isPowerOf2(this.numberOfSets, "The number of lines per set isn't a power of 2 (it's not N-ways, with N a power of 2");
            if (cacheSize % this.numberOfSets != 0) {
                System.out.println("Error: The cache isn't divisible by the number of sets");
                System.exit(1);
            }
            this.setNWays = this.cacheSize / this.numberOfSets;            
            this.setSize = log2(this.numberOfSets);

        }

        this.tagSize = 32 - this.offsetSize;
        if (this.isSet) {
            this.tagSize -= this.setSize;
        }
        else {
            this.tagSize -= this.indexSize;
        }

        //Lineas del cache
        this.lineas = new Linea[this.cacheSize];

        for (int i = 0; i < this.cacheSize; i++) {
            this.lineas[i] = new Linea(this.blockSize, this.tagSize);
        }

        if (this.isSet) {
            this.setLineas = new Linea[this.numberOfSets][this.setNWays];
            int k = 0;
            for (int i = 0; i < this.numberOfSets; i++) {
                for (int j = 0; j < this.setNWays; j++) {
                    this.setLineas[i][j] = this.lineas[k];
                    k++;
                }
            }
        }
        
        /*
        this.isSplit = split;
        //Si el cache size es 1, no puede ser split
        if (this.isSplit && this.cacheSize == 1) {
            System.out.println("Error: The cache can't be split, because the cache size is 1");
            System.exit(1);
        }
        //Si es split sin ser set associative, las lineas son individuales
        if (this.isSplit) {
            this.cacheSize = this.cacheSize / 2;
            this.indexSize -= 1;

        }
        if (this.isSplit && !this.isSet) {
            this.datos = new Linea[this.cacheSize];
            this.instrucciones = new Linea[this.cacheSize];
            for (int i = 0; i < this.cacheSize; i++) {
                this.datos[i] = new Linea(this.blockSize, this.tagSize);
                this.instrucciones[i] = new Linea(this.blockSize, this.tagSize);
            }
        } //Si es split y set associative, se divide el cache en sets
        else if (this.isSplit && this.isSet) {
            this.setDatos = new Linea[this.cacheSize / this.setSize][this.setSize];
            this.setInstrucciones = new Linea[this.cacheSize / this.setSize][this.setSize];
            int k = 0;
            for (int i = 0; i < (this.cacheSize / this.setSize); i++) {
                for (int j = 0; j < this.setSize; j++) {
                    this.setDatos[i][j] = this.lineas[k];
                    this.setInstrucciones[i][j] = this.lineas[k];
                    k++;
                }
            }

        }*/

    }

    public void access(int t, String address, Boolean isWriteBack, Boolean isAllocate) {
        if (this.isDirect) {
            String addressIndex = address.substring(this.tagSize, (this.tagSize + this.indexSize));
            int index = binToInt(addressIndex);
            int hit;
            if (lineas[index].getTag().compareTo(address.substring(0, this.tagSize)) == 0) {
                hit = 1;
            }
            else {
                hit = -1;
            }

            // Al leer se modifica el cache solo si es miss
            if ((t == 0 || t == 2) && hit == -1) {
                read(t, address, this.lineas);
            }
            else if (t == 1) {
                write(t, address, isWriteBack, isAllocate, this.lineas, hit);
            }
        }
        else {
            if (this.isSet) {
                //Obtener el set desde el address
                String addressSet = address.substring(this.tagSize, this.tagSize + this.setSize);
                int set = binToInt(addressSet);  
                this.lineas = this.setLineas[set];
            }
            
            int posicionHit = search(address, this.lineas);

            //En cada operacion se aumenta el contador del LRU (en todo el cache)
            addLru(this.lineas);

            // Al leer se modifica el cache solo si es miss
            if ((t == 0 || t == 2) && posicionHit == -1) {
                read(t, address, this.lineas);
            }
            else if (t == 1) {
                write(t, address, isWriteBack, isAllocate, this.lineas, posicionHit);
            }

                
        }

    }

    private void write(int t, String address, Boolean isWriteBack, Boolean isAllocate, Linea[] lineas, int hit) {
        int siguienteNoValid = 0;
        Linea older = new Linea(this.blockSize, this.tagSize);
        older.setLru(-1);
        //Si hay un miss, primero actua la miss policy correspondiente y luego la hit policy
        if (isAllocate && hit == -1) {
            writeAllocate(t, address, lineas);
        }
        else if (!isAllocate && hit == -1) {
            writeNoAllocate(t, address, lineas);
        }

        //Se realiza la hit policy cuando hay hit o miss
        if (this.isDirect) {
            //Obtener index desde el address
            String addressIndex = address.substring(this.tagSize, (this.tagSize + this.indexSize));
            int index = binToInt(addressIndex);
            if (isWriteBack) {
                writeBack(t, address, lineas[index]);
            }
            else {
                writeThrough(t, address, lineas[index]);
            }
        }
        else //Si se hizo un hit, se escribe en esa posicion
         if (hit != -1) {
                if (isWriteBack) {
                    writeBack(t, address, lineas[hit]);
                }
                else {
                    writeThrough(t, address, lineas[hit]);
                }
            } //Si se hace un miss, se escribe en el mas viejo
            //El más viejo tiene el contador de LRU con valor 0 (debido al miss policy)
            //En caso de que aun hayan lineas no validas, escribir en la siguiente linea no valida
            else {
                //Revisar si el cache tiene algo
                Boolean valid = true;
                while (valid && siguienteNoValid < lineas.length) {
                    valid = lineas[siguienteNoValid].getValid();
                    siguienteNoValid++;
                }
                if (valid) {
                    //Obtener la linea mas vieja (LRU mayor)
                    for (Linea lin : lineas) {
                        if (lin.getLru() > older.getLru()) {
                            older = lin;
                        }
                    }
                }
                else {
                    older = lineas[siguienteNoValid - 1];
                }

                if (isWriteBack) {
                    writeBack(t, address, older);
                }
                else {
                    writeThrough(t, address, older);
                }
            }
    }

    //Solo trabaja cuando hay un miss
    //https://books.google.cl/books?id=gQ-fSqbLfFoC&pg=SL2-PA12&lpg=SL2-PA12&dq=cache+no+allocate+example&source=bl&ots=mXytTO-_uq&sig=81njX5g_XEJKpTED1pFoQ4QE--E&hl=es-419&sa=X&redir_esc=y#v=onepage&q=cache%20no%20allocate%20example&f=false
    private void read(int t, String address, Linea[] lineas) {
        //Al haber un miss en lectura, se trae el dato desde memoria principal a cache (lo que es lo mismo que un write allocate)
        writeAllocate(t, address, lineas);
    }

    private void writeAllocate(int t, String address, Linea[] lineas) {
        int siguienteNoValid = 0;
        Linea older = new Linea(this.blockSize, this.tagSize);
        older.setLru(-1);
        //Marcar el miss correspondiente
        if (t == 0 || t == 1) {
            nMissDatos++;
        }
        else if (t == 2) {
            nMissInstrucciones++;
        }
        //Escribir la linea en memoria principal
        nWordsAMemoria += this.blockSize;
        //Traer la linea correspondiente desde memoria principal a cache
        nWordsDesdeMemoria += this.blockSize;

        //Escribir la linea en el cache (modificar tag)
        if (this.isDirect) {
            //Escribir en el indice dado por el address
            String addressIndex = address.substring(this.tagSize, (this.tagSize + this.indexSize));
            int index = binToInt(addressIndex);
            lineas[index].setTag(address.substring(0, this.tagSize));
            //valid -> 1
            lineas[index].setValid(true);
            //dirty -> 0
            lineas[index].setDirty(false);
        }
        else {
            //Revisar si el cache tiene algo
            Boolean valid = true;
            while (valid && siguienteNoValid < lineas.length) {
                valid = lineas[siguienteNoValid].getValid();
                siguienteNoValid++;
            }
            if (valid) {
                //Obtener la linea mas vieja (LRU mayor)
                for (Linea lin : lineas) {
                    if (lin.getLru() > older.getLru()) {
                        older = lin;
                    }
                }
            }
            else {
                older = lineas[siguienteNoValid - 1];
            }
            //Escribir en la linea mas vieja (LRU mayor)
            older.setTag(address.substring(0, this.tagSize));
            //Volver el contador de LRU a 0
            older.setLru(0);
            //valid -> 1
            older.setValid(true);
            //dirty -> 0
            older.setDirty(false);
        }
    }

    private void writeNoAllocate(int t, String address, Linea[] lineas) {

        int siguienteNoValid = 0;
        Linea older = new Linea(this.blockSize, this.tagSize);
        older.setLru(-1);
        //Marcar el miss correspondiente
        if (t == 0 || t == 1) {
            nMissDatos++;
        }
        else if (t == 2) {
            nMissInstrucciones++;
        }
        //Escribir en memoria principal
        nWordsAMemoria += this.blockSize;

        if (isDirect) {
            String addressIndex = address.substring(this.tagSize, (this.tagSize + this.indexSize));
            int index = binToInt(addressIndex);

            //dirty -> 0
            lineas[index].setDirty(false);
        }
        else {
            //Revisar si el cache tiene algo
            Boolean valid = true;
            while (valid && siguienteNoValid < lineas.length) {
                valid = lineas[siguienteNoValid].getValid();
                siguienteNoValid++;
            }
            if (valid) {
                //Obtener la linea mas vieja (LRU mayor)
                for (Linea lin : lineas) {
                    if (lin.getLru() > older.getLru()) {
                        older = lin;
                    }
                }
            }
            else {
                older = lineas[siguienteNoValid - 1];
            }
            //Volver el contador de LRU a 0
            older.setLru(0);

            //dirty -> 0
            older.setDirty(false);
        }

    }

    private void writeBack(int t, String address, Linea linea) {
        //Si es dirty mandar a memoria
        if (linea.getDirty()) {
            nWordsAMemoria += blockSize;
        }
        //Escribir dato en cache
        String addressTag = address.substring(0, this.tagSize);
        linea.setTag(addressTag);
        //Marcar como dirty
        linea.setDirty(true);
    }

    private void writeThrough(int t, String address, Linea linea) {
        //Escribir dato en cache
        String addressTag = address.substring(0, this.tagSize);
        linea.setTag(addressTag);
        //Escribir en memoria principal (solo un word, no el bloque completo)
        nWordsAMemoria += blockSize;
    }

    private int search(String address, Linea[] linea) {
        String addressTag = address.substring(0, this.tagSize);
        for (int i = 0; i < linea.length; i++) {
            if (addressTag.compareTo(linea[i].getTag()) == 0) {
                return i;
            }
        }
        return -1;
    }

    private void addLru(Linea[] lineas) {
        for (int i = 0; i < lineas.length; i++) {
            if (lineas[i].getValid()) {
                lineas[i].setLru(lineas[i].getLru() + 1);
            }
        }
    }

    private int binToInt(String bin) {
        int numero = 0;
        char[] binChar1 = bin.toCharArray();
        char[] binChar = new char[binChar1.length];
        for (int i = 0; i < binChar1.length; i++) {
            binChar[binChar.length - i - 1] = binChar1[i];
        }

        for (int i = 0; i < binChar.length; i++) {
            int binNum = Integer.parseInt(Character.toString(binChar[i]));
            numero += binNum * Math.pow(2, i);
        }
        return numero;
    }
    
        private int log2(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        int log = 31 - Integer.numberOfLeadingZeros(n);

        if (n % Math.pow(2, log) == 0) {
            return log;
        }
        else {
            return 0;
        }

    }

    private void isPowerOf2(int n, String error) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        int log = 31 - Integer.numberOfLeadingZeros(n);
        if (n % Math.pow(2, log) != 0) {
            System.out.println("Error: " + error);
            System.exit(1);
        }
    }

}
