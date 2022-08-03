package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding.resultsprocessing;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.SpecialComparisonProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.specialcomparison.SpecialComparisonType;
import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpecialComparisonProcessorTest {

    private List<TestClass> objectList;

    @Before
    public void init() {
        objectList = new ArrayList<>();

        TestClass obj1 = new TestClass(1, "Pedro", "Silva");
        TestClass obj2 = new TestClass(2, "Marcos", "Ferreira");
        TestClass obj3 = new TestClass(3, "Antonio", "Marques");
        TestClass obj4 = new TestClass(4, "Marcos", "Silva");
        TestClass obj5 = new TestClass(5, "Silvia", "Bressan");

        objectList.add(obj1);
        objectList.add(obj2);
        objectList.add(obj3);
        objectList.add(obj4);
        objectList.add(obj5);
    }

    @Test
    public void filterListNotEqualWithOneParameterNotEqual(){
        List<SpecialComparisonClause> specialComparisonClauses = new ArrayList<>();
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", SpecialComparisonType.NOT_EQUALS));
        specialComparisonClauses.get(0).setValue("Silva");

        ResultsProcessor processor = new SpecialComparisonProcessor(specialComparisonClauses);

        List<TestClass> expected = new ArrayList<>();
        TestClass exp1 = new TestClass(2, "Marcos", "Ferreira");
        TestClass exp2 = new TestClass(3, "Antonio", "Marques");
        TestClass exp3 = new TestClass(5, "Silvia", "Bressan");
        expected.add(exp1);
        expected.add(exp2);
        expected.add(exp3);

        assertEquals(expected,processor.postProcess(objectList));
    }


}