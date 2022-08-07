package net.sf.esfinge.querybuilder.cassandra.integration.main;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.exceptions.OrderingLimitExceededException;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.OrderingProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering.OrderByClause;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraOrderByTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestClass;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CassandraOrderByQueryBuilderIntegrationTest extends CassandraBasicDatabaseIntegrationTest {

    CassandraOrderByTestQuery testQuery = QueryBuilder.create(CassandraOrderByTestQuery.class);

    @Test
    public void orderByQueryWithOneFieldTest() {
        List<Person> list = testQuery.getPersonOrderByName();

        String[] actualNames = list.stream().map(Person::getName).toArray(String[]::new);
        String[] expectedNames = {"Antonio", "Marcos", "Maria", "Pedro", "Silvia"};

        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void orderByQueryWithOneFieldAndParameterDescendentTest() {
        List<Person> list = testQuery.getPersonByAgeOrderByNameDesc(21);

        String[] actualNames = list.stream().map(Person::getName).toArray(String[]::new);
        String[] expectedNames = {"Maria", "Marcos", "Antonio"};

        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void orderByQueryWithTwoFieldsTest() {
        List<Person> list = testQuery.getPersonOrderByLastNameAndName();

        String[] actualNames = list.stream().map(Person::getName).toArray(String[]::new);
        String[] expectedNames = {"Silvia", "Maria", "Antonio", "Marcos", "Pedro"};

        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void orderByQueryWithTwoFieldsWithOrderingTest() {
        List<Person> list = testQuery.getPersonOrderByLastNameDescAndNameAsc();

        String[] actualNames = list.stream().map(Person::getName).toArray(String[]::new);
        String[] expectedNames = {"Marcos", "Pedro", "Antonio", "Maria", "Silvia"};

        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void complexOrderByQueryTest() {
        List<Person> list = testQuery.getPersonByAgeAndLastNameOrderByAgeAndLastNameDescAndName(51, "Silva");

        String[] actualNames = list.stream().map(Person::getName).toArray(String[]::new);
        String[] expectedNames = {"Pedro", "Marcos"};

        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void orderByQueryWithNoResultsTest() {
        List<Person> list = testQuery.getPersonByNameOrderByNameDesc("non existent");

        assertTrue(list.isEmpty());
    }

    @Test
    public void orderByQueryWithOrderingLimitExceededTest() {
        for (int i = 0; i < 11; i++){
            Person p = new Person();
            p.setId(i);
            p.setName("test");
            p.setLastName("test");
            p.setAge(30);

            testQuery.save(p);
        }

        assertThrows(OrderingLimitExceededException.class, () -> testQuery.getPersonOrderByName());
    }
}
