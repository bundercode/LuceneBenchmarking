package Search;

import DocCollection.*;
/**
 * @file Benchmark.java
 * @author smitbl07
 * @project LuceneBenchmarking
 * 
 * @section DESCRIPTION
 * Storage class used to store average precisions as they are calculated by the
 * Engine.  Contains a toString method to print out the final results of the
 * average precisions per query.
 */
public class Benchmark {
    
    private final double[][] avep;
    
    /**
     * Default constructor for Benchmark.  Allocates space for number of queries
     * to be processed.
     */
    public Benchmark(){
        avep = new double[4][225];
    }
    
    /**
     * Setter for average precision at a location in the array.
     * 
     * @param query corresponds to query number and second portion of array
     * @param level corresponds to which test is being run 1-1 through 1-4
     * @param avep average precision to add to the array
     */
    public void addAveP(int query, int level, double avep){
        this.avep[level][query] = avep;
    }
    
    /**
     * Returns a stylized version of the stored average precisions.
     * 
     * @return average precisions as string
     */
    public String toString(final int avepLength){
        String message ="\n\n***********************************************\n"
                +"AVERAGE PRECISIONS:\n";
        
        for (int i=0; i < avepLength; i++){
            message += String.format("Query:%03d   "
                    +"%1.20f\t"+"%1.20f\t"+"%1.20f\t"+"%1.20f\n", 
                    i+1, this.avep[0][i], this.avep[1][i], this.avep[2][i], this.avep[3][i]);
        }
        return message;
    }
}
