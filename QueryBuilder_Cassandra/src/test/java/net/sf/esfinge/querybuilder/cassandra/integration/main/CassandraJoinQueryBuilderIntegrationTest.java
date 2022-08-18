package net.sf.esfinge.querybuilder.cassandra.integration.main;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseWorkerIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraJoinTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Worker;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class CassandraJoinQueryBuilderIntegrationTest {

    @BeforeClass
    public static void initDB() throws TTransportException, IOException, InterruptedException {
        CassandraTestUtils.initDB();
        CassandraTestUtils.createTablesWorker();
    }


    // TODO: DROPPING THE DB IN THE INTEGRATION TESTS CAN LEAD TO UNEXPECTED ERRORS
    /*@AfterClass
    public static void dropDB() {
        CassandraTestUtils.dropDB();
    }*/

    @Before
    public void populateTables() {
        CassandraTestUtils.populateTablesWorker();
    }

    @After
    public void cleanTables() {
        CassandraTestUtils.cleanTablesWorker();
    }

    CassandraJoinTestQuery testQuery = QueryBuilder.create(CassandraJoinTestQuery.class);

    // TODO: JOIN QUERY INTEGRATION TESTS
    @Test
    public void queryWithOneParameterForJoinTest() {
        List<Worker> list = testQuery.getWorkerByAddressCity("Juiz de Fora");

        list.forEach(System.out::println);
    }

    /*
    @Test
	public void queryWithOtherTable(){
		List<Person> list = tq.getPersonByAddressCity("Juiz de Fora");
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The first should be Antonio", "Antonio", list.get(0).getName());
		assertEquals("The second should be Silvia", "Silvia", list.get(1).getName());
	}

	@Test
	public void compositeQueryWithOtherTable(){
		List<Person> list = tq.getPersonByLastNameAndAddressState("Silva","SP");
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The first should be Pedro", "Pedro", list.get(0).getName());
		assertEquals("Marcos", list.get(1).getName());
	}
     */
}
