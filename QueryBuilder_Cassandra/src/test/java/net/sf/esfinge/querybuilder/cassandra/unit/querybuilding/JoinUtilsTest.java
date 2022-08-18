package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.exceptions.GetterNotFoundInClassException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join.JoinClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join.JoinUtils;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JoinUtilsTest {

    @Test
    public void filterBySpecialComparisonNotEqualStringTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType("Hello", "Hello to all", ComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualWithEqualStringTest() {
        assertFalse(JoinUtils.filterByJoinClauseComparisonType("Hello", "Hello", ComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualIntTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType(1, 2, ComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualWithEqualIntTest() {
        assertFalse(JoinUtils.filterByJoinClauseComparisonType(1, 1, ComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonStartingWithStringTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType("Hello", "He", ComparisonType.STARTS));
    }

    @Test
    public void filterBySpecialComparisonNotStartingWithStringTest() {
        assertFalse(JoinUtils.filterByJoinClauseComparisonType("Hello", "whatever", ComparisonType.STARTS));
    }

    @Test
    public void filterBySpecialComparisonEndingWithStringTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType("Hello", "lo", ComparisonType.ENDS));
    }

    @Test
    public void filterBySpecialComparisonNotEndingWithStringTest() {
        assertFalse(JoinUtils.filterByJoinClauseComparisonType("Hello", "whatever", ComparisonType.ENDS));
    }

    @Test
    public void filterBySpecialComparisonContainingStringTest() {
        assertTrue(JoinUtils.filterByJoinClauseComparisonType("Hello", "el", ComparisonType.CONTAINS));
    }

    @Test
    public void filterBySpecialComparisonNotContainingStringTest() {
        assertFalse(JoinUtils.filterByJoinClauseComparisonType("Hello", "whatever", ComparisonType.CONTAINS));
    }

    @Test
    public void filterBySpecialComparisonWithNotAvailableAttributeTest() {
        JoinClause clause = new JoinClause("whatever", "whatever", ComparisonType.NOT_EQUALS);
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