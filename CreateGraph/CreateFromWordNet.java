/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CreateGraph;

import edu.smu.tspell.wordnet.*;
import java.io.File;
import java.util.*;
import Util.*;
import com.google.common.collect.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map.Entry;

public class CreateFromWordNet {

  public static StringMethods u = new StringMethods();
  public static HashMap<String, Double> edgeDB = new HashMap<>();
  static HashBiMap<Integer, String> allVertexDB = HashBiMap.create();
  static HashSet<Integer> wordFormVertexDB = new HashSet<>();
  static HashBiMap<Integer, String> senseVertexDB = HashBiMap.create();
  static HashMap<Integer, ArrayList<Integer>> relationshipDB = new HashMap<>();
  static HashMap<Integer, ArrayList<Integer>> definitionRelationshipDB = new HashMap<>();
  static HashMap<Integer, ArrayList<Integer>> exampleUseRelationshipDB = new HashMap<>();
  static WordTree w;
  static HashMap<String, Synset> synsetMap = new HashMap<>();
  static HashMap<String, Double> frequency = new HashMap<>();
  static GraphMethods gm;
  static int ID = 0;

  public static void main(String[] args) throws Exception {
    w = new WordTree();
    gm = new GraphMethods(edgeDB, relationshipDB,allVertexDB,0,u);

    populateWordFormVertices();
    System.out.println("1");
    populateSenses(); // Figure 1 & 2
    System.out.println("2");
    addDefinitionEdges(); // Step 3
    System.out.println("3");
    addExampleUseEdges(); // Step 4
    System.out.println("4");
    addWordContainment();  // Step 5
    System.out.println("5");
    populateFrequencies();
    System.out.println("6");
    computeRelationships(); //Figure 5, Step 6&7
    //store("/home/cs460/graph.data");
    store("graph.data");
  }

