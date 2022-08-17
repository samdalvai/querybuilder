package net.sf.esfinge.querybuilder.cassandra.integration.main;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseWorkerIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraJoinTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Worker;
import org.junit.Test;

import java.util.List;

public class CassandraJoinQueryBuilderIntegrationTest extends CassandraBasicDatabaseWorkerIntegrationTest {

    CassandraJoinTestQuery testQuery = QueryBuilder.create(CassandraJoinTestQuery.class);

    // TODO: JOIN QUERY INTEGRATION TESTS
    @Test
    public void queryWithOneParameterForJoinTest() {
        List<Worker> list = testQuery.getWorkerByAddressCity("Bolzano");
    }
}
