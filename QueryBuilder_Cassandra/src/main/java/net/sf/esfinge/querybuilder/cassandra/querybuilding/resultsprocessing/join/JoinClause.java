package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

public class JoinClause {

    private String propertyTypeName;
    private String propertyName;
    private ComparisonType comparisonType;

    public JoinClause(String propertyTypeName, String propertyName, ComparisonType comparisonType) {
        this.propertyTypeName = propertyTypeName;
        this.propertyName = propertyName;
        this.comparisonType = comparisonType;
    }

    @Override
    public String toString() {
        return "JoinClause{" +
                "propertyTypeName='" + propertyTypeName + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", comparisonType=" + comparisonType +
                '}';
    }


}
