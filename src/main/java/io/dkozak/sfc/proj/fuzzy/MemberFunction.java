package io.dkozak.sfc.proj.fuzzy;

import java.util.function.Function;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MemberFunction {
    public enum Type {
        UNKNOWN, LINEAR, CONSTANT, GAUSSIAN, NORMAL_DISTRIBUTION, TRIANGLE, TRAPEZOID;
    }

    public static MemberFunction linear(double a, double b, double c, double d) {
        double k = (d - b) / (c - a);
        return new MemberFunction(Type.LINEAR, x -> x.doubleValue() * k);
    }

    public static MemberFunction linearBounded(double k, double q) {
        return new MemberFunction(Type.LINEAR, x -> max(min(k * x.doubleValue() + q, 1), 0));
    }

    public static MemberFunction constant(double a) {
        return new MemberFunction(Type.CONSTANT, x -> a);
    }


    public static MemberFunction gaussian(double mi, double sigma) {
        return new MemberFunction(Type.GAUSSIAN, x -> Math.exp((-0.5) * Math.abs((x.doubleValue() - mi) / sigma)));
    }

    public static MemberFunction normalDistribution(double mi, double sigma) {
        return new MemberFunction(Type.NORMAL_DISTRIBUTION, x -> ((1.0 / (sigma * Math.sqrt(2 * Math.PI))) * (Math.exp((-0.5) * Math.abs((x.doubleValue() - mi) / (double) sigma)))));
    }

    public static MemberFunction triangle(double a, double b, double c) {
        return new MemberFunction(Type.TRIANGLE, input -> {
            double x = input.doubleValue();
            double val = min(((x - a) / (b - a)), ((c - x) / (c - b)));
            return val >= 0 ? val : 0;
        });
    }

    public static MemberFunction trapezoid(double a, double b, double c, double d) {
        return new MemberFunction(Type.TRAPEZOID, input -> {
            double x = input.doubleValue();
            double val = min((x - a) / (b - a), (d - x) / (d - c));
            val = val <= 1 ? val : 1;
            return val >= 0 ? val : 0;
        });
    }

    private Function<Number, Number> function;
    private MemberFunction.Type type;

    public MemberFunction(MemberFunction.Type type, Function<Number, Number> function) {
        this.type = type;
        this.function = function;
    }
    public Function<Number, Number> getFunction() {
        return function;
    }

    public void setFunction(Function<Number, Number> function) {
        this.function = function;
    }
}
