package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding.resultsprocessing;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.SecondaryQueryProcessor;
import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecondaryQueryProcessorTest extends BasicProcessorTest {

    @Test
    public void mergeTwoSecondaryQueriesWithDifferentElementsTest() {
        List<TestClass> secondList = new ArrayList<>();
        TestClass obj1 = new TestClass(1, "Pedro", "Silva");
        secondList.add(obj1);

        List<TestClass> expectedList = new ArrayList<>();
        expectedList.addAll(objectList);
        expectedList.addAll(secondList);

        ResultsProcessor processor = new SecondaryQueryProcessor(objectList,secondList);
        List<TestClass> result = processor.postProcess(objectList);

        assertEquals(expectedList, result);
    }


}
