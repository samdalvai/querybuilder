package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join;

import net.sf.esfinge.querybuilder.cassandra.CassandraEntityClassProvider;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

import java.util.Objects;

public class JoinClause {

    private String propertyTypeName;
    private String propertyName;
    private ComparisonType comparisonType;

    private Object value;

    private int argPosition;

    public JoinClause(String propertyTypeName, String propertyName, ComparisonType comparisonType) {
        this.propertyTypeName = propertyTypeName;
        this.propertyName = propertyName;
        this.comparisonType = comparisonType;
    }

    public String getPropertyTypeName() {
        return propertyTypeName;
    }

    public void setPropertyTypeName(String propertyTypeName) {
        this.propertyTypeName = propertyTypeName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getArgPosition() {
        return argPosition;
    }

    public void setArgPosition(int argPosition) {
        this.argPosition = argPosition;
    }

    @Override
    public String toString() {
        return "JoinClause{" +
                "propertyTypeName='" + propertyTypeName + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", comparisonType=" + comparisonType +
                ", value=" + value +
                ", argPosition=" + argPosition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinClause that = (JoinClause) o;
        return argPosition == that.argPosition && Objects.equals(propertyTypeName, that.propertyTypeName) && Objects.equals(propertyName, that.propertyName) && comparisonType == that.comparisonType && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyTypeName, propertyName, comparisonType, value, argPosition);
    }
}
