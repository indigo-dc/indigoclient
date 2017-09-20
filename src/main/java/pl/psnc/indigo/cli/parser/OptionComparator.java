/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.Option;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Comparator class for Options. This is singleton as we want to fill it
 * only once - when options are created.
 * This class is used by HelpFormatter
 *
 * @author michalo
 */
public final class OptionComparator
        implements Comparator<Option>, Serializable {
    private static final long serialVersionUID = 3465955962064963921L;
    private static final OptionComparator INSTANCE = new OptionComparator();

    private final Map<String, String> options = new HashMap<>();

    /**
     * We want just one instance of OptionComparator. In fact, there is no sense
     * to have more of them.
     */
    private OptionComparator() {
        super();
    }

    /**
     * We are comparing values stored under keys that reflect names of options.
     * options(o1).compareTo(o2)
     *
     * @param t  First option to compare
     * @param t1 Second option to compare
     * @return Returns result of comparison.
     */
    @Override
    public int compare(final Option t, final Option t1) {
        final String o1;
        if (options.get(t.getOpt()) == null) {
            o1 = options.get(t.getLongOpt());
        } else {
            o1 = options.get(t.getOpt());
        }

        final String o2;
        if (options.get(t1.getOpt()) == null) {
            o2 = options.get(t1.getLongOpt());
        } else {
            o2 = options.get(t1.getOpt());
        }
        return o1.compareTo(o2);
    }

    public static OptionComparator getInstance() {
        return OptionComparator.INSTANCE;
    }

    /**
     * We can add options with idxes that are use to compare options.
     * Idxes are compared as regular strings.
     *
     * @param option Name of the option to be added
     * @param idx    sort index we are looking for
     */
    public void addOption(final String option, final String idx) {
        options.put(option, idx);
    }

}
