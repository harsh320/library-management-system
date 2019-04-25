package com.example.navigation.others;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class sort_comp implements Comparator<String> {
    private final Map<String, Integer> freqMap;

    // Assign the specified map
    public sort_comp(HashMap<String, Integer> tFreqMap)
    {
        this.freqMap = tFreqMap;
    }

    // Compare the values
    @Override
    public int compare(String k1, String k2)
    {
        int freqCompare = freqMap.get(k2).compareTo(freqMap.get(k1));
            return freqCompare;
    }
}
