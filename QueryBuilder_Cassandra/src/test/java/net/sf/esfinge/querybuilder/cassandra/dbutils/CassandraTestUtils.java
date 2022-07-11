package net.sf.esfinge.querybuilder.cassandra.dbutils;


import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.KeyspaceRepository;

public class CassandraTestUtils {

    public static void initDB() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();

        String query = "CREATE KEYSPACE IF NOT EXISTS test WITH replication = {'class':'SimpleStrategy','replication_factor':1};";
        session.execute(query);

        createTables(session);
    }

    public static void createTables(Session session) {
        String query = "CREATE TABLE IF NOT EXISTS test.person(id int PRIMARY KEY, name text,lastname text, age int);";

        session.execute(query);
    }

    public static void populateTables() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();

        String query = "BEGIN BATCH\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (1, 'Homer', 'Simpson', 48);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (2, 'Marge', 'Simpson', 45);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (3, 'Bart', 'Simpson', 10);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (4, 'Ned', 'Flanders', 50);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (5, 'Nelson', 'Muntz', 11);\n" +
                "        APPLY BATCH";

        session.execute(query);
    }

    public static void cleanTables() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();

        String query = "TRUNCATE test.person";

        session.execute(query);
    }


    public static void dropDB() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();
        KeyspaceRepository schemaRepository = new KeyspaceRepository(session);

        String query = "DROP KEYSPACE IF EXISTS test";

        session.execute(query);
    }
}