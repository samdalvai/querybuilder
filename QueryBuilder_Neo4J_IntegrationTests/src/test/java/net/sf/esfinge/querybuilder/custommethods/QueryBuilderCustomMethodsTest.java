package net.sf.esfinge.querybuilder.custommethods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.neo4j.domain.Address;
import net.sf.esfinge.querybuilder.neo4j.domain.Person;

public class QueryBuilderCustomMethodsTest {
	
	@Test
	public void simpleCustomMethod(){
		SimpleInterface si = QueryBuilder.create(SimpleInterface.class);
		
		CustomMethodImpl.methodInvoked = false;
		
		si.customMethod();
		
		assertTrue("The custom method should be invoked", CustomMethodImpl.methodInvoked);
	}
	
	
	@Test
	public void genericCustomInterface(){
		GenericInterface gi = QueryBuilder.create(GenericInterface.class);
		
		Class<?> c = gi.getConfigClass();
		
		assertEquals("The generic parameter should be passed for configuration", c, Person.class);
	}
	
	@Test
	public void twoGenericCustomInterface(){
		GenericInterface gi = QueryBuilder.create(GenericInterface.class);
		OtherGenericInterface ogi = QueryBuilder.create(OtherGenericInterface.class);
		
		Class<?> c1 = gi.getConfigClass();
		Class<?> c2 = ogi.getConfigClass();
		
		assertEquals("The generic parameter should be Person", c1, Person.class);
		assertEquals("The generic parameter should be Address", c2, Address.class);
	}
	
	

}
