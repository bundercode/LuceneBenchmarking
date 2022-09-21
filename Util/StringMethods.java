package Util;

import java.util.*;
import java.util.Map.Entry;

public class StringMethods {

  public static double CUTOFF = 0.001;
  public static int DEPTH = 100;
  public static boolean READ_ONLY = true;
  public static final double REDIRECT_FORWARD = 0.2;
  public static final int WORD_COUNT = 10;
  public static final double DEF_MIN = 0;
  public static final double DEF_MAX = 0.6;
  public static final double EXAMPLE_USE_MIN = 0;
  public static final double EXAMPLE_USE_MAX = 0.2;
  public static final double WORD_CONTAINMENT_DEFINITION_MIN = 0;
  public static final double WORD_CONTAINMENT_DEFINITION_MAX = 0.3;
  public static final double WORD_CONTAINMENT_EXAMPLE_USE_MIN = 0;
  public static final double WORD_CONTAINMENT_EXAMPLE_USE_MAX = 0.1;
  public static final double REVERSE_EDGE = 0.3;
  public static final double REVERSE_EDGE_SMALL = 0.15;
  public static final double alpha1 = 1.8;
  public static final double alpha2 = 0.7;
  public static final double alpha3 = 1.0;
  public static final double alpha4 = 0.9;
  public static final double alpha5 = 0.9;
  public static final double alpha6 = 0.6;
  public static final double alpha7 = 0.6;
  public static final double alpha8 = 0.5;
  public static final double alpha9 = 0.9;
  public static final double alpha10 = 0.7;
  public static final double alpha11 = 0.6;
  public static final double beta1 = 0.6;
  public static final double beta2 = 0.3;
  /*
   * public static final double alpha1 = 1.0; public static final double
   * alpha2 = 1.0; public static final double alpha3 = 1.0; public static
   * final double alpha4 = 1.0; public static final double alpha5 = 1.0;
   * public static final double alpha6 = 1.0; public static final double
   * alpha7 = 1.0; public static final double alpha8 = 1.0; public static
   * final double alpha9 = 1.0;
   */
  public String noiseWordArray[] = {"a", "about", "above", "all", "along",
    "also", "although", "am", "an", "and", "any", "are", "aren''t", "as", "at",
    "be", "because", "been", "but", "by", "can", "cannot", "could", "couldn't",
    "did", "didn't", "do", "does", "doesn't", "e.g.", "either", "etc", "etc.",
    "even", "ever", "enough", "for", "from", "further", "get", "gets", "got", "had", "have",
    "hardly", "has", "hasn''t", "having", "he", "hence", "her", "here",
    "hereby", "herein", "hereof", "hereon", "hereto", "herewith", "him",
    "his", "how", "however", "i", "i.e.", "if", "in", "into", "it", "it''s", "its",
    "me", "more", "most", "mr", "my", "near", "nor", "now", "no", "not", "or", "on", "of", "onto",
    "other", "our", "out", "over", "really", "said", "same", "she",
    "should", "shouldn''t", "since", "so", "some", "such",
    "than", "that", "the", "their", "them", "then", "there", "thereby",
    "therefore", "therefrom", "therein", "thereof", "thereon", "thereto",
    "therewith", "these", "they", "this", "those", "through", "thus", "to",
    "too", "under", "until", "unto", "upon", "us", "very", "was", "wasn't",
    "we", "were", "what", "when", "where", "whereby", "wherein", "whether",
    "which", "while", "who", "whom", "whose", "why", "with", "without",
    "would", "you", "your", "yes"};
  public String tagWordsArray[] = {"ref"};
  HashSet<String> tagWords = new HashSet<>();
  // public String noiseWordArray[] = {"I"};
  HashSet<String> noiseWords = new HashSet<>();

  public StringMethods() {
    Collections.addAll(noiseWords, noiseWordArray);
    Collections.addAll(tagWords, tagWordsArray);//es
  }

  public boolean isNoiseWord(String word) {
    return noiseWords.contains(word);
  }

  public boolean isTagWord(String word) {//es
    return tagWords.contains(word);
  }

