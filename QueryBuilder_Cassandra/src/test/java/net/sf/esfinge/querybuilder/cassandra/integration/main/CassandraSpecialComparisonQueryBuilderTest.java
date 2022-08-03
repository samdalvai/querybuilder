package net.sf.esfinge.querybuilder.cassandra.integration.main;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseTest;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraSpecialComparisonTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CassandraSpecialComparisonQueryBuilderTest extends CassandraBasicDatabaseTest {

    CassandraSpecialComparisonTestQuery testQuery = QueryBuilder.create(CassandraSpecialComparisonTestQuery.class);


    @Test
    public void queryWithNotEquals() {
        List<Person> list = testQuery.getPersonByLastNameNotEquals("Whatever");

        assertTrue(list.size() == 5);
    }

    @Test
    public void queryWithNotEquals2() {
        List<Person> list = testQuery.getPersonByNameStartsAndAgeGreater("Whatever",30);

        assertTrue(list.size() == 5);
    }

    @Test
    public void queryWithNotEquals3() {
        List<Person> list = testQuery.getPersonByNameStartsAndAgeGreaterAndLastNameNotEquals("Whatever",30, "Hello");

        assertTrue(list.size() == 5);
    }



}