  public static void store(String fileName) throws Exception {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
      out.writeInt(ID);
      out.writeObject(edgeDB);
      out.writeObject(allVertexDB);
      out.writeObject(wordFormVertexDB);
      out.writeObject(relationshipDB);
    }
  }

  private static void populateWF(String fileName) throws Exception {
    try (Scanner fs = new Scanner(new File(fileName))) {
      for (int i = 0; i < 29; i++) {
        fs.nextLine();
      }
      while (fs.hasNextLine()) {
        String s = fs.nextLine().toLowerCase();
        StringTokenizer st = new StringTokenizer(s);
        String s1 = StringMethods.replaceSpace(st.nextToken()); // get only first word and replace underscore with space

        if (!u.isNoiseWord(s1)) {
          if (!allVertexDB.inverse().containsKey(s1)) {
            allVertexDB.put(++ID, s1);
            wordFormVertexDB.add(ID);
          }
        }

      }
    }
  }

  public static void populateWordFormVertices() throws Exception {
    //populateWF("/home/lubo/wordnet/dict/index.noun");
    //populateWF("/home/lubo/wordnet/dict/index.adj");
    //populateWF("/home/lubo/wordnet/dict/index.verb");
    //populateWF("/home/lubo/wordnet/dict/index.adv");
    populateWF("wordnet/dict/index.noun");
    populateWF("wordnet/dict/index.adj");
    populateWF("wordnet/dict/index.verb");
    populateWF("wordnet/dict/index.adv");
  }

  public static void populateSenses() {
    for (int wfID : wordFormVertexDB) {
      String wordForm = allVertexDB.get(wfID);
      Synset[] synsets = w.getDatabase().getSynsets(wordForm);
      double totalFrequency = w.getTotalFrequency(wordForm);

      for (Synset s : synsets) {
        String label = StringMethods.removeBrackets(s.getDefinition().toLowerCase());

        if (senseVertexDB.inverse().containsKey(label)) {// old sense
          int senseVertexID = senseVertexDB.inverse().get(label);
          gm.addEdge(wfID, senseVertexID, w.getFrequency(wordForm, s) / totalFrequency);
          gm.addEdge(senseVertexID, wfID, 1);
        } else { //new sense
          synsetMap.put(label, s);
          int senseVertexID;
          if(!allVertexDB.inverse().containsKey(label)){ //new label
            allVertexDB.put(++ID, label);
            senseVertexID = ID;
          } else { // old label
            senseVertexID = allVertexDB.inverse().get(label);
          }
          senseVertexDB.put(senseVertexID, label);
          gm.addEdge(wfID, senseVertexID, w.getFrequency(wordForm, s) / totalFrequency);
          gm.addEdge(senseVertexID, wfID, 1);
        }
      }
    }

  }

  public static void addDefinitionEdges() {
    for (Entry<Integer, String> senseVertex : senseVertexDB.entrySet()) {
      HashMap<Integer, String> wfVertices = new HashMap<>();
      String definition = senseVertex.getValue().toLowerCase();
      ArrayList<String> words = u.tokenize(definition);
      int sum = 0;
      for (String word : words) {
        if (allVertexDB.inverse().containsKey(word)) {
          int wordID = allVertexDB.inverse().get(word);
          sum = sum + u.getCount(word, definition);
          wfVertices.put(wordID, word);
        }
      }
      if (sum == 0) {
        continue;
      }
      double coef = 1;
      for (Entry<Integer, String> newVertex : wfVertices.entrySet()) {
        gm.addEdge(senseVertex.getKey(), newVertex.getKey(), coef * GraphMethods.computeMinMax(StringMethods.DEF_MIN, StringMethods.DEF_MAX, 1.0 * u.getCount(newVertex.getValue(), definition) / sum));
        if (coef > 0.3) {
          coef -= 0.2;
        }
        gm.appendRelationship(definitionRelationshipDB, newVertex.getKey(), senseVertex.getKey());
      }
    }
  }

  public static void addExampleUseEdges() {
    for (Entry<Integer, String> senseVertex : senseVertexDB.entrySet()) {
      HashMap<Integer, String> wfVertices = new HashMap<>();
      Synset sense = getSynset(senseVertex.getValue());
      String[] exampleUses = sense.getUsageExamples();
      for (String exampleUse : exampleUses) {
        ArrayList<String> words = u.tokenize(StringMethods.replaceSpace(exampleUse.toLowerCase()));
        int sum = 0;
        for (String word : words) {
          if (allVertexDB.inverse().containsKey(word)) {
            int wordID = allVertexDB.inverse().get(word);
            if (!Arrays.asList(sense.getWordForms()).contains(word)) {
              sum = sum + u.getCount(word, exampleUse);
              wfVertices.put(wordID, word);
            }
          }
        }
        if (sum == 0) {
          continue;
        }
        for (Entry<Integer, String> destinationVertex : wfVertices.entrySet()) {
          gm.addEdge(senseVertex.getKey(), destinationVertex.getKey(), GraphMethods.computeMinMax(StringMethods.EXAMPLE_USE_MIN, StringMethods.EXAMPLE_USE_MAX, u.getCount(destinationVertex.getValue(), exampleUse) * 1.0 / sum));
          gm.appendRelationship(exampleUseRelationshipDB, destinationVertex.getKey(), senseVertex.getKey());
        }
      }
    }
  }

  private static Synset getSynset(String label) {
    return synsetMap.get(label);
  }

  public static void computeBackwardRelationship(HashMap<Integer, ArrayList<Integer>> db, double min, double max) {
    for (Entry<Integer, ArrayList<Integer>> r : db.entrySet()) {
      int count = 0;
      String sourceVertex = allVertexDB.get(r.getKey());
      for (int destinationID : r.getValue()) {
        count += u.getCount(sourceVertex, senseVertexDB.get(destinationID));
      }
      if (count == 0) {
        continue;
      }
      for (int destinationID : r.getValue()) {
        String destinationVertex = senseVertexDB.get(destinationID);
        gm.addEdge(r.getKey(), destinationID, GraphMethods.computeMinMax(min, max,
                1.0 * u.getCount(sourceVertex, destinationVertex) / count));
      }
    }
  }

  public static void addWordContainment() {
    computeBackwardRelationship(definitionRelationshipDB, StringMethods.WORD_CONTAINMENT_DEFINITION_MIN, StringMethods.WORD_CONTAINMENT_DEFINITION_MAX);
    computeBackwardRelationship(exampleUseRelationshipDB, StringMethods.WORD_CONTAINMENT_EXAMPLE_USE_MIN, StringMethods.WORD_CONTAINMENT_EXAMPLE_USE_MAX);
  }

  public static void populateFrequencies() throws Exception {
    //try (Scanner fs = new Scanner(new File("/home/lubo/wordnet/words"))) {
    try (Scanner fs = new Scanner(new File("wordnet/words"))) {
      fs.nextLine();
      while (fs.hasNextLine()) {
        String s = fs.nextLine();
        StringTokenizer st = new StringTokenizer(s);
        int f = Integer.parseInt(st.nextToken());
        String s1 = StringMethods.replaceSpace(st.nextToken()).toLowerCase();
        if (allVertexDB.inverse().containsKey(s1)) {
          frequency.put(s1, f * 1.0);
        }
      }
    }

    for (Entry<Integer, String> senseVertex : senseVertexDB.entrySet()) {
      Synset s = getSynset(senseVertex.getValue());
      String[] wordForms = s.getWordForms();
      double sum = 0;
      for (String word : wordForms) {
        if (allVertexDB.inverse().containsKey(word) && frequency.containsKey(word)) {
          double expr = (w.getFrequency(word, s) / w.getTotalFrequency(word));
          sum = sum + frequency.get(word) * expr;
        }
      }
      frequency.put(senseVertex.getValue(), (Double) (Math.max(1, sum)));
    }
  }

  public static void computeRelationships() {
    for (Entry<Integer, String> senseVertex : senseVertexDB.entrySet()) {
      Synset sense = getSynset(senseVertex.getValue());
      int senseVertexID = senseVertex.getKey();
      if (sense.getType() == SynsetType.ADJECTIVE) {
        AdjectiveSynset adjSense = (AdjectiveSynset) sense;
        AdjectiveSynset[] related = adjSense.getRelated(); // related adjectives
        for (AdjectiveSynset adjSynset : related) {
          String label = adjSynset.getDefinition().toLowerCase();
          if (senseVertexDB.inverse().containsKey(label)) {
            int destinationVertexID = senseVertexDB.inverse().get(label);
            gm.addEdge(senseVertexID, destinationVertexID, StringMethods.alpha8);
          }
        }
        AdjectiveSynset[] similar = adjSense.getSimilar(); //similar adjectives
        for (AdjectiveSynset adjSynset : similar) {
          String label = adjSynset.getDefinition().toLowerCase();
          if (senseVertexDB.inverse().containsKey(label)) {
            int destinationVertexID = senseVertexDB.inverse().get(label);
            gm.addEdge(senseVertexID, destinationVertexID, StringMethods.alpha9);
          }
        }
      }

      if (sense.getType() == SynsetType.VERB) {
        VerbSynset verbSense = (VerbSynset) sense;

        VerbSynset[] troponyms = verbSense.getTroponyms();//troponyms
        int totalSum = 0;
        for (VerbSynset verbSynset : troponyms) {
          String label = verbSynset.getDefinition().toLowerCase();
          if (senseVertexDB.inverse().containsKey(label)) {
            double f = frequency.get(label);
            totalSum += (f == 0) ? 1 : f;
          }
        }
        for (VerbSynset verbSynset : troponyms) {
          String label = verbSynset.getDefinition().toLowerCase();
          if (totalSum != 0 && senseVertexDB.inverse().containsKey(label)) {
            int destinationVertexID = senseVertexDB.inverse().get(label);
            double f = frequency.get(label);
            gm.addEdge(senseVertexID, destinationVertexID, StringMethods.alpha4 * ((f == 0) ? 1 : f) / totalSum);
            gm.addEdge(destinationVertexID, senseVertexID, StringMethods.REVERSE_EDGE);
          }
        }


        VerbSynset[] hypernyms = verbSense.getHypernyms();//hypernyms
        for (VerbSynset verbSynset : hypernyms) {
          String label = verbSynset.getDefinition().toLowerCase();
          if (senseVertexDB.inverse().containsKey(label) ) {
            int destinationVertexID = senseVertexDB.inverse().get(label);
            gm.addEdge(senseVertexID, destinationVertexID, StringMethods.alpha5);
            gm.addEdge(destinationVertexID, senseVertexID, StringMethods.REVERSE_EDGE);
          }
        }
      }
      if (sense.getType() == SynsetType.NOUN) {
        NounSynset nounSense = (NounSynset) sense;
        NounSynset[] hyponyms = nounSense.getHyponyms();//hyponyms
        int totalSum = 0;
        for (NounSynset nounSynset : hyponyms) {
          String label = nounSynset.getDefinition().toLowerCase();
          if (senseVertexDB.inverse().containsKey(label)) {
            double f = frequency.get(label);
            if (f == 0) {
              totalSum++;
            } else {
              totalSum += f;
            }
          }
        }
        for (NounSynset nounSynset : hyponyms) {
          String label = nounSynset.getDefinition().toLowerCase();
          if (senseVertexDB.inverse().containsKey(label) && totalSum != 0) {
            int destinationVertexID = senseVertexDB.inverse().get(label);
            double f = frequency.get(label);
            gm.addEdge(senseVertexID, destinationVertexID, StringMethods.alpha4 * (f == 0 ? 1 : f) / totalSum);
            gm.addEdge(destinationVertexID, senseVertexID, StringMethods.REVERSE_EDGE);
          }
        }


        ArrayList<NounSynset> meronyms = new ArrayList<>();//meronyms
        meronyms.addAll(Arrays.asList(nounSense.getPartMeronyms()));
        meronyms.addAll(Arrays.asList(nounSense.getMemberMeronyms()));
        meronyms.addAll(Arrays.asList(nounSense.getSubstanceMeronyms()));

        for (NounSynset nounSynset : meronyms) {
          String label = nounSynset.getDefinition().toLowerCase();

          if (senseVertexDB.inverse().containsKey(label)) {
            int destinationVertexID = senseVertexDB.inverse().get(label);
            gm.addEdge(senseVertexID, destinationVertexID, StringMethods.alpha6 / meronyms.size());
            gm.addEdge(destinationVertexID, senseVertexID, StringMethods.REVERSE_EDGE_SMALL);
          }
        }
      }
    }
  }
}