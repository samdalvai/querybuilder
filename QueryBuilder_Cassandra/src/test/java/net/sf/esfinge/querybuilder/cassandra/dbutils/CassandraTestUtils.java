package net.sf.esfinge.querybuilder.cassandra.dbutils;


import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import net.sf.esfinge.querybuilder.cassandra.keyspace.KeyspaceRepository;
import net.sf.esfinge.querybuilder.cassandra.keyspace.ReplicationStrategy;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;

public class CassandraTestUtils {

    private String KEYSPACE_NAME = "test";
    private static final String TABLE_NAME = "person";

    public void initDB() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();
        KeyspaceRepository schemaRepository = new KeyspaceRepository(session);

        schemaRepository.createKeyspace(KEYSPACE_NAME, ReplicationStrategy.SimpleStrategy, 1);

        client.close();
    }

    public void clearDB() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();
        KeyspaceRepository schemaRepository = new KeyspaceRepository(session);

        schemaRepository.deleteKeyspace(KEYSPACE_NAME);
        client.close();
    }

    public void createTable() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();

        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(KEYSPACE_NAME + "." + TABLE_NAME)
                .append("(")
                .append("id int PRIMARY KEY, ")
                .append("name text,")
                .append("lastname text,")
                .append("age int);");

        final String query = sb.toString();

        session.execute(query);
        client.close();
    }

    public void insertPerson(Person person) {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();

        StringBuilder sb = new StringBuilder("INSERT INTO ")
                .append(KEYSPACE_NAME + "." + TABLE_NAME)
                .append("(id, name, lastname, age) ")
                .append("VALUES (")
                .append(person.getId())
                .append(", '")
                .append(person.getName()).append("', '")
                .append(person.getLastName()).append("', ")
                .append(person.getAge()).append(");");

        final String query = sb.toString();

        session.execute(query);
        client.close();
    }

    public void populatePerson() {
        createTable();

        Person person1 = new Person();
        person1.setId(1);
        person1.setName("Homer");
        person1.setLastName("Simpson");
        person1.setAge(48);

        Person person2 = new Person();
        person2.setId(2);
        person2.setName("Max");
        person2.setLastName("Power");
        person2.setAge(50);

        insertPerson(person1);
        insertPerson(person2);
    }
}
