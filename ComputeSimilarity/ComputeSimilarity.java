/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ComputeSimilarity;

import java.util.*; 
import com.google.common.collect.HashBiMap;
import java.io.FileInputStream;
import java.io.ObjectInputStream; 



public class ComputeSimilarity {

  private static HashMap<Integer, ArrayList<Integer>> relationshipDB = new HashMap<>();
  private static HashMap<String, Double> edgeDB = new HashMap<>();
  private static HashBiMap<Integer, String> allVertexDB = HashBiMap.create();
  static SimilarityMethods sm;

  
  public void ComputeSimilarity() throws Exception {
    //System.out.println(new Date());
    //readFile("/home/cs460/graphWikiComplete.data");
    readFile("graph.data");
    sm = new SimilarityMethods(relationshipDB, edgeDB);
  }
  
  public void readFile(String fileName) throws Exception {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
      int id = in.readInt();
      edgeDB = (HashMap<String, Double>) in.readObject();
      allVertexDB = (HashBiMap<Integer, String>) in.readObject();
      relationshipDB = (HashMap<Integer, ArrayList<Integer>>) in.readObject();
    }
  }

  public void findNeighbours(String word,ArrayList<String> ray,ArrayList<Double> edge) {
    
   
      String label1 = word;
      for(int i=0;i<ray.size();i++){
          
      String label2 =ray.get(i);
      double distance;
      
      if  (!(allVertexDB.inverse().containsKey(label1) && allVertexDB.inverse().containsKey(label2))){
        distance=0;
      } else {
        int v1ID = allVertexDB.inverse().get(label1);
        int v2ID = allVertexDB.inverse().get(label2);
        distance = sm.computeSimilarity(v1ID, v2ID) * sm.computeSimilarity(v2ID, v1ID);
      }

      double result = 1 / Math.log(Math.min(0.1, distance)) * Math.log(0.1);
     
      if(result<.3){
         // System.out.println("Hey FATBOY LOOK HERE "+ray.size());
        ray.remove(i);
        edge.remove(i);
      }    
      }
      
    }
  
}
