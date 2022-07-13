package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.exceptions.QueryParametersMismatchException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.QueryBuildingUtils;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryBuildingUtilsTest {


    @Test
    public void replaceQueryArgsWithOneArgTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE id > ?";
        Object[] args = {1};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE id > 1", newQuery);
    }

    @Test
    public void replaceQueryArgsWithTwoArgsTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE id = ? AND lastName = ?";
        Object[] args = {1, "Max"};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE id = 1 AND lastName = 'Max'", newQuery);
    }

    @Test
    public void replaceQueryArgsWithTwoStringArgsTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE name = ? AND lastName = ?";
        Object[] args = {"Max", "Power"};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'Max' AND lastName = 'Power'", newQuery);
    }

    @Test
    public void replaceQueryArgsWithNullValueArgTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person";
        Object[] args = {null};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person", newQuery);
    }

    @Test
    public void replaceQueryArgsWithNullAndNonNullValueArgsTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE name = ?";
        Object[] args = {null, "Max"};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'Max'", newQuery);
    }

    @Test(expected = QueryParametersMismatchException.class)
    public void replaceQueryArgsWithMoreArgsInArrayTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE id = ? AND lastName = ?";
        Object[] args = {1, "Max", "Additional"};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);
    }

    @Test(expected = QueryParametersMismatchException.class)
    public void replaceQueryArgsWithMoreArgsInQueryTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE id = ? AND lastName = ? OR city = ?";
        Object[] args = {1, "Max"};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);
    }


    @Test
    public void getValueRepresentationByTypeWithNumbersTest() {
        assertEquals("1", QueryBuildingUtils.getValueRepresentationByType(1));
        assertEquals("1.1", QueryBuildingUtils.getValueRepresentationByType(1.1));
    }

    @Test
    public void getValueRepresentationByTypeWithTextTest() {
        assertEquals("'Max'", QueryBuildingUtils.getValueRepresentationByType("Max"));
        assertEquals("'A'", QueryBuildingUtils.getValueRepresentationByType('A'));
    }

    @Test
    public void getParameterNameFromParameterWithComparisonTest(){
        List<String> name = new ArrayList<>();
        name.add("last");
        name.add("Name");
        name.add("Lesser");
        name.add("Or");
        name.add("Equals");
        System.out.println("The comparison type:");
        System.out.println(getComparisonType("lastNameLesserOrEquals").getOpName());
        System.out.println("What??");


        assertEquals("name", QueryBuildingUtils.getParameterNameFromParameterWithComparison("nameEquals"));
        assertEquals("age", QueryBuildingUtils.getParameterNameFromParameterWithComparison("ageLesser"));
        assertEquals("lastName", QueryBuildingUtils.getParameterNameFromParameterWithComparison("lastNameLesserOrEquals"));
    }

    // TODO: IMPLEMENT THIS METHOD
    public static ComparisonType getComparisonType(List<String> comparisonName, int index) {
        for(ComparisonType cp : ComparisonType.values()){
            String[] values = cp.name().split("_");
            boolean flag = true;
            for(int i = 0; i<values.length; i++){
                if(comparisonName.size() <= index+i || !values[i].toLowerCase().equals(comparisonName.get(index+i))){
                    flag = false;
                }
            }
            if(flag)
                return cp;
        }
        return ComparisonType.EQUALS;
    }

    public static ComparisonType getComparisonType(String property) {
        ComparisonType[] var1 = ComparisonType.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ComparisonType comparisonType = var1[var3];
            String comparison = comparisonType.name().replace("_", "");
            if (property.toUpperCase().contains(comparison)) {
                return comparisonType;
            }
        }

        return ComparisonType.EQUALS;
    }
}