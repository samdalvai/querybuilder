package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison;

import net.sf.esfinge.querybuilder.cassandra.exceptions.MethodInvocationException;
import net.sf.esfinge.querybuilder.cassandra.reflection.CassandraReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpecialComparisonUtils {

    public static boolean filterBySpecialComparison(Object objectAttributeValue, Object queryParameterValue, SpecialComparisonType comparisonType) {
        switch (comparisonType) {
            case NOT_EQUALS:
                return !objectAttributeValue.equals(queryParameterValue);
            case STARTS:
                return objectAttributeValue.toString().startsWith(queryParameterValue.toString());
            case ENDS:
                return objectAttributeValue.toString().endsWith(queryParameterValue.toString());
            case CONTAINS:
                return objectAttributeValue.toString().contains(queryParameterValue.toString());
            case COMPARE_TO_NULL:
                return queryParameterValue == null ? objectAttributeValue == null : objectAttributeValue.equals(queryParameterValue);
            default:
                return true;
        }
    }

    public static boolean filterBySpecialComparisonClause(Object parameterValue, SpecialComparisonClause clause) {
        return filterBySpecialComparison(parameterValue, clause.getValue(), clause.getSpecialComparisonType());
    }

    public static <E> List filterListBySpecialComparisonClause(List<E> list, SpecialComparisonClause clause) {
        Class clazz = list.get(0).getClass();

        Method[] getters = CassandraReflectionUtils.getClassGetters(clazz);

        Method getter = CassandraReflectionUtils.getClassGetterForField(clazz, getters, clause.getPropertyName());

        return list.stream().filter(obj -> {
            try {
                //if (getter.invoke(obj) != null)
                    return filterBySpecialComparisonClause(getter.invoke(obj), clause);
                //else
                  //  return false;
            } catch (Exception e) {
                throw new MethodInvocationException("Could not invoke method \"" + getter.getName() + "\" on object \"" + obj + "\", this is caused by: " + e.getMessage());
            }
        }).collect(Collectors.toList());
    }

    public static Object[] getArgumentsNotHavingSpecialClause(Object[] args, List<SpecialComparisonClause> spc) {
        if (spc.isEmpty())
            return args;

        Object[] newArgs = new Object[args.length - spc.size()];
        Integer[] specialArgsPositions = spc.stream().map(clause -> clause.getArgPosition()).toArray(Integer[]::new);

        int currentNewArgs = 0;
        int currentSpecialArgs = 0;

        for (int i = 0; i < args.length && currentNewArgs < newArgs.length; i++) {
            if (i != specialArgsPositions[currentSpecialArgs]) {
                newArgs[currentNewArgs] = args[i];
                currentNewArgs++;
                currentSpecialArgs++;
            }
        }

        return newArgs;
    }

    public static List<SpecialComparisonClause> getSpecialComparisonClauseWithArguments(Object[] args, List<SpecialComparisonClause> spc) {
        List<SpecialComparisonClause> newSpc = new ArrayList<>();

        for (SpecialComparisonClause c : spc) {
            c.setValue(args[c.getArgPosition()]);
            newSpc.add(c);
        }

        return newSpc;
    }
}
