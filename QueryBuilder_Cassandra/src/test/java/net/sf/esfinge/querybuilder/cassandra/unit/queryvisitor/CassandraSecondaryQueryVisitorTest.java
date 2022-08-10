package net.sf.esfinge.querybuilder.cassandra.unit.queryvisitor;

import net.sf.esfinge.querybuilder.cassandra.validation.CassandraValidationQueryVisitor;
import net.sf.esfinge.querybuilder.cassandra.validation.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CassandraSecondaryQueryVisitorTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
    public void oneOrConnectorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor)visitor).getSecondaryVisitor().getQueryRepresentation();
        String secondaryQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = ? ALLOW FILTERING",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE lastname = ? ALLOW FILTERING",
                secondaryQuery);
    }

    @Test
    public void complexOrConnectorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor)visitor).getSecondaryVisitor().getQueryRepresentation();
        String secondaryQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = ? AND lastname = ? ALLOW FILTERING",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE lastname = ? AND name = ? ALLOW FILTERING",
                secondaryQuery);
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorBeforeConditionTest() {
        visitor.visitEntity("Person");
        visitor.visitConector("or");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorAsFirstVisitTest() {
        visitor.visitConector("or");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorAsLastVisitTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitEnd();
    }
}
