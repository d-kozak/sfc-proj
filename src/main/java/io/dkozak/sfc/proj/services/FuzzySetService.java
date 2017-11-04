package io.dkozak.sfc.proj.services;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class FuzzySetService {

    private Map<String, FuzzySet> sets = new HashMap<>();

    public void addSet(FuzzySet set) {
        sets.put(set.getName(), set);
    }

    public List<FuzzySet> getSets() {
        return sets.entrySet()
                   .stream()
                   .map(Map.Entry::getValue)
                   .collect(toList());
    }

    public FuzzySet getSet(String name) {
        FuzzySet fuzzySet = sets.get(name);
        return fuzzySet != null ? fuzzySet : FuzzySet.NULL;
    }
}
