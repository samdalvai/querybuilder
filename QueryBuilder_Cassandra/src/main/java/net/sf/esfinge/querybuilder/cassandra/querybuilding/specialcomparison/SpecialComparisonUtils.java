package net.sf.esfinge.querybuilder.cassandra.querybuilding.specialcomparison;

import net.sf.esfinge.querybuilder.cassandra.reflection.CassandraReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class SpecialComparisonUtils {

    public static boolean filterBySpecialComparison(Object parameterValue, Object valueToCompare, SpecialComparisonType comparisonType) {
        switch (comparisonType) {
            case NOT_EQUALS:
                return !parameterValue.equals(valueToCompare);
            case STARTS:
                return parameterValue.toString().startsWith(valueToCompare.toString());
            case ENDS:
                return parameterValue.toString().endsWith(valueToCompare.toString());
            case CONTAINS:
                return parameterValue.toString().contains(valueToCompare.toString());
            default:
                return false;
        }
    }

    public static boolean filterBySpecialComparisonClause(Object parameterValue, SpecialComparisonClause clause) {
        return filterBySpecialComparison(parameterValue, clause.getValue(), clause.getSpecialComparisonType());
    }
    public static <E> List filterListBySpecialComparisonClause(List<E> list, SpecialComparisonClause clause){
        Class clazz = list.get(0).getClass();

        Method[] getters = CassandraReflectionUtils.getClassGetters(clazz);

        Method getter = CassandraReflectionUtils.getClassGetterForField(clazz,getters,clause.getPropertyName());

        return list.stream().filter(obj -> {
            try {
                return filterBySpecialComparisonClause(getter.invoke(obj),clause);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}
