/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ComputeSimilarity;

import Util.*;
import com.google.common.collect.HashBiMap;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author lubo 
 */
public class CreateExperimentalResults {

  private static int ID;
  private static HashMap<Integer, ArrayList<Integer>> relationshipDB = new HashMap<>();
  private static HashMap<String, Double> edgeDB = new HashMap<>();
  private static HashBiMap<Integer, String> allVertexDB = HashBiMap.create();
  static SimilarityMethods sm;

  public static void main(String[] args) throws Exception {
    
    System.out.println(new Date());
    //readFile("/home/cs460/graphWikiComplete.data");
    readFile("graphWikiComplete.data");
    sm = new SimilarityMethods(relationshipDB, edgeDB);
    computeSimilarity();
  }

  public static void readFile(String fileName) throws Exception {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
      ID = in.readInt();
      edgeDB = (HashMap<String, Double>) in.readObject();
      allVertexDB = (HashBiMap<Integer, String>) in.readObject();
      relationshipDB = (HashMap<Integer, ArrayList<Integer>>) in.readObject();
    }
  }

  public static void populate(String[] a, String[] b, int[] v1List, int[] v2List) {
    for (int i = 0; i < a.length; i++) {
      if (allVertexDB.inverse().containsKey(a[i]) && allVertexDB.inverse().containsKey(b[i])) {
        v1List[i] = allVertexDB.inverse().get(a[i]);
        v2List[i] = allVertexDB.inverse().get(b[i]);
      } else {
        v1List[i] = -1;
        v2List[i] = -1;
      }
    }
  }

  public static void printResults(String fileName1, String fileName2, String[] words1, String[] words2,
          int[] v1List, int[] v2List, double[] realResults) {

    double[] distance = new double[words1.length];
    double[] result1 = new double[words1.length];
    double[] result2 = new double[words1.length];
    for (int i = 0; i < words1.length; i++) {
      if (v1List[i] != -1) {
        distance[i] = (sm.computeSimilarity(v1List[i], v2List[i]) + sm.computeSimilarity(v2List[i], v1List[i])) / 2;
      } else {
        distance[i] = 0;
      }
      System.out.println(words1[i] + " + " + words2[i] + " = " + distance[i]);
    }

    try (PrintWriter out1 = new PrintWriter(fileName1); PrintWriter out2 = new PrintWriter(fileName2)) {
      for (double a10 = 0.1; a10 <= 1; a10 += 0.1) {
        out1.println("--------------" + a10 + "---------------");
        out2.println("--------------" + a10 + "---------------");
        System.out.println("--------" + a10 + "---------------");
        for (int i = 0; i < distance.length; i++) {
   
          if (distance[i] == 0) {
            result1[i] = 0;
            result2 [i]= 0;
          } else {
            result1[i] = 1 / Math.log(Math.min(a10, distance[i])) * Math.log(a10);
            result2[i] = Math.min(distance[i], a10) * (1 / a10);
          }
          out1.println(result1);
          out2.println(result2);
        }
        System.out.println("alpha = "+a10+" log correlation = "+correlation(realResults,result1));
        System.out.println("alpha = "+a10+" linear correlation = "+correlation(realResults,result2));
      }
    } catch (Exception e) {
      System.out.println(e);
    }


  }

  public static void computeSimilarity() {

    String[] words1 = {"car", "gem", "journey", "boy", "coast", "asylum", "magician", "midday", "furnace",
      "food", "bird", "bird", "tool", "brother", "crane", "lad", "journey", "monk", "food", "coast",
      "forest", "monk", "coast", "lad", "chord", "glass", "noon", "rooster"};
    String[] words2 = {"automobile", "jewel", "voyage", "lad", "shore", "madhouse", "wizard", "noon",
      "stove", "fruit", "cock", "crane", "implement", "monk", "implement", "brother", "car", "oracle",
      "rooster", "hill", "graveyard", "slave", "forest", "wizard", "smile", "magician", "string", "voyage"};


    int[] v1List1 = new int[words1.length];
    int[] v2List1 = new int[words2.length];

    populate(words1, words2, v1List1, v2List1);

    String[] a = new String[353];
    String[] b = new String[353];
    int[] v1List2 = new int[353];
    int[] v2List2 = new int[363];
    double[] r = new double[353];
    //try (Scanner keyboard = new Scanner(new FileReader("/home/lubo/wordnet/results_final.csv"))) {
    try (Scanner keyboard = new Scanner(new FileReader("wordnet/results_final.csv"))) {
      keyboard.nextLine();
      int i = 0;
      while (keyboard.hasNext()) {
        String s = keyboard.nextLine();
        StringTokenizer st = new StringTokenizer(s, ",");
        a[i] = st.nextToken();
        b[i] = st.nextToken();
        i++;
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    populate(a, b, v1List2, v2List2);

    allVertexDB = null;
            double[] realResults  = new double[] {3.92,3.84,3.84,3.76,3.7,3.61,3.5,3.42,3.11,3.08,3.05,2.97,
2.95,2.82,1.68,1.66,1.16,1.1,0.89,0.87,0.84,0.55,0.42,0.42,0.13,0.11,0.08,0.08};
    //printResults("/home/lubo/experiments/Test1LogWiki", "/home/lubo/experiments/Test1LinearWiki", words1, words2,
    printResults("experiments/Test1LogWiki", "experiments/Test1LinearWiki", words1, words2,
            v1List1, v2List1,realResults);
  //  printResults("/home/lubo/experiments/Test2LogWiki", "/home/lubo/experiments/Test2LinearWiki", a, b,
        //    v1List2, v2List2);
  }
  
  public static double correlation(double[] array1, double[] array2){
    double xbar;
    double ybar;
    double sum = 0;
    for(double el: array1){
      sum+=el;
    }
    xbar = sum / array1.length;
    sum = 0;
    for(double el: array2){
      sum+=el;
    }
    ybar = sum / array2.length;
    double topPart=0;
    double sum1 = 0;
    double sum2 = 0;
    for(int i=0; i < array1.length; i++){
      double a = (array1[i]-xbar);
      double b = (array2[i]-ybar);
      topPart += a*b;
      sum1 += a*a;
      sum2 += b*b;
    }
    return topPart /  Math.sqrt(sum1*sum2);
  }
 
}
