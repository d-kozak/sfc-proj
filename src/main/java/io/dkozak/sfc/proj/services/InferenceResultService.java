package io.dkozak.sfc.proj.services;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;

import java.util.ArrayList;
import java.util.List;

public class InferenceResultService {
    private final List<FuzzySet> results = new ArrayList<>();

    public void add(FuzzySet fuzzySet) {
        results.add(fuzzySet);
    }

    public List<FuzzySet> getResults() {
        return results;
    }
}
