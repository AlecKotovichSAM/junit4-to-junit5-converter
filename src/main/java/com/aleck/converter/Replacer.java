/**
 * 
 */
package com.aleck.converter;

import java.util.function.Function;

/**
 * @author Alec K
 *
 */
public class Replacer implements Function<String, String> {

    public final ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

    public ThreadLocal<Integer> getCounter() {
        return counter;
    }

    @Override
    public String apply(String line) {
        
        String found = Dictionary.TOKENS.get(line.trim()); // exact match

        if (found != null) {

            line = line.replace(line.trim(), found);

            counter.set(counter.get() + 1);
        } else {
            // Part of
            for (String k : Dictionary.TOKENS.keySet()) {
                if (line.indexOf(k) != -1) { // found
                    if (line.indexOf(Dictionary.TOKENS.get(k)) == -1) { // avoid double replacement
                        line = line.replace(k, Dictionary.TOKENS.get(k));
                        counter.set(counter.get() + 1);
                        break; // single match
                    }
                }
            }
        }
        
        
        // @Test(expected = ...
        if (line.indexOf("@Test") != -1 && line.indexOf("(expected") != -1) {
            line = line.replace("(expected", "//expected"); // comment out
            counter.set(counter.get() + 1);
        }
        
        return line;
    }

}
