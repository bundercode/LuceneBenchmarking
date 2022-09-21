package Util;

import java.io.Serializable;



public class WikiVertex implements Serializable{

  public int seeAlsoOutlinkCount;
  public int hyperlinkOutlinkCount;

  public WikiVertex(int seeAlsoOutlinkCount, int hyperlinkOutlinkCount) {
    this.seeAlsoOutlinkCount=seeAlsoOutlinkCount;
    this.hyperlinkOutlinkCount = hyperlinkOutlinkCount;
  }

 
  public String toString(){
    return "seeAlso = "+seeAlsoOutlinkCount+ 
            " hyperlink = "+hyperlinkOutlinkCount;
  }
}