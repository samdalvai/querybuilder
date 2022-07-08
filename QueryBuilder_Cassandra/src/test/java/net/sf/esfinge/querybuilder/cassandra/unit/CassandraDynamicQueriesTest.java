package net.sf.esfinge.querybuilder.cassandra.unit;

import net.sf.esfinge.querybuilder.cassandra.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.cassandra.exceptions.UnsupportedCassandraOperationException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CassandraDynamicQueriesTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
    public void notDynamicQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertFalse("Query should not be dynamic", qr.isDynamic());

        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT * FROM Person WHERE name = ?");
    }

    @Test
    public void ignoreWhenNullTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT * FROM Person");
    }

    @Test
    public void ignoreWhenNullFromVisitorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        String query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM Person");
    }

    @Test
    public void ignoreWhenNullFromVisitorWithTwoConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        String query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM Person WHERE age = ?");
    }

    @Test
    public void ignoreWhenNullFromVisitorWithTwoConditionsAndLastToBeIgnoredTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("age", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        String query = qr.getQuery().toString();

        assertEquals("SELECT * FROM Person WHERE age = ?", query);
    }

    @Test
    public void ignoreWhenNullFromVisitorWithComplexConditionsToBeIgnoredTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("age", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        String query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM Person WHERE age = ? AND name = ?");
    }

    @Test
    public void ignoreWhenNullQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", null);

        String query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM Person", query1);

        params.put("name", "James");

        String query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM Person WHERE name = 'James'", query2);
    }

    @Test
    public void ignoreWhenNullWithTwoConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("name", null);
        String query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM Person WHERE age = ?", query1);

        params.put("name", "James");
        String query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM Person WHERE name = 'James' AND age = ?", query2);
    }

    @Test
    public void ignoreWhenNullWithComplexConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitConector("OR");
        visitor.visitCondition("city", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("city", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("name", null);
        String query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM Person WHERE age = ? OR city = ?", query1);

        params.put("name", "James");
        params.put("age", 30);
        String query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM Person WHERE name = 'James' AND age = 30 OR city = ?", query2);
    }

    @Test(expected = UnsupportedCassandraOperationException.class)
    public void invalidCompareToNullQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.COMPARE_TO_NULL);
    }

	/*@Test
	public void twoCompareToNullQuery(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.COMPARE_TO_NULL);
		visitor.visitConector("and");
		visitor.visitCondition("lastName", ComparisonType.EQUALS, NullOption.COMPARE_TO_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		Map<String,Object> params = new HashMap<String, Object>();
		params.put("nameEquals", null);
		params.put("lastNameEquals", null);

		String query1 = qr.getQuery(params).toString();
		assertEquals(query1,"SELECT o FROM Person o WHERE o.name IS NULL and o.lastName IS NULL");

		params.put("nameEquals", "James");

		String query2 = qr.getQuery(params).toString();
		assertEquals(query2,"SELECT o FROM Person o WHERE o.name = :nameEquals and o.lastName IS NULL");

		params.put("lastNameEquals", "McLoud");

		String query3 = qr.getQuery(params).toString();
		assertEquals(query3,"SELECT o FROM Person o WHERE o.name = :nameEquals and o.lastName = :lastNameEquals");

		params.put("nameEquals", null);

		String query4 = qr.getQuery(params).toString();
		assertEquals(query4,"SELECT o FROM Person o WHERE o.name IS NULL and o.lastName = :lastNameEquals");
	}

	@Test
	public void twoIgnoreWhenNullQuery(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
		visitor.visitConector("and");
		visitor.visitCondition("lastName", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		Map<String,Object> params = new HashMap<String, Object>();
		params.put("nameEquals", null);
		params.put("lastNameEquals", null);

		String query1 = qr.getQuery(params).toString();
		assertEquals(query1,"SELECT o FROM Person o");

		params.put("nameEquals", "James");

		String query2 = qr.getQuery(params).toString();
		assertEquals(query2,"SELECT o FROM Person o WHERE o.name = :nameEquals");

		params.put("lastNameEquals", "McLoud");

		String query3 = qr.getQuery(params).toString();
		assertEquals(query3,"SELECT o FROM Person o WHERE o.name = :nameEquals and o.lastName = :lastNameEquals");

		params.put("nameEquals", null);

		String query4 = qr.getQuery(params).toString();
		assertEquals(query4,"SELECT o FROM Person o WHERE o.lastName = :lastNameEquals");
	}

	@Test
	public void twoIgnoreWhenNullQueryPlusOther(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
		visitor.visitConector("and");
		visitor.visitCondition("age", ComparisonType.GREATER_OR_EQUALS);
		visitor.visitConector("and");
		visitor.visitCondition("lastName", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		Map<String,Object> params = new HashMap<String, Object>();
		params.put("nameEquals", null);
		params.put("ageGreaterOrEquals", 18);
		params.put("lastNameEquals", null);

		String query1 = qr.getQuery(params).toString();
		assertEquals(query1,"SELECT o FROM Person o WHERE o.age >= :ageGreaterOrEquals");

		params.put("nameEquals", "James");

		String query2 = qr.getQuery(params).toString();
		assertEquals(query2,"SELECT o FROM Person o WHERE o.name = :nameEquals and o.age >= :ageGreaterOrEquals");

		params.put("lastNameEquals", "McLoud");

		String query3 = qr.getQuery(params).toString();
		assertEquals(query3,"SELECT o FROM Person o WHERE o.name = :nameEquals and o.age >= :ageGreaterOrEquals and o.lastName = :lastNameEquals");

		params.put("nameEquals", null);

		String query4 = qr.getQuery(params).toString();
		assertEquals(query4,"SELECT o FROM Person o WHERE o.age >= :ageGreaterOrEquals and o.lastName = :lastNameEquals");
	}*/

    @Test
    public void threeIgnoreWhenNullQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER_OR_EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("lastName", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", null);
        params.put("age", null);
        params.put("lastName", null);

        String query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM Person", query1);

        params.put("lastName", "McLoud");

        String query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM Person WHERE lastName = 'McLoud'", query2);

        params.put("age", 18);

        //String query3 = qr.getQuery(params).toString();
        //assertEquals("SELECT * FROM Person WHERE age >= 18 AND lastName = 'McLoud'", query3);

        params.put("name", "James");

        String query4 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM Person WHERE name = 'James' AND age >= 18 AND lastName = 'McLoud'", query4);
    }
}
