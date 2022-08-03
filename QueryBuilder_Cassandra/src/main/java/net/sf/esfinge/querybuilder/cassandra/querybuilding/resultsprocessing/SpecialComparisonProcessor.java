package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.specialcomparison.SpecialComparisonType;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.specialcomparison.SpecialComparisonUtils;
import net.sf.esfinge.querybuilder.cassandra.reflection.CassandraReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class SpecialComparisonProcessor extends BasicResultsProcessor {

    private List<SpecialComparisonClause> specialComparisonClauses;

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

        for (SpecialComparisonClause c : specialComparisonClauses){
            list = SpecialComparisonUtils.filterListBySpecialComparisonClause(list,c);
        }

        return list;
    }


}
