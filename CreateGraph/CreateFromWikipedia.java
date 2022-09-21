package CreateGraph;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import Util.*;
import com.google.common.collect.HashBiMap;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

class WikiTables {

  public static final String CATEGORY = "Category";//"category_example";//
  public static final String PAGE = "Page"; //"page_example";//
  public static final String CATEGORY_PAGES = "category_pages"; //"category_pages_example";//
  public static final String PAGE_CATEGORIES = "page_categories"; //"page_categories_example";//
  public static final String CATEGORY_OUTLINKS = "category_outlinks";//"category_outlinks_example";//
  public static final String PAGE_OUTLINKS = "page_outlinks"; //"page_outlinks_example";//
  public static final String PAGE_INLINKS = "page_inlinks";//"page_inlinks_example";//
  public static final String PAGE_REDIRECTS = "page_redirects";//"page_redirects_example";//
}

class TableAttributes {

  public static final String PAGE_ID = "pageId";
  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String TEXT = "text";
  public static final String OUTLINKS = "outLinks";
  public static final String INLINKS = "inLinks";
  public static final String ISDISAMBIGUATION = "isDisambiguation";
  public static final String REDIRECTS = "redirects";
  public static final String PAGES = "pages";
}

enum RelTypes {

  TITLE, SUBTITLE, TEXT;
}

public class CreateFromWikipedia {

  private static StringMethods u = new StringMethods();
  private static HashBiMap<Integer, String> allVertexDB = HashBiMap.create();
  private static HashSet<Integer> wordFormVertexDB = new HashSet<>();
  private static HashSet<Integer> redirectVertexDB = new HashSet<>();
  private static HashMap<Integer, WordFormVertex> wfInfo = new HashMap<>();
  private static HashBiMap<Integer, String> senseVertexDB = HashBiMap.create();
  private static HashMap<Integer, ArrayList<Integer>> relationshipDB = new HashMap<>();
  private static HashMap<Integer, ArrayList<Integer>> definitionRelationshipDB = new HashMap<>();
  private static HashMap<Integer, ArrayList<Integer>> exampleUseRelationshipDB = new HashMap<>();
  private static HashMap<String, Double> edgeDB = new HashMap<>();
  private static HashMap<Integer, WikiVertex> wikiInfo = new HashMap<>(); //vertexID and size
  private static HashBiMap<Integer, Integer> IDWikiPageID = HashBiMap.create();
  private static HashBiMap<Integer, Integer> IDWikiCategoryID = HashBiMap.create();
  private static HashMap<Integer, Integer> categoryVertexDB = new HashMap<>(); //vertexID and size
  private static HashMap<Integer, ArrayList<Integer>> wfToWikiTitle = new HashMap<>();
  private static HashMap<Integer, ArrayList<Integer>> wfToWikiSubTitle = new HashMap<>();
  private static HashMap<Integer, ArrayList<Integer>> wfToWikiText = new HashMap<>();
  private static HashMap<Integer, ArrayList<Integer>> seeAlsoReverse = new HashMap<>();//reverse edges
  private static HashMap<String, Integer> edgeInfoDB = new HashMap<>();// reverse edge with information
  private static GraphMethods gm;

  public static void main(String[] args) throws Exception {
    //int id = readFile("/home/cs460/graph.data");
    int id = readFile("graph.data");
    //    int id = readAll("/home/lubo/graphWiki.data");
    gm = new GraphMethods(edgeDB, relationshipDB, allVertexDB, id, u);
    addPageTitleNodes();
    addCategoryNodes();
    addRedirections();
    addWikitoWordFormEdges();
    addWFtoWikiEdge();
    addSeeAlsoEdges();
    addWikiHyperlinkEdges();
    addCategorySubCategoryEdges();
    addPageCategoryLinks();
    //store("/home/cs460/graphWikiComplete.data");
    store("graphWikiComplete.data");
    //  storeAll("/home/lubo/graphWiki1.data");
  }

