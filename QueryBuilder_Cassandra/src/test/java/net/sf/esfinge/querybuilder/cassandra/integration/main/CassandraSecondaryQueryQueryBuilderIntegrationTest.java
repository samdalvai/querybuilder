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

    /*
     String query = "BEGIN BATCH\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (1, 'Pedro', 'Silva', 20);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (2, 'Maria', 'Ferreira', 23);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (3, 'Marcos', 'Silva', 50);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (4, 'Antonio', 'Marques', 33);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (5, 'Silvia', 'Bressan', 11);\n" +
                "        APPLY BATCH";
     */

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

}
