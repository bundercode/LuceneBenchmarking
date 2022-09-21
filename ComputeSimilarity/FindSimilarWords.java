package ComputeSimilarity;

import static ComputeSimilarity.ComputeSimilarity.sm;
import java.util.*;
import Util.*;
import com.google.common.collect.HashBiMap;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import sun.security.provider.certpath.Vertex;

public class FindSimilarWords {

    static int ID = 0;
    protected static HashMap<Integer, ArrayList<Integer>> relationshipDB = new HashMap<>();
    protected static HashMap<String, Double> edgeDB = new HashMap<>();
    protected static HashBiMap<Integer, String> allVertexDB = HashBiMap.create();
    static HashSet<Integer> wordFormVertexDB = new HashSet<>();
    static SimilarityMethods sm;
    
    public FindSimilarWords() {
    //System.out.println(new Date());
        //readFile("/home/cs460/graphWikiComplete.data");
        readFile("graph.data");
        sm = new SimilarityMethods(relationshipDB, edgeDB);
        //getSimilarWords();
    }

    public void readFile(String fileName) {
        System.out.println("IN READFILE");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            ID = in.readInt();
            edgeDB = (HashMap<String, Double>) in.readObject();
            allVertexDB = (HashBiMap<Integer, String>) in.readObject();
            wordFormVertexDB = (HashSet<Integer>) in.readObject();
            relationshipDB = (HashMap<Integer, ArrayList<Integer>>) in.readObject();
            in.close();
        } catch (Exception ex) {
        }

        System.out.println("LEAVING READFILE");
    }

    public void getSimilarWords(String text) {
        //System.out.println("IN GETSIMILARWORDS");
        String label = text;
        if (!allVertexDB.inverse().containsKey(label)) {
            System.out.println("Can't find vertx: " + label);
            //return null;
        }
        // System.out.println("Label: "+label+" Inverse AVDB: "+allVertexDB.inverse().get(label)+"!!!!!");
        findWords(allVertexDB.inverse().get(label));
    }

    public void findWords(int vertexID) {
      //System.out.println("IN FINDWORDS");

        /**
         * given a vertexID it is easy to find the sense or words a word is
         * connected to. A sense in essence is just a definition, but a sense is
         * connected to other nodes of words and sense. This for loop gets the
         * first collection of words and sense.
         */
        for (int destinationVertexID : relationshipDB.get(vertexID)) {
            double edgeWeight = edgeDB.get(vertexID + "-" + destinationVertexID);

        }

        /**
         * This for takes sense from the for loop above and finds the words
         * connected to that sense
         */
   //System.out.println("Edges: "+edgesVal.size());
        // System.out.println("Ray: "+ray.size());
        //return edgesVal;
    }
}