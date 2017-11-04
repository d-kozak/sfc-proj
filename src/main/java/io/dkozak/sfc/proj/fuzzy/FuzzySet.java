package io.dkozak.sfc.proj.fuzzy;

import io.dkozak.sfc.proj.utils.DataFunction;
import javafx.scene.chart.LineChart;

import java.util.function.Function;

public class FuzzySet {
    private DataFunction memberFunction;

    public FuzzySet(DataFunction memberFunction) {
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

        return new FuzzySet(new DataFunction(union));
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

        return new FuzzySet(new DataFunction(join));
    }

    public FuzzySet complement() {
        Function<Number, Number> current = memberFunction.getFunction();
        Function<Number, Number> complement = input -> 1 - current.apply(input)
                                                                  .doubleValue();

        return new FuzzySet(new DataFunction(complement));
    }

    public void visualize(LineChart<Number, Number> chart, String name) {
        memberFunction.visualizeData(chart, name);
    }
}
