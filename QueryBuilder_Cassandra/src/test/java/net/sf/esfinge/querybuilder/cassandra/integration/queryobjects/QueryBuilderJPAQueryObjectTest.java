package net.sf.esfinge.querybuilder.cassandra.integration.queryobjects;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseTest;
import net.sf.esfinge.querybuilder.cassandra.integration.domainterms.CassandraTestDomainQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.assertEquals;

public class QueryBuilderJPAQueryObjectTest extends CassandraBasicDatabaseTest {
	
	private TestQueryObject testQuery = QueryBuilder.create(TestQueryObject.class);

	@Test
	public void simpleQueryObjectTest(){
		SimpleQueryObject qo = new SimpleQueryObject();
		qo.setLastName("Silva");
		qo.setAge(20);
		Person p = testQuery.getPerson(qo);

		assertEquals("Pedro",p.getName());
	}
	
	
	@Test
	public void queryObjectWithComparisonTypeTest(){
		ComparisonTypeQueryObject qo = new ComparisonTypeQueryObject();
		qo.setAgeGreater(18);
		qo.setName("Antonio");
		qo.setLastName("Marques");
		List<Person> list = testQuery.getPerson(qo);
		Person p = list.get(0);

		assertEquals(1,list.size());
		assertEquals("Antonio",p.getName());
	}

	/*@Test
	public void queryObjectWithDomainTerm(){
		ComparisonTypeQueryObject qo = new ComparisonTypeQueryObject();
		qo.setAgeGreater(1);
		qo.setAgeLesser(100);
		qo.setName("a");
		qo.setLastName("e");
		List<Person> list = testQuery.getPersonSilvaFamily(qo);
		Person p = list.get(0);
		assertEquals(1,list.size());
		assertEquals(new Integer(2),p.getId());
	}

	@Test
	public void queryObjectWithNullComparison(){
		CompareNullQueryObject qo = new CompareNullQueryObject();
		qo.setName("M");
		List<Person> list = testQuery.getPerson(qo);
		assertEquals(1,list.size());
		Person p = list.get(0);
		assertEquals(new Integer(3),p.getId());
	}

	@Test
	public void queryObjectIgnoreNull(){
		CompareNullQueryObject qo = new CompareNullQueryObject();
		qo.setLastName("B");
		List<Person> list = testQuery.getPerson(qo);
		assertEquals(1,list.size());
		Person p = list.get(0);
		assertEquals(new Integer(5),p.getId());
	}

	@Test
	public void queryObjectWithOrderBy(){
		ComparisonTypeQueryObject qo = new ComparisonTypeQueryObject();
		qo.setAgeGreater(1);
		qo.setAgeLesser(100);
		qo.setName("a");
		qo.setLastName("e");

		List<Person> list = testQuery.getPersonOrderByNameAsc(qo);
		assertEquals(new Integer(2),list.get(0).getId());
		assertEquals(new Integer(5),list.get(1).getId());

		list = testQuery.getPersonOrderByNameDesc(qo);
		assertEquals(new Integer(5),list.get(0).getId());
		assertEquals(new Integer(2),list.get(1).getId());
	}*/
	

}
