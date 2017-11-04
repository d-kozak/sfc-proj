package io.dkozak.sfc.proj.services;

import io.dkozak.sfc.proj.fuzzy.FuzzySet;

import java.util.ArrayList;
import java.util.List;

public class FuzzySetService {

    private List<FuzzySet> sets = new ArrayList<>();

    public void addSet(FuzzySet set) {
        sets.add(set);
    }

    public List<FuzzySet> getSets() {
        return sets;
    }
}
