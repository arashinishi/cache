package cache;

public class Simulador {

    public static void main(String[] args) {
        Boolean isWriteBack = true;
        Boolean isDirect = true;
        Boolean isFully = false;
        Boolean isAllocate = true;
        Boolean isSplit = false;
        int set=0;
        int blockSize, cacheSize;
        
        for (int i = 0 ; i <(args.length); i++){
            switch (args[i]){
                case "bs":
                    blockSize = Integer.parseInt(args[i+1]);
                    i++;
                case "cs":
                    cacheSize = Integer.parseInt(args[i+1]);
                    i++;
                case "wt":
                    isWriteBack = false;
                case "fa":
                    if (set>0) throw new IllegalArgumentException();
                    isFully = true;
                    isDirect = false;
                case "sa":
                    if (isFully) throw new IllegalArgumentException();
                    set = Integer.parseInt(args[i+1]);
                    i++;
                    isDirect = false;
                case "wna":
                    isAllocate = false;
                case "split":
                    isSplit = true;
                default:
                    throw new IllegalArgumentException();
                        
            }          
        }
        //Crear cache
        
        
    }
    

}