  public static int readFile(String fileName) throws Exception {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
      int id = in.readInt();
      edgeDB = (HashMap<String, Double>) in.readObject();
      allVertexDB = (HashBiMap<Integer, String>) in.readObject();
      wordFormVertexDB = (HashSet<Integer>) in.readObject();
      relationshipDB = (HashMap<Integer, ArrayList<Integer>>) in.readObject();
      for (int wfID : wordFormVertexDB) {
        wfInfo.put(wfID, new WordFormVertex());
      }
      return id;
    }
  }

  public static void storeAll(String fileName) throws Exception {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
      out.writeInt(gm.getID());
      out.writeObject(allVertexDB);

      out.writeObject(wordFormVertexDB);
      out.writeObject(redirectVertexDB);
      out.writeObject(wfInfo);
      out.writeObject(senseVertexDB);
      out.writeObject(relationshipDB);
      out.writeObject(definitionRelationshipDB);


      out.writeObject(exampleUseRelationshipDB);
      out.writeObject(edgeDB);
      out.writeObject(wikiInfo);
      out.writeObject(IDWikiPageID);
      out.writeObject(IDWikiCategoryID);

      out.writeObject(categoryVertexDB);
      out.writeObject(wfToWikiTitle);

      out.writeObject(wfToWikiSubTitle);
      out.writeObject(wfToWikiText);
      out.writeObject(seeAlsoReverse);
      out.writeObject(edgeInfoDB);

    }

  }

  public static void store(String fileName) throws Exception {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
      out.writeInt(gm.getID());
      out.writeObject(edgeDB);
      out.writeObject(allVertexDB);
      out.writeObject(relationshipDB);
    }
  }

  public static int readAll(String fileName) throws Exception {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
      int ID = in.readInt();
      allVertexDB = (HashBiMap<Integer, String>) in.readObject();
      wordFormVertexDB = (HashSet<Integer>) in.readObject();
      redirectVertexDB = (HashSet<Integer>) in.readObject();
      wfInfo = (HashMap<Integer, WordFormVertex>) in.readObject();
      senseVertexDB = (HashBiMap<Integer, String>) in.readObject();
      relationshipDB = (HashMap<Integer, ArrayList<Integer>>) in.readObject();
      definitionRelationshipDB = (HashMap<Integer, ArrayList<Integer>>) in.readObject();
      exampleUseRelationshipDB = (HashMap<Integer, ArrayList<Integer>>) in.readObject();
      edgeDB = (HashMap<String, Double>) in.readObject();
      wikiInfo = (HashMap<Integer, WikiVertex>) in.readObject(); //vertexID and size
      IDWikiPageID = (HashBiMap<Integer, Integer>) in.readObject();
      IDWikiCategoryID = (HashBiMap<Integer, Integer>) in.readObject();
      categoryVertexDB = (HashMap<Integer, Integer>) in.readObject(); //vertexID and size
      wfToWikiTitle = (HashMap<Integer, ArrayList<Integer>>) in.readObject();
      wfToWikiSubTitle = (HashMap<Integer, ArrayList<Integer>>) in.readObject();
      wfToWikiText = (HashMap<Integer, ArrayList<Integer>>) in.readObject();
      seeAlsoReverse = (HashMap<Integer, ArrayList<Integer>>) in.readObject();//reverse edges
      edgeInfoDB = (HashMap<String, Integer>) in.readObject();// reverse edge with information
      return ID;
    }
  }

  public static void startMethod(String name) {
    System.out.println("\n---------------------------------------------------------------------------");
    System.out.println("Starting method: " + name + " Time = " + new Date());
    System.out.println("---------------------------------------------------------------------------");
  }
  static int timer = 0;

  public static void printTimer(String name) {
    timer++;
    if (timer % 100000 == 0) {
      System.out.println("Method: " + name + " Time = " + new Date());
    }
  }

  private static void addPageTitleNodes() throws Exception {
    Wikipedia wiki = new Wikipedia();
    startMethod("AddPageTitleNodes");
    try (ResultSet r = wiki.getResult("Select " + TableAttributes.PAGE_ID + ", " + TableAttributes.NAME + " from " + WikiTables.PAGE)) {
      while (r.next()) {
        String label = StringMethods.replaceSpace(r.getString(TableAttributes.NAME)).toLowerCase();
        int wikiID = r.getInt(TableAttributes.PAGE_ID);
        int ID = gm.addVertex(label, wikiID, IDWikiPageID);
        if (ID == -1) {
          continue;
        }
        int hyperlinkOutlinks;
        try (ResultSet r2 = wiki.getResult("Select count(*) as count "
                        + "from (select distinct " + TableAttributes.ID + ", " + TableAttributes.OUTLINKS
                        + " from " + WikiTables.PAGE_OUTLINKS
                        + " where " + TableAttributes.ID + "= " + wikiID + ") as distinct_page_outlinks")) {
          hyperlinkOutlinks = 0;
          if (r2.next()) {
            hyperlinkOutlinks = r2.getInt("count");
          }
        }
        if (hyperlinkOutlinks == 0) {
          hyperlinkOutlinks = 1;
        }

        WikiVertex pageVertex = new WikiVertex(0, hyperlinkOutlinks);
        wikiInfo.put(ID, pageVertex);
        printTimer("AddPageTitleNodes");
      }
    }
    wiki.closeConnection();
  }

  private static int computeNumberOfPages(Wikipedia wiki, int id) throws Exception {
    ResultSet r = wiki.getResult("select count(*) as sum from " + WikiTables.CATEGORY_PAGES + " where " + TableAttributes.ID + " = " + id);
    int sum;
    if (r.next())//es
    {
      sum = r.getInt("sum");
      r.close();
      return sum;
    } else {
      r.close();
      return 0;
    }

  }

  private static void addCategoryNodes() throws Exception {//addCategoryNodes()
    int ID;
    Wikipedia wiki = new Wikipedia();
    startMethod("addCategoryNodes");
    try (ResultSet r = wiki.getResult("Select * from " + WikiTables.CATEGORY)) {
      while (r.next()) {//hasNext) {
        String label = StringMethods.replaceSpace(r.getString(TableAttributes.NAME).toLowerCase());
        int wikiID = r.getInt(TableAttributes.PAGE_ID);
        ID = gm.addVertex(label, wikiID, IDWikiCategoryID);
        if (ID == -1) {
          continue;
        }
        int categorySize = computeNumberOfPages(wiki, wikiID);
        categoryVertexDB.put(ID, categorySize);
        printTimer("addCategoyNodes");
      }
    }
    wiki.closeConnection();
  }

  private static void addRedirections() throws Exception {//es
    int ID;
    Wikipedia wiki = new Wikipedia();
    startMethod("addRedirections");
    HashMap<Integer, Integer> redirects = new HashMap<>();
    try (ResultSet r = wiki.getResult("Select * from " + WikiTables.PAGE_REDIRECTS)) {

      while (r.next()) {//hasNext) {

        String disambLabel = StringMethods.addSpace(StringMethods.removeDisambiguation(StringMethods.replaceSpace(r.getString(TableAttributes.REDIRECTS))));
        ID = gm.addVertex(disambLabel, 0, null);
        if (ID == -1) {
          continue;
        }
        redirectVertexDB.add(ID);
        int wikiID = r.getInt(TableAttributes.ID);

        if (IDWikiPageID.inverse().containsKey(wikiID)) {
          int vertexID = IDWikiPageID.inverse().get(wikiID);
          if (redirects.containsKey(vertexID)) {
            redirects.put(vertexID, redirects.get(vertexID) + 1);
          } else {
            redirects.put(vertexID, 1);
          }
          gm.addEdge(ID, vertexID, StringMethods.REDIRECT_FORWARD);
        }
        printTimer("addRedirctions");
      }
      r.beforeFirst();
      while (r.next()) {
        String disambLabel = StringMethods.addSpace(StringMethods.removeDisambiguation(StringMethods.replaceSpace(r.getString(TableAttributes.REDIRECTS))));// label in lowerCase
        if (!allVertexDB.inverse().containsKey(disambLabel)) {
          continue;
        }
        int sourceVertexID = allVertexDB.inverse().get(disambLabel);
        int wikiID = r.getInt(TableAttributes.ID);
        if (IDWikiPageID.inverse().containsKey(wikiID)) {
          int pageVertexID = IDWikiPageID.inverse().get(wikiID);
          gm.addEdge(pageVertexID, sourceVertexID, StringMethods.REDIRECT_FORWARD / redirects.get(pageVertexID));
        }
        printTimer("addRedirctionions");
      }
    }
    wiki.closeConnection();
  }

  public static void buildForwardEdgesText(int sourceVertexID, HashMap<String, Integer> wordFrequency) {
    int sum = 0;

    HashMap<String, Integer> counter = new HashMap<>();
    for (String word : wordFrequency.keySet()) {
      if (wordFormVertexDB.contains(allVertexDB.inverse().get(word))) {
        int destinationVertexID = allVertexDB.inverse().get(word);
        if (sourceVertexID == destinationVertexID) {
          continue;
        }
        int count = wordFrequency.get(word);
        sum = sum + count;
        counter.put(word, count);
      }
    }
    if (sum == 0) {
      return;
    }
    for (String word : counter.keySet()) {
      int destinationVertexID = allVertexDB.inverse().get(word);
      int amount = counter.get(word);
      gm.addEdge(sourceVertexID, destinationVertexID, GraphMethods.computeMinMax(0, 0.05, amount * 1.0 / sum));
      gm.appendRelationship(wfToWikiText, destinationVertexID, sourceVertexID);
      WordFormVertex wfVertex = wfInfo.get(destinationVertexID);
      wfVertex.addTextCount(amount);
      edgeInfoDB.put(destinationVertexID + "-" + sourceVertexID, amount);
    }
  }

  public static void buildForwardEdges(int sourceVertexID, ArrayList<String> words, double minValue, double maxValue,
          HashMap<Integer, ArrayList<Integer>> reverseRelationship, RelTypes relType) {
    int sum = 0;

    HashMap<String, Integer> counter = new HashMap<>();
    for (String word : words) {
      if (wordFormVertexDB.contains(allVertexDB.inverse().get(word))) {
        int destinationVertexID = allVertexDB.inverse().get(word);
        if (sourceVertexID == destinationVertexID) {
          continue;
        }
        sum++;
        if (counter.containsKey(word)) {
          counter.put(word, counter.get(word) + 1);
        } else {
          counter.put(word, 1);
        }
      }
    }
    if (sum == 0) {
      return;
    }
    for (String word : counter.keySet()) {
      if (wordFormVertexDB.contains(allVertexDB.inverse().get(word))) {
        int destinationVertexID = allVertexDB.inverse().get(word);
        int amount = counter.get(word);
        gm.addEdge(sourceVertexID, destinationVertexID, GraphMethods.computeMinMax(minValue, maxValue, amount * 1.0 / sum));
        gm.appendRelationship(reverseRelationship, destinationVertexID, sourceVertexID);
        WordFormVertex wfVertex = wfInfo.get(destinationVertexID);
        switch (relType) {
          case SUBTITLE:
            wfVertex.addSubTitleCount(amount);
            break;
          case TITLE:
            wfVertex.addTitleCount(amount);
            break;
        }
      }
    }
  }

  public static void buildFromPageTitle(int sourceVertexID, String label, double minValueTitle, double maxValueTitle,
          double minValueSubtitle, double maxValueSubtitle) {
    String title = u.removeSubtitle(label);
    buildForwardEdges(sourceVertexID, u.tokenize(title), minValueTitle, maxValueTitle, wfToWikiTitle, RelTypes.TITLE);
    if (u.containsSubtitle(label)) {
      String subtitle = u.getSubtitle(label);
      buildForwardEdges(sourceVertexID, u.tokenize(subtitle), minValueSubtitle, maxValueSubtitle, wfToWikiSubTitle, RelTypes.SUBTITLE);
    }
  }

  public static void addWikitoWordFormEdges() throws Exception {
    // int counter = 0;
    startMethod("addWikitoWordFormEdges");

    for (int categoryVertexID : categoryVertexDB.keySet()) {//CATEGORY PAGE TITLE - WORDFORM RELATIONSHIP
      buildFromPageTitle(categoryVertexID, allVertexDB.get(categoryVertexID), 0, 0.05, 0, 0.025);
    }

    for (int redirectVertexID : redirectVertexDB) {//REDIRECT PAGE TITLE - WORDFORM RELATIONSHIP
      buildFromPageTitle(redirectVertexID, allVertexDB.get(redirectVertexID), 0, 0.05, 0, 0.025);
    }

    Wikipedia wiki = new Wikipedia();
    for (Entry<Integer, WikiVertex> sourceVertex : wikiInfo.entrySet()) {//WIKIPEDIA PAGE TITLE - WORDFORM RELATIONSHIP
      int sourceVertexID = sourceVertex.getKey();
      String sourceVertexLabel = allVertexDB.get(sourceVertexID);
      int sourceVertexWikiID = IDWikiPageID.get(sourceVertexID);
      buildFromPageTitle(sourceVertexID, sourceVertexLabel, 0, 0.05, 0, 0.025);

      ResultSet r = wiki.getResult("Select * from " + WikiTables.PAGE + " where " + TableAttributes.PAGE_ID + " = "
              + sourceVertexWikiID);
      r.next();

      String cleanedText = u.cleanPageText(r.getString(TableAttributes.TEXT));
      r.close();
      HashMap<String, Integer> threeOccuranceWords = u.tokenizeTxt(cleanedText);

      buildForwardEdgesText(sourceVertexID, threeOccuranceWords);
      printTimer("addWikiToWordFormEdges");
    }

    wiki.closeConnection();
  }

  public static void addWFtoWikiHelper(int wfVertexID, HashMap<Integer, ArrayList<Integer>> relationship, double min, double max, long count,
          RelTypes type) {
    if (count == 0) {
      return;
    }
    String wfVertexLabel = allVertexDB.get(wfVertexID);
    if (!relationship.containsKey(wfVertexID)) {
      return;
    }
    for (Integer wikiVertexID : relationship.get(wfVertexID)) {
      String title = StringMethods.removeDisambiguation(allVertexDB.get(wikiVertexID));
      switch (type) {
        case TITLE:
          title = u.removeSubtitle(title);
          gm.addEdge(wfVertexID, wikiVertexID, GraphMethods.computeMinMax(min, max, u.getCount(wfVertexLabel, title) * 1.0 / count));
          break;
        case SUBTITLE:
          title = u.getSubtitle(title);
          gm.addEdge(wfVertexID, wikiVertexID, GraphMethods.computeMinMax(min, max, u.getCount(wfVertexLabel, title) * 1.0 / count));
          break;
        case TEXT:
          gm.addEdge(wfVertexID, wikiVertexID, GraphMethods.computeMinMax(min, max,
                  edgeInfoDB.get(wfVertexID + "-" + wikiVertexID) * 1.0 / count));
      }

    }
  }

  public static void addWFtoWikiEdge() throws Exception {//from WF to page title
    startMethod("addWFtoWikiPageTitle");

    for (Entry<Integer, WordFormVertex> wfVertex : wfInfo.entrySet()) {
      addWFtoWikiHelper(wfVertex.getKey(), wfToWikiTitle, 0, 0.05, wfVertex.getValue().titleCount, RelTypes.TITLE);
      addWFtoWikiHelper(wfVertex.getKey(), wfToWikiSubTitle, 0, 0.025, wfVertex.getValue().subtitleCount, RelTypes.SUBTITLE);
      addWFtoWikiHelper(wfVertex.getKey(), wfToWikiText, 0, 0.05, wfVertex.getValue().textCount, RelTypes.TEXT);
    }
  }

  public static void addSeeAlsoEdges() throws Exception {
    startMethod("addSeeAlsoEdges");
    Wikipedia wiki = new Wikipedia();

    for (int sourceWikiVertexID : wikiInfo.keySet()) {
      int sourceWikiVertexwikiID = IDWikiPageID.get(sourceWikiVertexID);
      ResultSet r = wiki.getResult("Select *  from " + WikiTables.PAGE + " where " + TableAttributes.ID + " = " + sourceWikiVertexwikiID);
      if (r.next()) {
        ArrayList<String> links = StringMethods.parseSeeAlsoLinks(r.getString(TableAttributes.TEXT));
        for (String link : links) {
          if (allVertexDB.inverse().containsKey(link)) {
            int destinationWikiVertexID = allVertexDB.inverse().get(link);
            if (sourceWikiVertexID != destinationWikiVertexID) {
              gm.addEdge(sourceWikiVertexID, destinationWikiVertexID, 0.05 / links.size());
              WikiVertex wikiVertex = wikiInfo.get(destinationWikiVertexID);
              if (wikiVertex != null) {
                wikiVertex.seeAlsoOutlinkCount++;
              }
              gm.appendRelationship(seeAlsoReverse, destinationWikiVertexID, sourceWikiVertexID);
            }
          }
        }
        r.close();
      }
    }
    wiki.closeConnection();

    for (int wikiVertexID : wikiInfo.keySet()) {
      if (seeAlsoReverse.get(wikiVertexID) == null) {
        continue;
      }
      for (int destinationWikiVertexID : seeAlsoReverse.get(wikiVertexID)) {
        int count = wikiInfo.get(destinationWikiVertexID).seeAlsoOutlinkCount;
        if (count > 0) {
          gm.addEdge(destinationWikiVertexID, wikiVertexID, 0.05 / count);
        }
      }
    }
  }

  public static void addWikiHyperlinkEdges() throws Exception {
    startMethod("addWikiHyperLinkEdges");
    ArrayList<Integer> skipPages = new ArrayList<>();
    skipPages.add(894164);
    skipPages.add(48361);
    skipPages.add(3434750);
    Wikipedia wiki = new Wikipedia();

    for (Entry<Integer, WikiVertex> sourceWikiVertex : wikiInfo.entrySet()) {
      printTimer("addWikiHyperlinkEdges");
      int sourceWikiVertexID = sourceWikiVertex.getKey();
      int sourceWikiVertexWikiID = IDWikiPageID.get(sourceWikiVertexID);
      if (!skipPages.contains(sourceWikiVertexWikiID)) {
        ResultSet r = wiki.getResult("Select * from " + WikiTables.PAGE_INLINKS + " where " + TableAttributes.ID + " = " + sourceWikiVertexWikiID);

        int hyperLinksCount = 0;
        ArrayList<Integer> hyperlinks = new ArrayList<>();
        HashMap<Integer, Integer> map = new HashMap<>();
        while (r.next()) {
          hyperLinksCount++;
          int link = r.getInt(TableAttributes.INLINKS);
          u.updateHashMap(map, link);
          if (!hyperlinks.contains(link)) {
            hyperlinks.add(link);
          }
        }
        r.close();
        Iterator iterator = hyperlinks.iterator();
        while (iterator.hasNext()) {
          int destinationWikiVertexID = (Integer) iterator.next();
          if (sourceWikiVertexID != destinationWikiVertexID) {
            if (wikiInfo.containsKey(destinationWikiVertexID)) {
              int outLinksCount = wikiInfo.get(destinationWikiVertexID).hyperlinkOutlinkCount;
              gm.addEdge(sourceWikiVertexID, destinationWikiVertexID, map.get(destinationWikiVertexID) * 0.025 / hyperLinksCount);
              gm.addEdge(destinationWikiVertexID, sourceWikiVertexID, 0.025 / outLinksCount);
            }
          }
        }
      }

    }

    wiki.closeConnection();
  }

  public static void addCategorySubCategoryEdges() throws Exception {
    startMethod("addCategorySubCategoryEdges");
    Wikipedia wiki = new Wikipedia();

    for (Entry<Integer, Integer> categoryVertex : categoryVertexDB.entrySet()) {
      int categoryVertexID = categoryVertex.getKey();
      int categoryVertexWikiID = IDWikiCategoryID.get(categoryVertexID);
      ResultSet r = wiki.getResult("Select * from " + WikiTables.CATEGORY_OUTLINKS + " where " + TableAttributes.ID + " = " + categoryVertexWikiID);
      int totalPages = 0;
      while (r.next()) {
        int subCategoryVertexWikiID = r.getInt(TableAttributes.OUTLINKS);
        if (IDWikiCategoryID.inverse().containsKey(subCategoryVertexWikiID)) {
          int subCaegoryVertexID = IDWikiCategoryID.inverse().get(subCategoryVertexWikiID);
          totalPages += categoryVertexDB.get(subCaegoryVertexID);
        }
      }
      if (totalPages != 0) {
        r.beforeFirst();
        while (r.next()) {
          int subCategoryVertexWikiID = r.getInt(TableAttributes.OUTLINKS);
          if (!IDWikiCategoryID.inverse().containsKey(subCategoryVertexWikiID)) {
            continue;
          }
          int subCategoryVertexID = IDWikiCategoryID.inverse().get(subCategoryVertexWikiID);
          int subCategoryVertexSize = categoryVertexDB.get(subCategoryVertexID);
          if (subCategoryVertexID != categoryVertexID) {//seven categories in mysql table that are its own sub-category
            gm.addEdge(categoryVertexID, subCategoryVertexID, 0.025 * subCategoryVertexSize / totalPages);
            gm.addEdge(subCategoryVertexID, categoryVertexID, 0.025);
          }
        }
        r.close();
      }

    }
    wiki.closeConnection();
  }

  private static void addPageCategoryLinks() throws Exception {
    Wikipedia wiki = new Wikipedia();
    startMethod("addPageCategoryLinks");
    for (Entry<Integer, Integer> categoryVertex : categoryVertexDB.entrySet()) {
      int categoryVertexID = categoryVertex.getKey();
      int categoryVertexWikiID = IDWikiCategoryID.get(categoryVertexID);
      int categoryVertexSize = categoryVertex.getValue();
      if (categoryVertexSize != 0) {
        ResultSet r = wiki.getResult("Select * from " + WikiTables.CATEGORY_PAGES + " where " + TableAttributes.ID + " = " + categoryVertexWikiID);
        while (r.next()) {
          int wikiVertexWikiID = r.getInt(TableAttributes.PAGES);

          if (IDWikiPageID.inverse().containsKey(wikiVertexWikiID)) {
            int wikiVertexID = IDWikiPageID.inverse().get(wikiVertexWikiID);
            gm.addEdge(categoryVertexID, wikiVertexID, 0.025 / categoryVertexSize);
            gm.addEdge(wikiVertexID, categoryVertexID, 0.025);
          }
        }
        r.close();
      }
    }

    wiki.closeConnection();
  }
}
