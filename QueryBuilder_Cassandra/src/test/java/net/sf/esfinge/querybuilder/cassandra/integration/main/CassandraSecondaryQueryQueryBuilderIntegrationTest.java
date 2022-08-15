package net.sf.esfinge.querybuilder.cassandra.integration.main;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraSecondaryQueryTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraSecondaryQueryQueryBuilderIntegrationTest extends CassandraBasicDatabaseIntegrationTest {

    CassandraSecondaryQueryTestQuery testQuery = QueryBuilder.create(CassandraSecondaryQueryTestQuery.class);

    @Test
    public void queryWithOneOrConnectorTest() {
        List<Person> list = testQuery.getPersonByNameOrLastName("Pedro", "Ferreira");

        assertEquals(2, list.size());
        assertEquals("Pedro", list.get(0).getName());
        assertEquals("Ferreira", list.get(1).getLastName());
    }

    @Test
    public void queryWithTwoOrConnectorsTest() {
        List<Person> list = testQuery.getPersonByNameOrLastNameOrAge("Pedro", "Ferreira", 50);

        assertEquals(3, list.size());
        assertEquals("Pedro", list.get(0).getName());
        assertEquals("Ferreira", list.get(1).getLastName());
        assertEquals(new Integer(50), list.get(2).getAge());
    }

    @Test
    public void queryWithTwoOrConnectorsAndDuplicateResultsTest() {
        List<Person> list = testQuery.getPersonByNameOrLastName("Pedro", "Silva");

        assertEquals(2, list.size());
        assertEquals("Pedro", list.get(0).getName());
        assertEquals("Marcos", list.get(1).getName());
    }

}
