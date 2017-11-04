package io.dkozak.sfc.proj.fuzzy;

import io.dkozak.sfc.proj.utils.DataFunction;
import javafx.scene.chart.LineChart;

import java.util.function.Function;

public class FuzzySet {
    public static final FuzzySet NULL = new FuzzySet("NULL", new DataFunction(Function.identity()));

    private final DataFunction memberFunction;
    private final String name;

    public FuzzySet(String name, DataFunction memberFunction) {
        this.name = name;
        this.memberFunction = memberFunction;
    }

    public FuzzySet union(FuzzySet oth) {
        Function<Number, Number> our = memberFunction.getFunction();
        Function<Number, Number> other = oth.memberFunction.getFunction();

        Function<Number, Number> union = input -> {
            double left = our.apply(input)
                             .doubleValue();
            double right = other.apply(input)
                                .doubleValue();
            return Math.max(left, right);
        };

        return new FuzzySet("Union of " + this.name + " and " + oth.name, new DataFunction(union));
    }

    public FuzzySet intersect(FuzzySet oth) {
        Function<Number, Number> our = memberFunction.getFunction();
        Function<Number, Number> other = oth.memberFunction.getFunction();

        Function<Number, Number> join = input -> {
            double left = our.apply(input)
                             .doubleValue();
            double right = other.apply(input)
                                .doubleValue();
            return Math.min(left, right);
        };

        return new FuzzySet("Intersection of " + this.name + " and " + oth.name, new DataFunction(join));
    }

    public FuzzySet complement() {
        Function<Number, Number> current = memberFunction.getFunction();
        Function<Number, Number> complement = input -> 1 - current.apply(input)
                                                                  .doubleValue();

        return new FuzzySet("Complement of " + name, new DataFunction(complement));
    }

    public void visualize(LineChart<Number, Number> chart) {
        memberFunction.visualizeData(chart, this.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FuzzySet fuzzySet = (FuzzySet) o;

        return name.equals(fuzzySet.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }
}
