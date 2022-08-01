package net.sf.esfinge.querybuilder.cassandra.querybuilding.specialcomparison;

import java.util.Objects;

public class SpecialComparisonClause {

    String propertyName;

    SpecialComparisonType specialComparisonType;

    String nextConnector;

    public SpecialComparisonClause(String propertyName, SpecialComparisonType specialComparisonType) {
        this.propertyName = propertyName;
        this.specialComparisonType = specialComparisonType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public SpecialComparisonType getSpecialComparisonType() {
        return specialComparisonType;
    }

    public String getNextConnector() {
        return nextConnector;
    }

    public void setNextConnector(String nextConnector) {
        this.nextConnector = nextConnector;
    }

    @Override
    public String toString() {
        return "SpecialComparisonClause{" +
                "propertyName='" + propertyName + '\'' +
                ", specialComparisonType=" + specialComparisonType +
                ", nextConnector='" + nextConnector + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialComparisonClause that = (SpecialComparisonClause) o;
        return Objects.equals(propertyName, that.propertyName) && specialComparisonType == that.specialComparisonType && Objects.equals(nextConnector, that.nextConnector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName, specialComparisonType, nextConnector);
    }
}
