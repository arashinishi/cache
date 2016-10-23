package cache;

import java.io.File;
import java.util.Scanner;

public class Simulador {

    public static void main(String[] args) {
        int blockSize=0, cacheSize=0, set=0;
        Boolean isWriteBack = true;
        Boolean isDirect = true;
        Boolean isFully = false;
        Boolean isAllocate = true;
        Boolean isSplit = false;     
        
        if(args.length>0){
            int i;
            for (i = 0 ; i <(args.length-1); i++){                
                if (args[i].compareTo("-bs") == 0){
                    blockSize = Integer.parseInt(args[i+1]);                    
                    i++;
                }
                
                else if (args[i].compareTo("-cs") == 0){
                    cacheSize = Integer.parseInt(args[i+1]);
                    i++;
                }
                
                else if (args[i].compareTo("-wt") == 0){
                    isWriteBack = false;
                }
                
                else if (args[i].compareTo("-fa") == 0){
                    if (set>0) throw new IllegalArgumentException();
                    isFully = true;
                    isDirect = false;
                }
                
                else if (args[i].compareTo("-sa") == 0){
                    if (isFully) throw new IllegalArgumentException();
                    set = Integer.parseInt(args[i+1]);
                    i++;
                    isDirect = false;
                }
                
                else if (args[i].compareTo("-wna") == 0){
                    isAllocate = false;
                }
                
                else if (args[i].compareTo("-split") == 0){
                    isSplit = true;
                }
                
                else
                    throw new IllegalArgumentException();

                         
            }
            System.out.println(args[args.length-1]);
            File trace = new File(args[args.length-1]);
        }
        else
            System.out.println("You need to provide arguments");
        
        //Crear cache
        new Cache(blockSize, cacheSize, isWriteBack, isDirect, isFully, set, isAllocate, isSplit);
        
        //Leer entradas
        
        Scanner scanner = new Scanner(System.in);
        String entrada = null;
        int t = -1;
        String address = null;
        
        
        do
        {
            entrada = null;
            entrada = scanner.nextLine();
            String [] cmd = entrada.split(" ");
            
            try
            {
                t = Integer.parseInt(cmd[0]);
                if((t <0) || (t > 2))
                {
                    System.out.println("Wrong type of access");
                    System.exit(1);
                }
            }
            catch(NumberFormatException ex)
            {
                System.out.println(ex);
                System.exit(1);
            }
            
            address = cmd[1];
            
            System.out.println("T : " + t + " Address : " + address);
        }
        while(scanner.hasNext());
        
        
    }
    

}
