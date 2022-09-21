package Util;

import java.io.Serializable;



public class WordFormVertex implements Serializable{

  public long titleCount = 0;
  public long subtitleCount = 0;
  public long textCount = 0;

  public void addTitleCount(int amount) {
    titleCount += amount;
  }

  public void addSubTitleCount(int amount) {
    subtitleCount += amount;
  }

  public void addTextCount(int amount) {
    textCount += amount;
  }

  public String toString() {
    return "titleCount = "+titleCount +" subtitleCount = "+subtitleCount+" textCount = "+textCount;
            
  }
}