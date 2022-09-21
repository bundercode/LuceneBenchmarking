/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ComputeSimilarity;

import Util.*;
import java.util.*;

/**
 *
 * @author lubo
 */
public class SimilarityMethods {

  private HashMap<Integer, ArrayList<Integer>> relationshipDB;
  private HashMap<String, Double> edgeDB;

  public SimilarityMethods(HashMap<Integer, ArrayList<Integer>> relationshipDB, HashMap<String, Double> edgeDB) {
    this.relationshipDB = relationshipDB;
    this.edgeDB = edgeDB;
  }
  private HashSet<Integer> visited;
  private double totalDistance;
  private double v2ID;
  private HashMap<Integer,Double> forwardVisited;
  private HashMap<Integer,Double> backwardVisited;

  public void depthFirst(int v1ID, int depth, double distance) {
    if (depth == StringMethods.DEPTH || distance < StringMethods.CUTOFF || visited.contains(v1ID)) {
      return;
    }
    if (v1ID == v2ID) {
      totalDistance *= (1-distance);
      return;
    }
    visited.add(v1ID);
    if (relationshipDB.get(v1ID) == null) {
      return;
    }
    for (int destinationNodeID : relationshipDB.get(v1ID)) {
      double edgeWeight = edgeDB.get(v1ID + "-" + destinationNodeID);
      depthFirst(destinationNodeID, depth + 1, distance * edgeWeight);
    }
  }

  public double computeSimilarity(int v1ID, int v2ID) {

    visited = new HashSet<>();
    this.v2ID = v2ID;
    totalDistance = 1;
    depthFirst(v1ID, 0, 1);
    return 1-totalDistance;
  }
  /*
   public double computeSimilarity2(int v1ID, int v2ID) {
   double dist = 0;
   LinkedList<Node> q = new LinkedList<>();
   HashSet<Node> visitedNodes = new HashSet<>();
   Node n = new Node(v1ID, 0, 1);
   if (n == null) {
   return dist;
   }
   q.add(n);
   while (!q.isEmpty()) {
   Node newNode = q.poll();
   if (newNode.depth >= StringMethods.DEPTH) {
   continue;
   }
   if (newNode.distance < StringMethods.CUTOFF) {
   continue;
   }
   if (newNode.vertexID == v2ID) {
   dist += newNode.distance;
   continue;
   }
   if (!visitedNodes.contains(newNode)) {
   visitedNodes.add(newNode);
   } else {
   continue;
   }
   if (relationshipDB.get(newNode.vertexID) == null) {
   continue;
   }
   for (int destinationNodeID : relationshipDB.get(newNode.vertexID)) {
   double edgeWeight = edgeDB.get(newNode.vertexID + "-" + destinationNodeID);
   q.add(new Node(destinationNodeID, newNode.depth + 1, newNode.distance * edgeWeight));
   }
   }
   return dist;
   }
   */

  class MyRunnable implements Runnable {

    private int vertexID;
    private Direction direction;

    public MyRunnable(int vertexID, Direction direction) {
      this.vertexID = vertexID;
      this.direction = direction;
    }

    @Override
    public void run() {
      depthFirstSearch(vertexID, 0, 1.0, direction);
    }

    public void depthFirstSearch(int v1ID, int depth, double distance, Direction dir) {
      if (depth == StringMethods.DEPTH || distance < StringMethods.CUTOFF) {
        return;
      }
      if (dir == Direction.RIGHT && forwardVisited.containsKey(v1ID)) {
        return;
      }
      if (dir == Direction.LEFT && backwardVisited.containsKey(v1ID)) {
        return;
      }
      if (dir == Direction.RIGHT && backwardVisited.containsKey(v1ID)) {
        totalDistance += (distance*backwardVisited.get(v1ID));
        return;
      }
      if (dir == Direction.LEFT && forwardVisited.containsKey(v1ID)) {
        totalDistance += (distance*forwardVisited.get(v1ID));
        return;
      }

      if (dir == Direction.RIGHT) {
        forwardVisited.put(v1ID,distance);
      } else {
        backwardVisited.put(v1ID,distance);
      }
      if (relationshipDB.get(v1ID) == null) {
        return;
      }
      for (int destinationNodeID : relationshipDB.get(v1ID)) {
        double edgeWeight;
        if (dir == Direction.RIGHT) {
          edgeWeight = edgeDB.get(v1ID + "-" + destinationNodeID);
        } else {
          if (edgeDB.containsKey(destinationNodeID + "-" + v1ID)) {
            edgeWeight = edgeDB.get(destinationNodeID + "-" + v1ID);
          } else {
            continue;
          }
        }
        depthFirstSearch(destinationNodeID, depth + 1, distance * edgeWeight, dir);
      }
    }
  }

  public double computeSimilarity2(int v1ID, int v2ID) {
    totalDistance = 0;
    forwardVisited = new HashMap<>();
    backwardVisited = new HashMap<>();
    Runnable r1 = new MyRunnable(v1ID, Direction.RIGHT);
    Runnable r2 = new MyRunnable(v2ID, Direction.LEFT);
    Thread t1 = new Thread(r1);
    Thread t2 = new Thread(r2);
    t1.start();
    t2.start();
    try {
      t1.join();
      t2.join();
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return totalDistance;
  }
}
