package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.ordering.ChainComparator;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.ordering.ChainComparatorFactory;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.ordering.OrderByClause;

import java.util.List;
import java.util.stream.Collectors;

public class OrderingProcessor extends BasicResultsProcessor{

    private List<OrderByClause> orderByClauses;

    public OrderingProcessor(List<OrderByClause> orderByClauses) {
        super();
        this.orderByClauses = orderByClauses;
    }

    public OrderingProcessor(List<OrderByClause> orderByClauses, ResultsProcessor nextPostprocessor) {
        super(nextPostprocessor);
        this.orderByClauses = orderByClauses;
    }

    @Override
    public <E> List<E> resultsProcessing(List<E> list) {
        if (orderByClauses.isEmpty() || list.isEmpty())
            return list;

        Class clazz = list.get(0).getClass();

        ChainComparator comparator = ChainComparatorFactory.createChainComparator(clazz, orderByClauses);

        return list.stream().sorted(comparator).collect(Collectors.toList());
    }
}
