package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Backup {

    /*@Test
    public void queryWithOneOrConnectorTest() {
        List<Person> list = testQuery.getPersonByNameOrLastName("Pedro", "Ferreira");

        assertEquals(2, list.size());
        assertEquals("Pedro", list.get(0).getName());
        assertEquals("Ferreira", list.get(1).getLastName());
    }

    @Test
    public void queryWithTwoOrConnectorsTest() {
        List<Person> list = testQuery.getPersonByNameOrLastNameOrAge("Pedro", "Ferreira", 50);

        assertEquals(3, list.size());
        assertEquals("Pedro", list.get(0).getName());
        assertEquals("Ferreira", list.get(1).getLastName());
        assertEquals(new Integer(50), list.get(2).getAge());
    }*/

    /*
    @Override
    public Object executeQuery(QueryInfo queryInfo, Object[] args) {
        CassandraEntityClassProvider provider = new CassandraEntityClassProvider();
        this.clazz = (Class<E>) provider.getEntityClass(queryInfo.getEntityName());
        CassandraUtils.checkValidClassConfiguration(clazz);

        QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();
        queryInfo.visit(visitor);

        QueryRepresentation qr = visitor.getQueryRepresentation();

        List<CassandraChainQueryVisitor> visitors = ((CassandraValidationQueryVisitor)visitor).getSecondaryVisitorsList();
        List<QueryRepresentation> qrList = visitors.stream().map(v -> v.getQueryRepresentation())
                .collect(Collectors.toList());

        List<String> queries = qrList.stream().map(representation -> getQuery(queryInfo,args,representation))
                .collect(Collectors.toList());

        List<SpecialComparisonClause> specialComparisonClauses = SecondaryQueryUtils.fromListOfLists(qrList.stream().map(representation -> ((CassandraQueryRepresentation) representation).getSpecialComparisonClauses())
                .collect(Collectors.toList()));

        List<SpecialComparisonClause> newSpc = SpecialComparisonUtils.getSpecialComparisonClausesWithValues(args, specialComparisonClauses);

        //String query = getQuery(queryInfo, args, qr);

        List<E> results = new ArrayList<>();

        for (String query : queries) {
            results.addAll(getQueryResults(query));
        }

        //List<E> results = getQueryResults(query);

        if (queryInfo.getQueryType() == QueryType.RETRIEVE_SINGLE) {
            if (results.size() > 1)
                //throw new WrongTypeOfExpectedResultException("The query " + query + " resulted in " + results.size() + " results instead of one or zero results");
                throw new WrongTypeOfExpectedResultException("The query " + queries.get(0) + " resulted in " + results.size() + " results instead of one or zero results");

            if (results.size() > 0)
                return results.get(0);
            else
                return null;
        }

        ResultsProcessor processor = new SecondaryQueryProcessor<E>(new SpecialComparisonProcessor(newSpc, ((CassandraQueryRepresentation) qr).getProcessor()));

        return processor.postProcess(results);
    }
     */
}