  public static String replaceSpace(String s) {
    StringBuffer result = new StringBuffer();

    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == '_' || s.charAt(i) == '\"' || s.charAt(i) == '/') {//es to remove '/' from title
        result.append(' ');
      } else {
        result.append(s.charAt(i));
      }
    }

    return new String(result);//.replaceAll(",", "");//es remove commas
  }
  
  public static boolean isUpperCase(char c){
    return c>='A' && c<='Z';
  }
  
  public static String addSpace(String label){
    StringBuffer result= new StringBuffer("");
    boolean seenLowerCase = false;
    for(int i=0; i < label.length(); i++){
      char c = label.charAt(i);
      if(isUpperCase(c)){
        c = (char) (c-'A'+'a');
        if(!seenLowerCase){
          result.append(c);
        } else {
          result.append(' ');
          result.append(c);
        }
      } else {
        result.append(c);
        seenLowerCase = true;
      }
    }
    return result.toString();
  }

  public static String removeDisambiguation(String label) {//ES
    String disambiguation = "(disambiguation)";
    String result;

    if (label.endsWith(disambiguation)) {
      result = label.substring(0, (label.length() - disambiguation.length()) - 1);//+1 to remove space at the end
      return result;
    }
    return label;
  }

  public boolean containsSubtitle(String label) {
    if (label.contains("(") && label.contains(")")) {
      if (label.indexOf("(") < label.indexOf(")")) {
        return true;
      }
      return false;
    } else {
      return false;
    }
  }

  public String removeSubtitle(String label) {
    String subtitleRegex = "(\\(([\\x00-\\x7F]*?)\\))+";
    String result = "";
    if (label.contains("(") && label.contains(")")) {
      if (label.indexOf("(") < label.indexOf(")")) {
        result = label.replaceAll(subtitleRegex, "");
        return result.trim();
      }
    }
    return label;
  }

  public String getSubtitle(String label) {
    String result = "";
    int beginIndex, endIndex;
    if (label.contains("(") && label.contains(")")) {
      beginIndex = label.indexOf("(");
      endIndex = label.indexOf(")");
      result = label.substring(beginIndex + 1, endIndex);
      return result.trim();
    }
    return label;

  }

  public String cleanPageText(String text) {//remove hyperlinks and special characters
    StringTokenizer tokenizer = new StringTokenizer(text, "\n");
    String urlRegex = "^http\\://[a-zA-Z0-9]\\-\\.[a-zA-Z]{2,3}(/\\s*)?$";
    String hyperLinkRegex = "(\\[\\[([\\x00-\\x7F]*?)\\]\\])+";
    String specialCharRegex = "[<>'\"\\[\\]/;:%\\{\\}\\^\\.\\&\\|@_~`\\(\\)!\\+#\\$\\*=\\?\\,]+";

    String line = "";
    String result = "";
    String cleanedLine = "";
    while (tokenizer.hasMoreTokens()) {
      line = tokenizer.nextToken().toLowerCase();
      if (line.startsWith("==") && line.contains("see") && line.contains("also") && line.endsWith("==")) {
        break;
      }
      cleanedLine = line.replaceAll(urlRegex, " ").replaceAll(hyperLinkRegex, " ").replaceAll(specialCharRegex, " ");
      result = result + " " + cleanedLine;

    }
    return result;
  }

  public static ArrayList<String> parseSeeAlsoLinks(String link) {//ES 06/14/13
    // System.out.println("Text length: "+ link.length());
    //   String text=link.toLowerCase();
    // ArrayList<String> result = new ArrayList<>();
    // int index=text.indexOf("==see also==");
    //   if(index!=-1){
    // String seeAlsoLines=text.substring(index, text.length());
    ArrayList<String> result = new ArrayList<>();
    Scanner scanner = new Scanner(link);
    //   StringTokenizer tokenizer = new StringTokenizer(seeAlsoLines, "\n");//link
    String line = "";
    String seeAlsoLink = "";

    while (scanner.hasNext()) {//tokenizer
      //line = tokenizer.nextToken().trim().toLowerCase();
      line = scanner.nextLine().trim().toLowerCase();
      if (line.startsWith("==") && line.contains("see") && line.contains("also") && line.endsWith("==") && scanner.hasNext()) {
        //line = tokenizer.nextToken();
        line = scanner.nextLine();
        while (!line.startsWith("==") && !line.endsWith("==")) {//(line.contains("[[") && line.contains("]]")) {
          int beginIndex = line.indexOf("[[");
          int endIndex = line.indexOf("]]");
          if (beginIndex > -1 && endIndex > -1 && beginIndex < endIndex) {
            seeAlsoLink = line.substring(beginIndex + 2, endIndex);
            result.add(removeBrackets(replaceSpace(seeAlsoLink).toLowerCase()));
          }
          if (scanner.hasNext()) {

            line = scanner.nextLine();
          } else {
            break;
          }
        }

      }

    }

    return result;
  }

  public ArrayList<String> tokenize(String s) {//ES 06/06/13

    ArrayList<String> result = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(s);
    String s1 = "";
    String s2 = "";
    String s3 = "";
    int count = st.countTokens();

    for (int i = count; i > 0; i--) {
      String myString = st.nextToken();
     
      if (!noiseWords.contains(myString.toLowerCase())) {
        result.add(myString.toLowerCase());
      }
      if (s1.equals("")) {
        s1 = myString;
        continue;
      }
      if (s2.equals("")) {
        s2 = myString;
        result.add(s1.toLowerCase() + " " + s2.toLowerCase());
        continue;
      }
      if (s3.equals("")) {
        s3 = myString;
        result.add(s1.toLowerCase() + " " + s2.toLowerCase() + " " + s3.toLowerCase());
        if (count == 1) {
          result.add(s2.toLowerCase() + " " + s3.toLowerCase());
        }
        continue;

      }
      s1 = s2;
      s2 = s3;
      s3 = myString;
      result.add(s1.toLowerCase() + " " + s2.toLowerCase());
      result.add(s1.toLowerCase() + " " + s2.toLowerCase() + " " + s3.toLowerCase());

      if (count == 1) {
        result.add(s2.toLowerCase() + " " + s3.toLowerCase());
      }
    }
    return result;
  }

  public void updateHashMap(HashMap<String,Integer> map, String myString) {//es
    if (!map.containsKey(myString.toLowerCase())) {
      map.put(myString.toLowerCase(), 1);
    } else {
      int key = (Integer) map.get(myString.toLowerCase());
      key++;
      map.put(myString.toLowerCase(), key);
    }
  }

  public void updateHashMap(HashMap<Integer,Integer> map, Integer id) {//es
    if (!map.containsKey(id)) {
      map.put(id, 1);
    } else {
      int count = (Integer) map.get(id);
      count++;
      map.put(id, count);
    }
  }

  public HashMap<String,Integer> tokenizeTxt(String s) {//ES 06/12/13
    //   String cleanText=cleanPageText(s);
    HashMap<String, Integer> map = new HashMap<>();
    HashMap<String,Integer> result = new HashMap<>();
    StringTokenizer st = new StringTokenizer(s);//cleanText
    String s1 = "";
    String s2 = "";
    String s3 = "";
    int count = st.countTokens();
    for (int i = count; i > 0; i--) {
      String myString = st.nextToken();
      if (tagWords.contains(myString.toLowerCase()) || myString.matches(".*\\d.*")) {//do not include tag words such as ref
        continue;
      }

      if (!noiseWords.contains(myString.toLowerCase())) {//|| myString.matches(".*\\d.*"))
        //remove () here
        updateHashMap(map, myString);
        //  result.add(myString.toLowerCase());
      }
      if (s1.equals("")) {
        s1 = myString;
        continue;
      }
      if (s2.equals("")) {
        s2 = myString;
        updateHashMap(map, s1.toLowerCase() + " " + s2.toLowerCase());
        //  result.add(s1.toLowerCase() + " " + s2.toLowerCase());
        continue;
      }
      if (s3.equals("")) {
        s3 = myString;
        updateHashMap(map, s1.toLowerCase() + " " + s2.toLowerCase() + " " + s3.toLowerCase());
        if (count == 1) {
          updateHashMap(map, s2.toLowerCase() + " " + s3.toLowerCase());
          //result.add(s2.toLowerCase() + " " + s3.toLowerCase());
        }
        continue;

      }
      s1 = s2;
      s2 = s3;
      s3 = myString;
      updateHashMap(map, s1.toLowerCase() + " " + s2.toLowerCase());
      updateHashMap(map, s1.toLowerCase() + " " + s2.toLowerCase() + " " + s3.toLowerCase());
      // result.add(s1.toLowerCase() + " " + s2.toLowerCase());
      //result.add(s1.toLowerCase() + " " + s2.toLowerCase() + " " + s3.toLowerCase());

      if (count == 1) {
        updateHashMap(map, s2.toLowerCase() + " " + s3.toLowerCase());
        // result.add(s2.toLowerCase() + " " + s3.toLowerCase());
      }
    }
    Iterator<Entry<String, Integer>> hashMapIterator = map.entrySet().iterator();
    Entry entry;
    for (int i = 0; i < map.size(); i++) {
      entry = hashMapIterator.next();
      if ((Integer) entry.getValue() >= WORD_COUNT) {
        result.put((String)entry.getKey(), (Integer)entry.getValue());
      }
    }
    return result;
  }

  /**
   *
   * @param word small string
   * @param s big string
   * @return the number of occurrences of word in s
   */
  public int getCount(String word, String s) {
    int count = 0;
    int size = word.length();
    for (int i = 0; i <= s.length() - size; i++) {
      if (s.substring(i, i + size).equalsIgnoreCase(word)) {
        count++;
      }
    }
    return count;
  }

  public static String replaceBrackets(String s) {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
      if (!(s.charAt(i) == '(' || s.charAt(i) == ')')) {
        result.append(s.charAt(i));
      }
    }
    return new String(result);
  }

  public static String removeBrackets(String s) {
    StringBuffer result = new StringBuffer();
    int counter = 0;
    boolean removeSpace = false;
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == '(') {
        counter++;
      } else if (s.charAt(i) == ')') {
        counter--;
        removeSpace = true;
      } else {
        if (counter == 0 && !removeSpace) {
          result.append(s.charAt(i));
        }
        removeSpace = false;
      }
    }
    return (new String(result)).trim();
  }
}
