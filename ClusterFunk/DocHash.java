package ClusterFunk;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author smitbl07
 * 
 * Represents documents as an n-dimension vector,
 *  where n is the number of unique words in the
 *  entire document collection
 * 
 * (Replaces DocVector)
 */
public class DocHash {
    
    private final int docID;
    private HashMap<String, Double> vector;
    private double magnitude;
    private boolean magUnknown;

    public DocHash(int docID) {
        this.docID =docID;
        this.vector =new HashMap();
        this.magnitude =0.0;
        this.magUnknown =true;
    }

    public int getDocID() 
        {return this.docID;}

    public void add(String key, int value) 
        {this.vector.put(key, (double)value);}

    public void add(String key, double value) 
        {this.vector.put(key, value);}
    
    public void append(String key, int value) {
        if (this.vector.containsKey(key)) {
            double temp = this.vector.get(key);
            temp +=(double)value;
            this.vector.put(key, temp);
        }
        else this.vector.put(key, (double)value);
    }
    
    public void append(String key, double value) {
        if (this.vector.containsKey(key)) {
            double temp = this.vector.get(key);
            temp +=value;
            this.vector.put(key, temp);
        }
        else this.vector.put(key, value);
    }

    public double get(String key) 
        {return (double)(this.vector.get(key));}
    
    public HashMap<String, Double> getHash()
        {return this.vector;}
    
    /*
    number of total words in document
    */
    public int numTotal() {
        int numTotal =0;
        Collection<Double> wordCounts = (Collection)vector.values();
        Iterator<Double> countIter = (Iterator)wordCounts.iterator();
        
        while (countIter.hasNext()) {
            numTotal += (int)(countIter.next().doubleValue());
        }
        
        return numTotal;
    }

    /*
    number of unique words in document
    */
    public int numUnique() 
        {return this.vector.size();}
    
    public Set<String> wordIDSet()
        {return this.vector.keySet();}

    public double getMagnitude() {
        if (this.magUnknown) {
            calcMagnitude();
            this.magUnknown =false;
        }
        return this.magnitude;
    }

    /*
    Helper method for cosine()
    */
    public double cosSim(DocHash doc) {
        if (this.numUnique() < doc.numUnique()){
            return cosine(this, doc);
        }
        else return cosine(doc, this);
    }
    
    /*
    similarity == cos() == (A.B)/(||A||)(||B||)
    Note: for efficiency, document with smaller unique word count
        should be DocHash a.
    */
    public static double cosine(DocHash a, DocHash b) {
        double cosine =0.0;
        Set<String> wordSet = (Set)a.vector.keySet();
        Iterator<String> wordIter = (Iterator)wordSet.iterator();
        String index;

        /*for (int i =0; i < vectorSize; i++) {
            cosine += (a.get(i) * b.get(i));
        }*/
        while (wordIter.hasNext()) {
            index =wordIter.next();
            if (b.vector.containsKey(index)) {
                cosine +=(a.vector.get(index) *b.vector.get(index));
            }
        }

        cosine = cosine /(a.getMagnitude() *b.getMagnitude());
        return cosine;
    }
    
    /*
    Calculates magnitude (||A||) of this vector
        and stores value in this.magnitude
    */
    private void calcMagnitude() {
        double magnitude =0.0;
        Collection<Double> wordCounts = (Collection)vector.values();
        Iterator<Double> countIter = (Iterator)wordCounts.iterator();

        /*for (int i =0; i < vectorSize; i++) {
                magnitude += Math.pow(vector.get(i), 2);
        }*/
        while (countIter.hasNext()) {
                magnitude += Math.pow(countIter.next(), 2.0);
        }

        magnitude = Math.sqrt(magnitude);
        this.magnitude =magnitude;
    }
    
    public void normalize() {
        Set<String> wordSet = (Set)this.vector.keySet();
        Iterator<String> wordIter = (Iterator)wordSet.iterator();
        String index;
        double mag = this.getMagnitude();

        /*for (int i=0; i < vectorSize; i++) {
            vector.set(i, (vector.get(i) / getMagnitude()));
        }*/
        while (wordIter.hasNext()) {
            index =wordIter.next();
            this.vector.put(index, (this.vector.get(index)/mag));
        }
    }
}
