package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing;

import java.util.ArrayList;
import java.util.List;

public class SecondaryQueryProcessor<E> implements ResultsProcessor {

    List<List<E>> resultsList = new ArrayList<>();

    public SecondaryQueryProcessor(List<E> ... result) {
        for (List<E> l : result)
            resultsList.add(l);
    }

    @Override
    public <E> List<E> postProcess(List<E> list) {
        return null;
    }
}
