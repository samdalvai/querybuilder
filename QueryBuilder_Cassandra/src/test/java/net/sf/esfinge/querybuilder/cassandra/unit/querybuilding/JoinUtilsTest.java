package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.exceptions.GetterNotFoundInClassException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join.JoinClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join.JoinUtils;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JoinUtilsTest {

    @Test
    public void filterByJoinClauseEqualStringTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType("Hello", "Hello", JoinComparisonType.EQUALS));
    }

    @Test
    public void filterByJoinClauseEqualWithNotEqualStringTest() {
        assertFalse(JoinUtils.filterByJoinClauseComparisonType("Hello", "Hellos", JoinComparisonType.EQUALS));
    }

    @Test
    public void filterByJoinClauseGreaterOrEqualStringTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType(3, 2, JoinComparisonType.GREATER_OR_EQUALS));
    }

    @Test
    public void filterByJoinClauseLesserOrEqualStringTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType(2, 4, JoinComparisonType.LESSER_OR_EQUALS));
    }

    @Test
    public void filterByJoinClauseNotEqualStringTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType("Hello", "Hello to all", JoinComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterByJoinClauseNotEqualWithEqualStringTest() {
        assertFalse(JoinUtils.filterByJoinClauseComparisonType("Hello", "Hello", JoinComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterByJoinClauseNotEqualIntTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType(1, 2, JoinComparisonType.NOT_EQUALS));
    }

    @Test
    public void ffilterByJoinClauseNotEqualWithEqualIntTest() {
        assertFalse(JoinUtils.filterByJoinClauseComparisonType(1, 1, JoinComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterByJoinClauseStartingWithStringTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType("Hello", "He", JoinComparisonType.STARTS));
    }

    @Test
    public void filterByJoinClauseNotStartingWithStringTest() {
        assertFalse(JoinUtils.filterByJoinClauseComparisonType("Hello", "whatever", JoinComparisonType.STARTS));
    }

    @Test
    public void filterByJoinClauseEndingWithStringTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType("Hello", "lo", JoinComparisonType.ENDS));
    }

    @Test
    public void filterByJoinClauseNotEndingWithStringTest() {
        assertFalse(JoinUtils.filterByJoinClauseComparisonType("Hello", "whatever", JoinComparisonType.ENDS));
    }

    @Test
    public void filterByJoinClauseContainingStringTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType("Hello", "el", JoinComparisonType.CONTAINS));
    }

    @Test
    public void filterByJoinClauseNotContainingStringTest() {
        assertFalse(JoinUtils.filterByJoinClauseComparisonType("Hello", "whatever", JoinComparisonType.CONTAINS));
    }

    @Test
    public void filterByJoinClauseWithNotAvailableAttributeTest() {
        JoinClause clause = new JoinClause("whatever", "whatever", JoinComparisonType.NOT_EQUALS);
        clause.setValue("whatever");

        List<Person> list = new ArrayList<>();
        Person p = new Person();
        p.setId(1);
        p.setLastName("testlastname");
        p.setAge(33);
        list.add(p);

        assertThrows(GetterNotFoundInClassException.class, () -> JoinUtils.filterListByJoinClause(list, clause));
    }

    /*@Test
    public void hasCompareToNullAnnotationWithAnnotationPresentTest() {
        assertTrue(SpecialComparisonUtils.hasCompareToNullAnnotationOnFields(new CompareNullQueryObject()));
    }

    @Test
    public void hasCompareToNullAnnotationWithAnnotationNotPresentTest() {
        assertFalse(SpecialComparisonUtils.hasCompareToNullAnnotationOnFields(new Person()));
    }*/
}