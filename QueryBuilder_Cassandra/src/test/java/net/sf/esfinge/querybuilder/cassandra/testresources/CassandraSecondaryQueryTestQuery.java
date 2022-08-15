package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Greater;

import java.util.List;

public interface CassandraSecondaryQueryTestQuery extends Repository<Person> {

    /**
     * QUERIES WITH OR CONNECTOR
     **/

    List<Person> getPersonByNameOrLastName(String name, String lastname);

    List<Person> getPersonByNameOrLastNameOrAge(String name, String lastname, Integer age);

    List<Person> getPersonByIdOrNameOrLastNameOrAge(Integer id, String name, String lastname, Integer age);

    List<Person> getPersonByNameOrLastNameAndAge(String name, String lastname, Integer age);

    List<Person> getPersonByNameAndLastNameOrAge(String name, String lastname, Integer age);

    List<Person> getPersonByAgeOrLastNameOrderByNameDesc(@Greater Integer age, String lastname);
}
