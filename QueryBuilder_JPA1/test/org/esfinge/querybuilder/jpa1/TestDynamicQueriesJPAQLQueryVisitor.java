package org.esfinge.querybuilder.jpa1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.esfinge.querybuilder.methodparser.ComparisonType;
import org.esfinge.querybuilder.methodparser.QueryRepresentation;
import org.esfinge.querybuilder.methodparser.conditions.NullOption;
import org.junit.Ignore;
import org.junit.Test;

public class TestDynamicQueriesJPAQLQueryVisitor extends GenericTestJPAQLQueryVisitor {
	
	@Test
	public void notDynamicQuery(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.NONE);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		
		assertFalse("Query should not be dynamic", qr.isDynamic());
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o WHERE o.name = :nameEquals");
	}
	
	@Test
	public void ignoreWhenNullQuery(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("nameEquals", null);
		
		String query1 = qr.getQuery(params).toString();
		assertEquals(query1,"SELECT o FROM Person o");
		
		params.put("nameEquals", "James");
		
		String query2 = qr.getQuery(params).toString();
		assertEquals(query2,"SELECT o FROM Person o WHERE o.name = :nameEquals");
	}
	
	@Test
	public void compareToNullQuery(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.COMPARE_TO_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("nameEquals", null);
		
		String query1 = qr.getQuery(params).toString();
		assertEquals(query1,"SELECT o FROM Person o WHERE o.name IS NULL");
		
		params.put("nameEquals", "James");
		
		String query2 = qr.getQuery(params).toString();
		assertEquals(query2,"SELECT o FROM Person o WHERE o.name = :nameEquals");
	}
	
	@Test
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
	}
	
	@Test
	public void threeIgnoreWhenNullQuery(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
		visitor.visitConector("and");
		visitor.visitCondition("age", ComparisonType.GREATER_OR_EQUALS, NullOption.IGNORE_WHEN_NULL);
		visitor.visitConector("and");
		visitor.visitCondition("lastName", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("nameEquals", null);
		params.put("ageGreaterOrEquals", null);
		params.put("lastNameEquals", null);
		
		String query1 = qr.getQuery(params).toString();
		assertEquals(query1,"SELECT o FROM Person o");
		
		params.put("lastNameEquals", "McLoud");
		
		String query2 = qr.getQuery(params).toString();
		assertEquals(query2,"SELECT o FROM Person o WHERE o.lastName = :lastNameEquals");
		
		params.put("ageGreaterOrEquals", 18);
		
		String query3 = qr.getQuery(params).toString();
		assertEquals(query3,"SELECT o FROM Person o WHERE o.age >= :ageGreaterOrEquals and o.lastName = :lastNameEquals");
		
		params.put("nameEquals", "James");
		
		String query4 = qr.getQuery(params).toString();
		assertEquals(query4,"SELECT o FROM Person o WHERE o.name = :nameEquals and o.age >= :ageGreaterOrEquals and o.lastName = :lastNameEquals");
	}

}
