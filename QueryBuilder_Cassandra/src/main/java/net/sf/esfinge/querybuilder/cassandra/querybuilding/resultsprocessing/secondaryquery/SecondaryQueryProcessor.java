package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.secondaryquery;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;

import java.util.ArrayList;
import java.util.List;

public class SecondaryQueryProcessor<E> implements ResultsProcessor {

    List<E> resultsList = new ArrayList<>();

    public SecondaryQueryProcessor(List<E>... result) {
        for (List<E> l : result)
            resultsList.addAll(l);
    }

    @Override
    public <E> List<E> postProcess(List<E> list) {
        if (list.isEmpty())
            return list;

        List<E> mergedList = new ArrayList<>();

        // TODO: implement

        return mergedList;
    }

    private <E> boolean isObjectInList(E object, List<E> list) {
        for (E e : list) {
            if (SecondaryQueryUtils.reflectiveEquals(e, object))
                return true;
        }

        return false;
    }

}
