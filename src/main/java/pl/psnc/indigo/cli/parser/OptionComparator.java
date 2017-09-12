/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import java.util.HashMap;
import org.apache.commons.cli.Option;

/**
 *
 * @author michalo
 */
public class OptionComparator implements java.util.Comparator {

  private static OptionComparator comparator = null;
  private HashMap<String, String> options;
  
  private OptionComparator() {
    options = new HashMap<String, String>();
  }
  
  /**
   * We are comparing values stored under keys that reflect names of options
   * options(o1).compareTo(o2)
   * @param o1
   * @param o2
   * @return 
   */
  @Override
  public int compare(Object o1, Object o2) {
    String o1value = options.get( ((Option)o1).getOpt() == null ? ((Option)o1).getLongOpt() : ((Option)o1).getOpt() );
    String o2value = options.get( ((Option)o2).getOpt() == null ? ((Option)o2).getLongOpt() : ((Option)o2).getOpt() );
    
    return o1value.compareTo(o2value);   
  }
  
  public static OptionComparator getInstance() {
    if(comparator == null) {
      comparator = new OptionComparator();
    }
    return comparator;
  }
  
  /**
   * We can add options with idxes that are use to compare options.
   * Idxes are compared as regular strings. 
   * @param option
   * @param idx 
   */
  public void addOption(String option, String idx) {
    options.put(option, idx);
  }
  
}
