package net.sf.esfinge.querybuilder.cassandra.unit.queryvisitor;

import net.sf.esfinge.querybuilder.cassandra.validation.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;

public class CassandraJoinQueryVisitorTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    // TODO: // TODO: JOIN QUERY VISITOR TESTS
    // TODO: PROBLEM WITH COMPLEX QUERIES AND CASSANDRA: JOINS DO NOT EXIST, IMPLEMENT THEM AT APPLICATION LEVEL??

    /*@Test
    public void mixedWithfixParameterQueryFromOtherClass(){
        visitor.visitEntity("Person");
        visitor.visitCondition("address.state", ComparisonType.EQUALS, "SP");
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();

        String query = qr.getQuery().toString();
        assertEquals(query,"SELECT o FROM <#keyspace-name#>.Person o WHERE o.address.state = :addressStateEquals and o.age > :ageGreater");
        assertEquals(qr.getFixParameterValue("addressStateEquals"), "SP");
        assertTrue(qr.getFixParameters().contains("addressStateEquals"));
    }*/


    /*@Test
    public void orderByWithConditions(){
        visitor.visitEntity("Person");
        visitor.visitCondition("address.state", ComparisonType.EQUALS, "SP");
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER);
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitOrderBy("name", OrderingDirection.DESC);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();

        String query = qr.getQuery().toString();
        assertEquals(query,"SELECT o FROM <#keyspace-name#>.Person o WHERE o.address.state = :addressStateEquals and o.age > :ageGreater ORDER BY o.age ASC, o.name DESC");
    }*/

    /*
    @Test
	public void compositeCondition(){
		visitor.visitEntity("Person");
		visitor.visitCondition("address.city", ComparisonType.EQUALS);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM <#keyspace-name#>.Person o WHERE o.address.city = :addressCityEquals");
	}

	@Test
	public void complexQuery(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS);
		visitor.visitConector("or");
		visitor.visitCondition("lastName", ComparisonType.EQUALS);
		visitor.visitConector("and");
		visitor.visitCondition("address.city", ComparisonType.EQUALS);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM <#keyspace-name#>.Person o WHERE o.name = :nameEquals or o.lastName = :lastNameEquals and o.address.city = :addressCityEquals");
	}*/

}
