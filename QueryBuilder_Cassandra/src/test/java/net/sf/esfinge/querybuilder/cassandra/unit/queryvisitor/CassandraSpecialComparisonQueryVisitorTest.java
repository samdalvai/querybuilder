package net.sf.esfinge.querybuilder.cassandra.unit.queryvisitor;

import net.sf.esfinge.querybuilder.cassandra.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.cassandra.exceptions.UnsupportedCassandraOperationException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CassandraSpecialComparisonQueryVisitorTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
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
    }
}
