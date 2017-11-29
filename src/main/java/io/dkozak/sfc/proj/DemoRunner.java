package io.dkozak.sfc.proj;

import io.dkozak.sfc.proj.components.chartline.ChartLinePresenter;
import io.dkozak.sfc.proj.components.chartline.ChartLineView;
import io.dkozak.sfc.proj.components.result.ResultPresenter;
import io.dkozak.sfc.proj.fuzzy.FuzzySet;
import io.dkozak.sfc.proj.fuzzy.MemberFunction;
import io.dkozak.sfc.proj.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DemoRunner {

    private ResultPresenter resultPresenter;

    private Random random = new Random();

    private List<FuzzySet> sets = new ArrayList<>();

    public DemoRunner(ResultPresenter resultPresenter) {
        this.resultPresenter = resultPresenter;
        generateRandomSets();
    }

    private void generateRandomSets() {
        sets.add(new FuzzySet(MemberFunction.gaussian(50, 10)));
        sets.add(new FuzzySet(MemberFunction.gaussian(30, 15)));
        sets.add(new FuzzySet(MemberFunction.trapezoid(10, 20, 30, 40)));
        sets.add(new FuzzySet(MemberFunction.triangle(20, 40, 60)));
        sets.add(new FuzzySet(MemberFunction.constant(15)));
        sets.add(new FuzzySet(MemberFunction.triangle(5, 15, 20)));
    }

    private FuzzySet randomSet() {
        return sets.get(random.nextInt(sets.size()));
    }


    public void run(List<ChartLineView> chartViews) {
        for (ChartLineView view : chartViews) {
            ChartLinePresenter presenter = (ChartLinePresenter) view.getPresenter();
            Map<String, FuzzySet> sets = presenter.getSets();
            sets.clear();

            FuzzySet set = randomSet();
            sets.put("Antecedent 1", set);
            Utils.showSetInAChart(set.setName("Antecedent 1"), presenter.getChartLeft(), 0, 100);

            set = randomSet();
            sets.put("Fact 1", set);
            Utils.showSetInAChart(set.setName("Fact 1"), presenter.getChartLeft(), 0, 100);

            set = randomSet();
            sets.put("Antecedent 2", set);
            Utils.showSetInAChart(set.setName("Antecedent 2"), presenter.getChartMiddle(), 0, 100);

            set = randomSet();
            sets.put("Fact 2", set);
            Utils.showSetInAChart(set.setName("Fact 2"), presenter.getChartMiddle(), 0, 100);

            set = randomSet();
            sets.put("Consequent", set);
            Utils.showSetInAChart(set.setName("Consequent"), presenter.getChartRight(), 0, 100);

        }
        resultPresenter.onCompute(null);
    }
}
