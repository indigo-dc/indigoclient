/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import java.util.HashMap;
import org.apache.commons.cli.Option;

/**
 * Comparator class for Options. This is singleton as we want to fill it
 * only once - when options are created.
 * This class is used by HelpFormatter
 * @author michalo
 */
public final class OptionComparator implements java.util.Comparator {

  private static OptionComparator comparator = null;
  private HashMap<String, String> options;

  /**
   * We want just one instance of OptionComparator. In fact, there is no sense
   * to have more of them.
   */
  private OptionComparator() {
    options = new HashMap<String, String>();
  }

  /**
   * We are comparing values stored under keys that reflect names of options.
   * options(o1).compareTo(o2)
   * @param o1 First option to compare
   * @param o2 Second option to compare
   * @return Returns result of comparison.
   */
  @Override
  public int compare(final Object o1, final Object o2) {
    String o1value = null;
    if (options.get(((Option) o1).getOpt()) == null) {
      o1value = options.get(((Option) o1).getLongOpt());
    } else {
      o1value = options.get(((Option) o1).getOpt());
    }

    String o2value = null;
    if (options.get(((Option) o2).getOpt()) == null) {
      o2value = options.get(((Option) o2).getLongOpt());
    } else {
      o2value = options.get(((Option) o2).getOpt());
    }

    return o1value.compareTo(o2value);
  }

  public static OptionComparator getInstance() {
    if (comparator == null) {
      comparator = new OptionComparator();
    }
    return comparator;
  }

  /**
   * We can add options with idxes that are use to compare options.
   * Idxes are compared as regular strings.
   * @param option Name of the option to be added
   * @param idx sort index we are looking for
   */
  public void addOption(final String option, final String idx) {
    options.put(option, idx);
  }

}
