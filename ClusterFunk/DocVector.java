package ClusterFunk;

import java.util.ArrayList; /* ArrayList */

/**
 *
 * @author smitbl07
 * 
 * Represents documents as an n-dimension vector,
 *  where n is the number of unique words in the
 *  entire document collection
 */
@Deprecated
public class DocVector {
    
	private ArrayList<Double> vector;
	private double magnitude;
	private boolean magUnknown;
        private boolean notNorm;
        private int docID;
        
        public DocVector(int docID) {
            vector = new ArrayList();
            magnitude = 0;
            magUnknown = true;
            notNorm = true;
            this.docID = docID;
        }
        
        public int getDocID() 
            {return this.docID;}
        
        public void add(int value) 
            {vector.add((double)value);}
        
        public void add(double value) 
            {vector.add(value);}
        
        public void set(int position, int value) 
            {vector.set(position, (double)value);}
        
        public void set(int position, double value) 
            {vector.set(position, value);}
        
        public double get(int position) {
            if (notNorm) {
                normalize();
            }
            return vector.get(position);
        }
        
        public int size() 
            {return this.vector.size();}
        
        public boolean notNorm()
            {return this.notNorm;}
        
	public double getMagnitude() {
            if (magUnknown) {
		calcMagnitude();
		magUnknown =false;
            }
            return this.magnitude;
	}
        
        public void normalize() {
            int vectorSize = vector.size();
            for (int i=0; i < vectorSize; i++) {
                vector.set(i, (vector.get(i) / getMagnitude()));
            }
        }

	/*
	similarity == cos() == (A.B)/||A||||B||
	*/
	public double cosSim(DocVector doc) {
            if (notNorm) {
                normalize();
            }
            if (doc.notNorm) {
                doc.normalize();
            }
            
            double cosSim =0.0;
            int vectorSize = vector.size();

            for (int i =0; i < vectorSize; i++) {
                cosSim += (vector.get(i) * (Double)(doc.get(i)));
            }

            cosSim = cosSim / (magnitude * doc.getMagnitude());
            return cosSim;
	}

	/*
	Calculates magnitude (||A||) of this vector and stores value
		in this.magnitude
	*/
	private void calcMagnitude() {
            double magnitude =0.0;
            int vectorSize = vector.size();

            for (int i =0; i < vectorSize; i++) {
                    magnitude += Math.pow(vector.get(i), 2);
            }

            magnitude = Math.sqrt(magnitude);
            this.magnitude = magnitude;
	}
        
        public String toString() {
            if (notNorm) {
                normalize();
            }
            String vectorStr = "";
            
            for (Double value : vector) {
                vectorStr += (value + " ");
            }
            
            return vectorStr;
        }
}
