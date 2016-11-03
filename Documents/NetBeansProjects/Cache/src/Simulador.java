
import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;

public class Simulador {

    // Retornos
    public static void main(String[] args) {
        int blockSize = 0, cacheSize = 0, numberOfSets = 0;
        int nInstrucciones = 0;
        int nDatos = 0;
        Boolean isWriteBack = true;
        Boolean isDirect = true;
        Boolean isFully = false;
        Boolean isAllocate = true;
        Boolean isSplit = false;
        Scanner trace = null;
        Cache cache;

        if (args.length > 0) {
            int i;
            for (i = 0; i < (args.length - 1); i++) {
                if (args[i].compareTo("-bs") == 0) {
                    try {
                        blockSize = Integer.parseInt(args[i + 1]);
                        if (blockSize % 4 != 0){
                            System.out.println("Error: The block size isn't divisible by 4");
                            System.exit(1);
                        }
                        blockSize /= 4;
                    } catch (Exception e) {
                        System.out.println("Error: The argument after -bs must be a number");
                    }
                    i++;
                }
                else if (args[i].compareTo("-cs") == 0) {
                    try {
                        cacheSize = Integer.parseInt(args[i + 1]);
                    } catch (Exception e) {
                        System.out.println("Error: The argument after -cs must be a number");
                    }
                    i++;
                }
                else if (args[i].compareTo("-wt") == 0) {
                    isWriteBack = false;
                }
                else if (args[i].compareTo("-fa") == 0) {
                    if (numberOfSets > 0) {
                        System.out.println("Error: The cache can't be FullyAssociative and SetAssociative at the same time");
                        System.exit(1);
                    }
                    isFully = true;
                    isDirect = false;
                }
                else if (args[i].compareTo("-sa") == 0) {
                    if (isFully) {
                        System.out.println("Error: The cache can't be FullyAssociative and SetAssociative at the same time");
                        System.exit(1);
                    }
                    numberOfSets = Integer.parseInt(args[i + 1]);
                    i++;
                    isDirect = false;
                }
                else if (args[i].compareTo("-wna") == 0) {
                    isAllocate = false;
                }
                else if (args[i].compareTo("-split") == 0) {
                    isSplit = true;
                }
                else {
                    System.out.println("Error: Invalid argument");
                    System.exit(1);
                }

            }

            if (numberOfSets > cacheSize) {
                System.out.println("Error: The number of sets can't be higher that the cache size");
                System.exit(1);
            }

            //Lee el archivo desde los argumentos
            try {
                trace = new Scanner(new File(args[args.length - 1]));
            } catch (Exception e) {
                System.out.println("Error: Invalid file");
                System.exit(1);
            }

        }
        else {
            System.out.println("Error: You need to provide options and a file");
            System.out.println("Usage: java Simulador <options> file.trace");
            System.exit(1);
        }

        //Crear cache
        cache = new Cache(blockSize, cacheSize, isDirect, isFully, numberOfSets, isSplit);

        //Leer entradas
        String entrada = null;
        int t = -1;
        String hexaddress = null;

        do {
            entrada = null;
            entrada = trace.nextLine();
            String[] cmd = entrada.split(" ");

            try {
                t = Integer.parseInt(cmd[0]);
                if ((t < 0) || (t > 2)) {
                    System.out.println("Error: Wrong type of access");
                    System.exit(1);
                }
            } catch (NumberFormatException ex) {
                System.out.println(ex);
                System.exit(1);
            }

            //Convertir el address de hexadecimal a binario para utilizarlo en el cache
            hexaddress = cmd[1];
            if (hexaddress.length() > 8) {
                hexaddress = hexaddress.substring(0, 8);
            }

            String address = new BigInteger(hexaddress, 16).toString(2);
            //Rellenar con ceros a la izquierda hasta dejar los 32 bits del address
            address = String.format("%32s", address).replace(" ", "0");

            //Hacer funcionar el cache aqui...
            cache.access(t, address, isWriteBack, isAllocate);

            if ((t == 0) || (t == 1)) {
                nDatos++;
            }
            else if (t == 2) {
                nInstrucciones++;
            }
            else {
                System.exit(1);
            }
        } while (trace.hasNext());

        System.out.println("Number of instructions references: " + nInstrucciones);
        System.out.println("Number of data references: " + nDatos);
        System.out.println("Number of instruction misses: " + cache.nMissInstrucciones);
        System.out.println("Number of data misses: " + cache.nMissDatos);
        System.out.println("Number of copied words from main memory: " + cache.nWordsDesdeMemoria);
        System.out.println("Number of copied words to main memory: " + cache.nWordsAMemoria);
    }
    
    

}
