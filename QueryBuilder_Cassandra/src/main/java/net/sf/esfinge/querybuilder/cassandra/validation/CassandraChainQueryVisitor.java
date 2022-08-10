package net.sf.esfinge.querybuilder.cassandra.validation;

import net.sf.esfinge.querybuilder.cassandra.CassandraQueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.util.Map;
import java.util.Set;

public class CassandraChainQueryVisitor implements QueryVisitor {

    private CassandraQueryVisitor primaryVisitor;
    private CassandraQueryVisitor secondaryVisitor;

    public CassandraChainQueryVisitor(CassandraQueryVisitor primaryVisitor) {
        this.primaryVisitor = primaryVisitor;
    }

    @Override
    public void visitEntity(String s) {

    }

    @Override
    public void visitConector(String s) {

    }

    @Override
    public void visitCondition(String s, ComparisonType comparisonType) {

    }

    @Override
    public void visitCondition(String s, ComparisonType comparisonType, NullOption nullOption) {

    }

    @Override
    public void visitCondition(String s, ComparisonType comparisonType, Object o) {

    }

    @Override
    public void visitOrderBy(String s, OrderingDirection orderingDirection) {

    }

    @Override
    public void visitEnd() {

    }

    @Override
    public boolean isDynamic() {
        return primaryVisitor.isDynamic();
    }

    @Override
    public String getQuery() {
        return primaryVisitor.getQuery();
    }

    @Override
    public String getQuery(Map<String, Object> map) {
        return primaryVisitor.getQuery(map);
    }

    @Override
    public Set<String> getFixParameters() {
        return primaryVisitor.getFixParameters();
    }

    @Override
    public Object getFixParameterValue(String s) {
        return primaryVisitor.getFixParameterValue(s);
    }

    @Override
    public QueryRepresentation getQueryRepresentation() {
        return primaryVisitor.getQueryRepresentation();
    }

    public QueryVisitor getSecondaryVisitor() {
        return secondaryVisitor;
    }
}
