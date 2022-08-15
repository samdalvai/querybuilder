package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;

import java.util.List;

public interface CassandraJoinTestQuery extends Repository<Worker> {

    // TODO: METHODS WITH CUSTOM CLASS ATTRIBUTE DON'T WORK IN CASSANDRA, BECAUSE THERE IS NO SUCH THIS AS JOINS, IMPLEMENT AT APPLICATION LOGIC OR LEAVE IT FORBIDDEN?
    List<Worker> getWorkerByAddressCity(String city);

    List<Worker> getWorkerByLastNameAndAddressState(String lastname, String state);
}
