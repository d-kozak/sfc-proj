package io.dkozak.sfc.proj.fuzzy;

import java.util.function.Function;

public class FuzzySet {
    public static final FuzzySet NULL = new FuzzySet("NULL", new MemberFunction(MemberFunction.Type.UNKNOWN, Function.identity()));

    private MemberFunction memberFunction;
    private String name;

    public FuzzySet(MemberFunction memberFunction) {
        this.memberFunction = memberFunction;
        this.name = "NULL";
    }

    public FuzzySet(String name, MemberFunction memberFunction) {
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

        return new FuzzySet("Union of " + this.name + " and " + oth.name, new MemberFunction(MemberFunction.Type.UNKNOWN, union));
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

        return new FuzzySet("Intersection of " + this.name + " and " + oth.name, new MemberFunction(MemberFunction.Type.UNKNOWN, join));
    }

    public FuzzySet complement() {
        Function<Number, Number> current = memberFunction.getFunction();
        Function<Number, Number> complement = input -> 1 - current.apply(input)
                                                                  .doubleValue();

        return new FuzzySet("Complement of " + name, new MemberFunction(MemberFunction.Type.UNKNOWN, complement));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FuzzySet fuzzySet = (FuzzySet) o;

        return name.equals(fuzzySet.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public MemberFunction getMemberFunction() {
        return memberFunction;
    }

    public void setMemberFunction(MemberFunction memberFunction) {
        this.memberFunction = memberFunction;
    }

    public double getMaxValueFromInterval(int lowerBound, int uppperBound) {
        Function<Number, Number> function = memberFunction.getFunction();
        double max = function
                .apply(lowerBound)
                .doubleValue();
        for (int i = lowerBound + 1; i < uppperBound; i++) {
            double tmp = function.apply(i)
                                 .doubleValue();
            if (tmp > max)
                max = tmp;
        }

        return max;
    }
}
