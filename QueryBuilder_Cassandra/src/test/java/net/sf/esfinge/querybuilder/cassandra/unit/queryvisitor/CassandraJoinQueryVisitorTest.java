package net.sf.esfinge.querybuilder.cassandra.unit.queryvisitor;

import net.sf.esfinge.querybuilder.cassandra.CassandraQueryRepresentation;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering.OrderByClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType;
import net.sf.esfinge.querybuilder.cassandra.validation.CassandraValidationQueryVisitor;
import net.sf.esfinge.querybuilder.cassandra.validation.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class CassandraJoinQueryVisitorTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
    public void oneJoinConditionTest(){
        visitor.visitEntity("Worker");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Worker",
                query);

        qr = ((CassandraValidationQueryVisitor) visitor).getJoinVisitor().getQueryRepresentation();
        String joinQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Address WHERE state = 0? ALLOW FILTERING",
                joinQuery);
    }

    @Test
    public void twoJoinConditionsTest(){
        visitor.visitEntity("Worker");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("address.city", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getJoinVisitor().getQueryRepresentation();
        String joinQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Worker",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Address WHERE state = 0? AND city = 1? ALLOW FILTERING",
                joinQuery);
    }

    @Test
    public void oneConditionForMainEntityAndOneJoinTest(){
        visitor.visitEntity("Worker");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getJoinVisitor().getQueryRepresentation();
        String joinQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Worker WHERE name = 0? ALLOW FILTERING",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Address WHERE state = 1? ALLOW FILTERING",
                joinQuery);
    }

    @Test
    public void twoConditionsForMainEntityAndOneJoinTest(){
        visitor.visitEntity("Worker");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getJoinVisitor().getQueryRepresentation();
        String joinQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Worker WHERE name = 0? AND lastname = 1? ALLOW FILTERING",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Address WHERE state = 2? ALLOW FILTERING",
                joinQuery);
    }

    @Test
    public void oneJoinAndOneConditionForMainEntityTest(){
        visitor.visitEntity("Worker");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getJoinVisitor().getQueryRepresentation();
        String joinQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Worker WHERE name = 1? ALLOW FILTERING",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Address WHERE state = 0? ALLOW FILTERING",
                joinQuery);

    }

    @Test
    public void twoJoinsAndOneConditionForMainEntityTest(){
        visitor.visitEntity("Worker");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("address.city", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getJoinVisitor().getQueryRepresentation();
        String joinQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Worker WHERE name = 2? ALLOW FILTERING",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Address WHERE state = 0? AND city = 1? ALLOW FILTERING",
                joinQuery);

    }

    @Test
    public void oneJoinConditionAndOneOrderByClauseTest(){
        visitor.visitEntity("Worker");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        OrderByClause expected = new OrderByClause("age", OrderingDirection.ASC);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Worker",
                query);

        qr = ((CassandraValidationQueryVisitor) visitor).getJoinVisitor().getQueryRepresentation();
        String joinQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Address WHERE state = 0? ALLOW FILTERING",
                joinQuery);
        assertEquals(expected, ((CassandraQueryRepresentation) visitor.getQueryRepresentation()).getOrderByClauses().get(0));
    }

    @Test
    public void oneJoinConditionAndOneSpecialClauseTest(){
        visitor.visitEntity("Worker");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        SpecialComparisonClause expected = new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Worker",
                query);

        qr = ((CassandraValidationQueryVisitor) visitor).getJoinVisitor().getQueryRepresentation();
        String joinQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Address WHERE state = 0? ALLOW FILTERING",
                joinQuery);

        assertEquals(expected, ((CassandraQueryRepresentation) visitor.getQueryRepresentation()).getSpecialComparisonClauses().get(0));
    }

}
