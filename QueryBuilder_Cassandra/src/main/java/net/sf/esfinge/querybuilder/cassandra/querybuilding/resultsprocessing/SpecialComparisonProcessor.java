package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonUtils;

import java.util.List;

public class SpecialComparisonProcessor extends BasicResultsProcessor {

    private final List<SpecialComparisonClause> specialComparisonClauses;

    public SpecialComparisonProcessor(List<SpecialComparisonClause> specialComparisonClauses) {
        super();
        this.specialComparisonClauses = specialComparisonClauses;
    }

    public SpecialComparisonProcessor(List<SpecialComparisonClause> specialComparisonClauses, ResultsProcessor nextPostprocessor) {
        super(nextPostprocessor);
        this.specialComparisonClauses = specialComparisonClauses;
    }

    @Override
    public <E> List<E> resultsProcessing(List<E> list) {
        if (specialComparisonClauses.isEmpty() || list.isEmpty())
            return list;

        list.forEach(e -> System.out.println(e));

        for (SpecialComparisonClause c : specialComparisonClauses) {
            System.out.println("passing: " + c);
            list = SpecialComparisonUtils.filterListBySpecialComparisonClause(list, c);

            System.out.println("result: ");
            list.forEach(e -> System.out.println(e));
        }

        return list;
    }


}
