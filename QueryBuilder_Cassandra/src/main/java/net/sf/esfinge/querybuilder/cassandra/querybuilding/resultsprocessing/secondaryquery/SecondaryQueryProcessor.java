package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.secondaryquery;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.BasicResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;

import java.util.ArrayList;
import java.util.List;

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

    public static <E> List fromVariableNumberOfLists(List<E>... result) {
        List<E> resultsList = new ArrayList<>();

        for (List<E> l : result)
            resultsList.addAll(l);

        return resultsList;
    }

}
