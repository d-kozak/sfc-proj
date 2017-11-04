package io.dkozak.sfc.fuzzy.function;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.util.function.Function;

public class DataFunction {
    public static DataFunction linear(int a, int b, int c, int d) {
        int k = (d - b) / (c - a);
        return new DataFunction(x -> x.intValue() * k);
    }

    public static DataFunction constant(int a) {
        return new DataFunction(x -> a);
    }


    public static DataFunction gausian(double mi, double sigma) {
        return new DataFunction(x -> Math.exp((-0.5) * Math.abs((x.doubleValue() - mi) / (double) sigma)));
    }

    public static DataFunction normalDistribution(double mi, double sigma) {
        return new DataFunction(x -> ((1.0 / (sigma * Math.sqrt(2 * Math.PI))) * (Math.exp((-0.5) * Math.abs((x.doubleValue() - mi) / (double) sigma)))));
    }

    public static DataFunction triangle(double a, double b, double c) {
        return new DataFunction(input -> {
            double x = input.doubleValue();
            double val = Math.min(((x - a) / (b - a)), ((c - x) / (c - b)));
            return val >= 0 ? val : 0;
        });
    }

    public static DataFunction trapezoid(double a, double b, double c, double d) {
        return new DataFunction(input -> {
            double x = input.doubleValue();
            double val = Math.min((x - a) / (b - a), (d - x) / (d - c));
            val = val <= 1 ? val : 1;
            return val >= 0 ? val : 0;
        });
    }

    private Function<Number, Number> function;

    public DataFunction(Function<Number, Number> function) {
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
}
