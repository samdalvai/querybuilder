package net.sf.esfinge.querybuilder.cassandra.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.exceptions.ComparisonTypeNotFoundException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.QueryParametersMismatchException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

import java.util.Arrays;

public class QueryBuildingUtils {

    public static String replaceQueryArgs(String query, Object[] args) {
        int paramOccurrence = countOccurrenceOfCharacterInString(query, '?');

        if (paramOccurrence != countNotNullArguments(args))
            throw new QueryParametersMismatchException("Number of parameters in the query different from the number of arguments");

        String newQuery = query;

        // Skip substituting values equal to null
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null)
                newQuery = newQuery.substring(0, newQuery.indexOf('?')) + getValueRepresentationByType(args[i]) + newQuery.substring(newQuery.indexOf('?') + 1);
        }

        return newQuery;
    }

    private static int countNotNullArguments(Object[] args) {
        return Arrays.stream(args).filter(arg -> arg != null).toArray(Object[]::new).length;
    }

    private static int countOccurrenceOfCharacterInString(String string, Character character) {
        return string.chars()
                .filter(c -> c == character)
                .toArray()
                .length;
    }

    public static String getValueRepresentationByType(Object value) {
        String className = value.getClass().getSimpleName();

        if (className.equals("String") || className.equals("Character"))
            return "'" + value + "'";

        return "" + value + "";
    }

    public static String extractParameterNameFromParameterWithComparison(String namedParameter) {
        ComparisonType cp = getComparisonType(namedParameter);
        System.out.println(cp.getOpName());
        return namedParameter.replace(cp.getOpName(), "");
    }

    public static ComparisonType getComparisonType(String property) {
        ComparisonType[] comparisons = ComparisonType.values();
        ComparisonType out = null;

        // Get the longest comparison match among the comparisons
        // Starting from the right
        int longest = 0;
        for (ComparisonType c : comparisons) {
            int i = property.length() - 1;
            int j = c.getOpName().length() - 1;
            int currentMatch = 0;

            while (i >= 0 && j >= 0) {
                if (property.charAt(i) == c.getOpName().charAt(j)) {
                    currentMatch++;
                    i--;
                    j--;
                    if (currentMatch > longest) {
                        if (j == 0){
                            longest = currentMatch;
                            out = c;
                        }
                    }
                } else {
                    break;
                }
            }
        }

        if (out == null)
            throw new ComparisonTypeNotFoundException("Comparison type not found for: " + property);

        return out;
    }


}
