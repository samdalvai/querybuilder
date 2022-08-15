package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.secondaryquery;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.BasicResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;

import java.util.List;
import java.util.stream.Collectors;

public class SecondaryQueryProcessor<E> extends BasicResultsProcessor {

    public SecondaryQueryProcessor() {
    }

    public SecondaryQueryProcessor(ResultsProcessor nextPostprocessor) {
        super(nextPostprocessor);
    }

    @Override
    public <E> List<E> resultsProcessing(List<E> list) {
        if (list.isEmpty())
            return list;

        return SecondaryQueryUtils.removeCopyElementsFromList(list);
    }

    public static <E> List fromListOfLists(List<List<E>> result) {
        return result.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

}
