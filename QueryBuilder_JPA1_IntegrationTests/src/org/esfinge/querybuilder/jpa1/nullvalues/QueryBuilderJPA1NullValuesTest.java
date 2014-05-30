package org.esfinge.querybuilder.jpa1.nullvalues;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.esfinge.querybuilder.QueryBuilder;
import org.esfinge.querybuilder.jpa1.domainterms.TestDomainQuery;
import org.esfinge.querybuilder.jpa1.testresources.Person;
import org.esfinge.querybuilder.jpa1.testresources.QueryBuilderDatabaseTest;
import org.junit.Before;
import org.junit.Test;

public class QueryBuilderJPA1NullValuesTest extends QueryBuilderDatabaseTest {
	
	private TestNullValueQueries tq;
	
	@Before
	public void setupDatabase() throws Exception {
		initializeDatabase("/initial_db_nullvalues.xml");
		tq = QueryBuilder.create(TestNullValueQueries.class);
	}
	
	@Test
	public void compareToNullQuery(){
		List<Person> list = tq.getPersonByName(null);
		assertEquals("The list should have 1 person", 1, list.size());
		assertEquals("The list should have id = 4", new Integer(4), list.get(0).getId());
		
		list = tq.getPersonByName("Silvia");
		assertEquals("The list should have 1 person", 1, list.size());
		assertEquals("The list should have id = 5", new Integer(5), list.get(0).getId());
	}
	
	@Test
	public void compareToNullQueryWithOtherParams(){
		List<Person> list = tq.getPersonByNameAndLastName("M", null);
		assertEquals("The list should have 1 person", 1, list.size());
		assertEquals("The list should have id = 2", new Integer(2), list.get(0).getId());
	}
	
	@Test
	public void ignoreWhenNull(){
		List<Person> list = tq.getPersonByAgeGreater(null);
		assertEquals("The list should have 5 persons", 5, list.size());
		
		list = tq.getPersonByAgeGreater(18);
		assertEquals("The list should have 2 persons", 2, list.size());
	}
	
	@Test
	public void ignoreWhenNullWithTwoParams(){
		List<Person> list = tq.getPersonByNameStartsAndLastNameStarts("M", null);
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The list should have id = 2", new Integer(2), list.get(0).getId());
		assertEquals("The list should have id = 3", new Integer(3), list.get(1).getId());
		
		list = tq.getPersonByNameStartsAndLastNameStarts(null, "S");
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The list should have id = 1", new Integer(1), list.get(0).getId());
		assertEquals("The list should have id = 3", new Integer(3), list.get(1).getId());
		
		list = tq.getPersonByNameStartsAndLastNameStarts("M", "S");
		assertEquals("The list should have 1 person", 1, list.size());
		assertEquals("The list should have id = 3", new Integer(3), list.get(0).getId());
	}

}