package Util;

import com.google.common.collect.HashBiMap;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author lubo 
 */
public class GraphMethods {

  private HashMap<String, Double> edgeDB = new HashMap<>();
  private HashMap<Integer, ArrayList<Integer>> relationshipDB = new HashMap<>();
  private HashBiMap<Integer, String> allVertexDB = HashBiMap.create();
  private int ID;
  private StringMethods u;

  public GraphMethods(HashMap<String, Double> edgeDB, HashMap<Integer, ArrayList<Integer>> relationshipDB,
          HashBiMap<Integer, String> allVertexDB, int ID, StringMethods u) {
    this.edgeDB = edgeDB;
    this.relationshipDB = relationshipDB;
    this.allVertexDB = allVertexDB;
    this.ID = ID;
    this.u = u;
  }

  public int getID(){
    return ID;
  }
  public int addVertex(String label, int wikiID, HashBiMap<Integer, Integer> IDWikiID) {
    if (!u.isNoiseWord(label)) {
      if (!allVertexDB.inverse().containsKey(label)) { //new node
        allVertexDB.put(++ID, label);
        if (wikiID != 0) {
          IDWikiID.put(ID, wikiID);
        }
        return ID;
      } else { // old node
        int id = allVertexDB.inverse().get(label);
        if (wikiID != 0 && !IDWikiID.containsKey(id)) {
          IDWikiID.put(id, wikiID);
        }
        return id;
      }
    }
    return -1;
  }

  public void appendRelationship(HashMap<Integer, ArrayList<Integer>> relationship, int key, int value) {
    if (!relationship.containsKey(key)) {
      ArrayList<Integer> a = new ArrayList<>();
      a.add(value);
      relationship.put(key, a);
    } else {
      relationship.get(key).add(value);
    }
  }

  public void addEdge(int v1ID, int v2ID, double weight) {
    if (weight < StringMethods.CUTOFF) {
      return;
    }
    String key = v1ID + "-" + v2ID;
    if (!edgeDB.containsKey(key)) { //new edge
      edgeDB.put(key, weight);
      appendRelationship(relationshipDB, v1ID, v2ID);
    } else {
      double newWeight = edgeDB.get(key) + weight;
      edgeDB.put(key, newWeight);
    }
  }

  public static double computeMinMax(double min, double max, double ratio) {
    if (ratio > 0.5) {
      return max * 1.2;
    }
    return min + (max - min) * -1 / ((Math.log(ratio) / Math.log(2.0)));
    //return min + (max - min) * Math.sqrt(ratio);

  }
}
