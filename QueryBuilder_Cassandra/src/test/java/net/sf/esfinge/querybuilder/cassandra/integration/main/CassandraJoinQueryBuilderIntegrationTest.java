package net.sf.esfinge.querybuilder.cassandra.integration.main;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraJoinTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Worker;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraJoinQueryBuilderIntegrationTest extends CassandraBasicDatabaseIntegrationTest {

    CassandraJoinTestQuery testQuery = QueryBuilder.create(CassandraJoinTestQuery.class);


    @Test
    public void queryWithOneParameterForJoinTest() {
        List<Worker> list = testQuery.getWorkerByAddressCity("Bolzano");
    }
}
