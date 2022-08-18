package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;

import java.util.List;

public interface CassandraJoinTestQuery extends Repository<Worker> {

    List<Worker> getWorkerByAddressCity(String city);

    List<Worker> getWorkerByAddressCityAndAddressState(String city, String state);

    List<Worker> getWorkerByLastNameAndAddressState(String lastname, String state);

    List<Worker> getWorkerByAddressStateAndLastName(String state, String lastname);

    List<Worker> getWorkerByAddressCityOrAddressState(String city, String state);

    List<Worker> getWorkerByAddressCityOrLastName(String city, String lastName);

    List<Worker> getWorkerByAddressCityOrLastNameOrderById(String city, String lastName);
}
