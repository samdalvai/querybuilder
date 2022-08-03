package net.sf.esfinge.querybuilder.cassandra.querybuilding.specialcomparison;

public class SpecialComparisonUtils {

    public static boolean filterBySpecialComparison(Object parameterValue, Object valueToCompare, SpecialComparisonType comparisonType){
        switch (comparisonType){
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
}
