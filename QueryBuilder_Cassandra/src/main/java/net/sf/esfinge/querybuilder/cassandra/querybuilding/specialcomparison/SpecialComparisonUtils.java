package net.sf.esfinge.querybuilder.cassandra.querybuilding.specialcomparison;

import net.sf.esfinge.querybuilder.cassandra.CassandraQueryRepresentation;
import net.sf.esfinge.querybuilder.cassandra.reflection.CassandraReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static Object[] getArgumentsNotHavingSpecialClause(Object[] args, List<SpecialComparisonClause> spc){
        if (spc.isEmpty())
            return args;

        Object[] newArgs = new Object[args.length - spc.size()];
        Integer[] specialArgsPositions = spc.stream().map(clause -> clause.getArgPosition()).toArray(Integer[]::new);

        int currentNewArgs = 0;
        int currentSpecialArgs = 0;

        for (int i = 0; i < args.length; i++){
            if (i != specialArgsPositions[currentSpecialArgs]){
                newArgs[currentNewArgs] = args[i];
                currentNewArgs++;
                currentSpecialArgs++;
            }
        }

        return newArgs;
    }

    public static List<SpecialComparisonClause> getSpecialComparisonClauseWithArguments(Object[] args, List<SpecialComparisonClause> spc){
        List<SpecialComparisonClause> newSpc = new ArrayList<>();

        for (SpecialComparisonClause c : spc){
            c.setValue(args[c.getArgPosition()]);
            newSpc.add(c);
        }

        return newSpc;
    }
}
