package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join;

import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MethodInvocationException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.reflection.CassandraReflectionUtils;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JoinUtils {

    public static boolean filterByJoinClauseComparisonType(Object objectAttributeValue, Object queryParameterValue, ComparisonType comparisonType) {
        switch (comparisonType) {
            case EQUALS:
                return objectAttributeValue.equals(queryParameterValue);
            case GREATER_OR_EQUALS:
                return objectAttributeValue.equals(queryParameterValue) || (double) objectAttributeValue >= (double) queryParameterValue;
            case LESSER_OR_EQUALS:
                return objectAttributeValue.equals(queryParameterValue) || (double) objectAttributeValue <= (double) queryParameterValue;
            case GREATER:
                return (double) objectAttributeValue > (double) queryParameterValue;
            case LESSER:
                return (double) objectAttributeValue < (double) queryParameterValue;
            case NOT_EQUALS:
                return !objectAttributeValue.equals(queryParameterValue);
            case STARTS:
                return objectAttributeValue.toString().startsWith(queryParameterValue.toString());
            case ENDS:
                return objectAttributeValue.toString().endsWith(queryParameterValue.toString());
            case CONTAINS:
                return objectAttributeValue.toString().contains(queryParameterValue.toString());
            default:
                return true;
        }
    }

    public static boolean filterByJoinClause(Object parameterValue, JoinClause joinClause) {
        return filterByJoinClauseComparisonType(parameterValue, joinClause.getValue(), joinClause.getComparisonType());
    }

    public static <E> List filterListByJoinClause(List<E> list, JoinClause joinClause) {
        if (list.size() == 0)
            return list;

        Class clazz = list.get(0).getClass();

        Method[] getters = CassandraReflectionUtils.getClassGetters(clazz);

        Method getter = CassandraReflectionUtils.getClassGetterForField(clazz, getters, joinClause.getPropertyName());

        return list.stream().filter(obj -> {
            try {
                // If we have the @CompareToNull annotation on the parameter of the query method
                // but we pass a non null value to the method, then we should skip attributes
                // in the results which are not null
                if (getter.invoke(obj) == null && joinClause.getValue() != null) {
                    return false;
                } else {
                    return filterByJoinClause(getter.invoke(obj), joinClause);
                }
            } catch (Exception e) {
                throw new MethodInvocationException("Could not invoke method \"" + getter.getName() + "\" on object \"" + obj + "\", this is caused by: " + e);
            }
        }).collect(Collectors.toList());
    }

    public static List<SpecialComparisonClause> getSpecialComparisonClausesWithValues(Object[] args, List<SpecialComparisonClause> spc) {
        List<SpecialComparisonClause> newSpc = new ArrayList<>();

        for (SpecialComparisonClause c : spc) {
            c.setValue(args[c.getArgPosition()]);
            newSpc.add(c);
        }

        return newSpc;
    }

    public static boolean hasCompareToNullAnnotationOnFields(Object obj) {
        if (obj != null) {
            for (Field f : obj.getClass().getDeclaredFields()) {
                if (f.isAnnotationPresent(CompareToNull.class))
                    return true;
            }
        }

        return false;
    }

}
