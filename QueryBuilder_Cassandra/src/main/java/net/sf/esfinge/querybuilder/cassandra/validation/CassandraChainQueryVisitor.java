package net.sf.esfinge.querybuilder.cassandra.validation;

import net.sf.esfinge.querybuilder.cassandra.CassandraQueryVisitor;
import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidNumberOfSecondaryQueriesException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.util.Map;
import java.util.Set;

public class CassandraChainQueryVisitor implements QueryVisitor {

    private CassandraQueryVisitor primaryVisitor;
    private CassandraChainQueryVisitor secondaryVisitor;

    public CassandraChainQueryVisitor() {
        this.primaryVisitor = new CassandraQueryVisitor();
    }


    @Override
    public void visitEntity(String entity) {
        if (secondaryVisitor == null) {
            primaryVisitor.visitEntity(entity);
        }
    }

    @Override
    public void visitConector(String connector) {
        if (connector.equalsIgnoreCase("OR")) {
            if (secondaryVisitor == null) {
                primaryVisitor.visitEnd();
                secondaryVisitor = new CassandraChainQueryVisitor();
                secondaryVisitor.visitEntity(primaryVisitor.getEntity());
            } else
                throw new InvalidNumberOfSecondaryQueriesException("Error, the maximum number of secondary queries permitted is one");
        } else
            primaryVisitor.visitConector(connector);
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType) {
        if (secondaryVisitor == null) {
            primaryVisitor.visitCondition(parameter, comparisonType);
        } else {
            secondaryVisitor.visitCondition(parameter, comparisonType);
        }
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, NullOption nullOption) {
        if (secondaryVisitor == null) {
            primaryVisitor.visitCondition(parameter, comparisonType, nullOption);
        } else {
            secondaryVisitor.visitCondition(parameter, comparisonType, nullOption);
        }
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, Object value) {
        if (secondaryVisitor == null) {
            primaryVisitor.visitCondition(parameter, comparisonType, value);
        } else {
            secondaryVisitor.visitCondition(parameter, comparisonType, value);
        }
    }

    @Override
    public void visitOrderBy(String parameter, OrderingDirection orderingDirection) {
        if (secondaryVisitor == null) {
            primaryVisitor.visitOrderBy(parameter, orderingDirection);
        } else {
            secondaryVisitor.visitOrderBy(parameter, orderingDirection);
        }
    }

    @Override
    public void visitEnd() {
        if (secondaryVisitor == null) {
            primaryVisitor.visitEnd();
        } else {
            secondaryVisitor.visitEnd();
        }
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
