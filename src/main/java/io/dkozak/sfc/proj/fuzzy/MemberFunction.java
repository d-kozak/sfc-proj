package io.dkozak.sfc.proj.fuzzy;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.util.function.Function;

public class MemberFunction {
    public static MemberFunction linear(double a, double b, double c, double d) {
        double k = (d - b) / (c - a);
        return new MemberFunction(x -> x.doubleValue() * k);
    }

    public static MemberFunction constant(double a) {
        return new MemberFunction(x -> a);
    }


    public static MemberFunction gaussian(double mi, double sigma) {
        return new MemberFunction(x -> Math.exp((-0.5) * Math.abs((x.doubleValue() - mi) / sigma)));
    }

    public static MemberFunction normalDistribution(double mi, double sigma) {
        return new MemberFunction(x -> ((1.0 / (sigma * Math.sqrt(2 * Math.PI))) * (Math.exp((-0.5) * Math.abs((x.doubleValue() - mi) / (double) sigma)))));
    }

    public static MemberFunction triangle(double a, double b, double c) {
        return new MemberFunction(input -> {
            double x = input.doubleValue();
            double val = Math.min(((x - a) / (b - a)), ((c - x) / (c - b)));
            return val >= 0 ? val : 0;
        });
    }

    public static MemberFunction trapezoid(double a, double b, double c, double d) {
        return new MemberFunction(input -> {
            double x = input.doubleValue();
            double val = Math.min((x - a) / (b - a), (d - x) / (d - c));
            val = val <= 1 ? val : 1;
            return val >= 0 ? val : 0;
        });
    }

    private Function<Number, Number> function;

    public MemberFunction(Function<Number, Number> function) {
        this.function = function;
    }

    public void visualizeData(LineChart<Number, Number> lineChart, String name) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);


        for (int i = 0; i < 100; i++) {
            series.getData()
                  .add(new XYChart.Data<>(i, function.apply(i)));
        }

        lineChart.getData()
                 .add(series);
    }

    public Function<Number, Number> getFunction() {
        return function;
    }

    public void setFunction(Function<Number, Number> function) {
        this.function = function;
    }
}
