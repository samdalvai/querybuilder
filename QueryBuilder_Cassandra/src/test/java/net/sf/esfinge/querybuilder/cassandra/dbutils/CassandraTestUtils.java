package net.sf.esfinge.querybuilder.cassandra.dbutils;


import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import net.sf.esfinge.querybuilder.cassandra.keyspace.KeyspaceRepository;
import net.sf.esfinge.querybuilder.cassandra.keyspace.ReplicationStrategy;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;

public class CassandraTestUtils {

    private String keyspaceName;
    private static final String TABLE_NAME = "person";

    private KeyspaceRepository schemaRepository;
    private Session session;


    public void initDB() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();

        this.session = client.getSession();
        this.keyspaceName = client.getKeyspaceName();

        schemaRepository = new KeyspaceRepository(session);

        schemaRepository.createKeyspace(keyspaceName, ReplicationStrategy.SimpleStrategy, 1);
    }

    public void clearDB() {
        schemaRepository.deleteKeyspace(keyspaceName);
    }

    public void createTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME)
                .append("(")
                .append("id uuid PRIMARY KEY, ")
                .append("name text,")
                .append("city text,")
                .append("age int);");

        final String query = sb.toString();

        session.execute(query);
    }

    public void insertPerson(Person person) {
        StringBuilder sb = new StringBuilder("INSERT INTO ")
                .append(TABLE_NAME)
                .append("(id, name, city, age) ")
                .append("VALUES (")
                .append(person.getId())
                .append(", '")
                .append(person.getName()).append("', '")
                .append(person.getLastName()).append("', ")
                .append(person.getAge()).append(");");

        final String query = sb.toString();

        session.execute(query);
    }

    public void populatePerson() {
        schemaRepository.useKeyspace(keyspaceName);

        createTable();

        Person person1 = new Person();
        person1.setId(UUIDs.timeBased());
        person1.setName("Homer");
        person1.setLastName("Simpson");
        person1.setAge(48);

        Person person2 = new Person();
        person2.setId(UUIDs.timeBased());
        person2.setName("Max");
        person2.setLastName("Power");
        person2.setAge(50);

        insertPerson(person1);
        insertPerson(person2);
    }
}
