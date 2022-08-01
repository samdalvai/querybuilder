package net.sf.esfinge.querybuilder.cassandra.unit.queryvisitor;

import net.sf.esfinge.querybuilder.cassandra.CassandraQueryRepresentation;
import net.sf.esfinge.querybuilder.cassandra.validation.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.specialcomparison.SpecialComparisonType;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CassandraSpecialComparisonQueryVisitorTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
    public void singleSpecialComparisonTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        visitor.visitEnd();

        SpecialComparisonClause expected = new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS);

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person");
        assertEquals(expected, ((CassandraQueryRepresentation) qr).getSpecialComparisonClauses().get(0));
    }

    @Test
    public void multipleSpecialComparisonTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.STARTS);
        visitor.visitEnd();

        SpecialComparisonClause expected1 = new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS);
        SpecialComparisonClause expected2 = new SpecialComparisonClause("age", SpecialComparisonType.STARTS);
        expected1.setNextConnector("AND");

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person");
        assertEquals(expected1, ((CassandraQueryRepresentation) qr).getSpecialComparisonClauses().get(0));
        assertEquals(expected2, ((CassandraQueryRepresentation) qr).getSpecialComparisonClauses().get(1));
    }

    /*@Test
    public void unsupportedCassandraComparisonTypeNotEqualsTest() {
        assertThrows(UnsupportedCassandraOperationException.class, () -> {
            visitor.visitEntity("Person");
            visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        });
    }

    @Test
    public void unsupportedCassandraComparisonTypeContainsTest() {
        assertThrows(UnsupportedCassandraOperationException.class, () -> {
            visitor.visitEntity("Person");
            visitor.visitCondition("name", ComparisonType.CONTAINS);
        });
    }

    @Test
    public void unsupportedCassandraComparisonTypeStartsTest() {
        assertThrows(UnsupportedCassandraOperationException.class, () -> {
            visitor.visitEntity("Person");
            visitor.visitCondition("name", ComparisonType.STARTS);
        });
    }

    @Test
    public void unsupportedCassandraComparisonTypeEndsTest() {
        assertThrows(UnsupportedCassandraOperationException.class, () -> {
            visitor.visitEntity("Person");
            visitor.visitCondition("name", ComparisonType.ENDS);
        });
    }*/
}
