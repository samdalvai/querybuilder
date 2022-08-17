package net.sf.esfinge.querybuilder.cassandra.validation;

import net.sf.esfinge.querybuilder.cassandra.CassandraQueryVisitor;
import net.sf.esfinge.querybuilder.cassandra.config.ConfigReader;
import net.sf.esfinge.querybuilder.cassandra.exceptions.JoinDepthLimitExceededException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.SecondaryQueryLimitExceededException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.QueryBuildingUtils;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.util.Map;
import java.util.Set;

public class CassandraChainQueryVisitor implements QueryVisitor {

    private final CassandraQueryVisitor primaryVisitor;
    private CassandraChainQueryVisitor secondaryVisitor;

    private final int queryDepth;

    public CassandraChainQueryVisitor(int queryDepth) {
        this.primaryVisitor = new CassandraQueryVisitor();
        this.queryDepth = queryDepth;
    }

    public CassandraChainQueryVisitor(int queryDepth, CassandraQueryVisitor previousVisitor) {
        this.primaryVisitor = new CassandraQueryVisitor(previousVisitor);
        this.queryDepth = queryDepth;
    }

    @Override
    public void visitEntity(String entity) {
        if (secondaryVisitor == null) {
            primaryVisitor.visitEntity(entity);
        }
    }

    @Override
    public void visitConector(String connector) {
        if (secondaryVisitor == null) {
            if (connector.equalsIgnoreCase("OR")) {
                primaryVisitor.visitEnd();

                int queryLimit = ConfigReader.getConfiguration().getSecondaryQueryLimit();

                if (queryDepth >= queryLimit)
                    throw new SecondaryQueryLimitExceededException("Current query depth is " + queryDepth + ", but the configured limit is " + queryLimit);

                secondaryVisitor = new CassandraChainQueryVisitor(this.queryDepth + 1, primaryVisitor);
                secondaryVisitor.visitEntity(primaryVisitor.getEntity());
            } else
                primaryVisitor.visitConector(connector);

        } else {
            secondaryVisitor.visitConector(connector);
        }
    }

    private void verifySecondaryVisitorForJoin(String parameter) {
        // TODO: MIGHT SAVE NAME OF PRIMARY ENTITY?
        if (QueryBuildingUtils.countOccurrenceOfCharacterInString(parameter, '.') > 1)
            throw new JoinDepthLimitExceededException("Join depth exceeded, the maximum depth is 1");

        String joinEntity = parameter.substring(0, 1).toUpperCase() + parameter.substring(1, parameter.indexOf('.')).toLowerCase();
        System.out.println("Join entity: " + joinEntity);

        primaryVisitor.visitEnd();
        secondaryVisitor = new CassandraChainQueryVisitor(this.queryDepth + 1, primaryVisitor);
        secondaryVisitor.visitEntity(joinEntity);

    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType) {
        if (parameter.contains(".")) {
            verifySecondaryVisitorForJoin(parameter);

            String joinParameter = parameter.substring(parameter.indexOf('.') + 1);
            secondaryVisitor.visitCondition(joinParameter, comparisonType);
        } else {
            if (secondaryVisitor == null) {
                primaryVisitor.visitCondition(parameter, comparisonType);
            } else {
                secondaryVisitor.visitCondition(parameter, comparisonType);
            }
        }
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, NullOption nullOption) {
        if (parameter.contains(".")) {
            verifySecondaryVisitorForJoin(parameter);

            String joinParameter = parameter.substring(parameter.indexOf('.') + 1);
            secondaryVisitor.visitCondition(joinParameter, comparisonType, nullOption);
        } else {
            if (secondaryVisitor == null) {
                primaryVisitor.visitCondition(parameter, comparisonType, nullOption);
            } else {
                secondaryVisitor.visitCondition(parameter, comparisonType, nullOption);
            }
        }
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, Object value) {
        if (parameter.contains(".")) {
            verifySecondaryVisitorForJoin(parameter);

            String joinParameter = parameter.substring(parameter.indexOf('.') + 1);
            System.out.println("Join parameter: " + joinParameter);
            secondaryVisitor.visitCondition(joinParameter, comparisonType, value);
        } else {
            if (secondaryVisitor == null) {
                primaryVisitor.visitCondition(parameter, comparisonType, value);
            } else {
                secondaryVisitor.visitCondition(parameter, comparisonType, value);
            }
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

    public CassandraChainQueryVisitor getSecondaryVisitor() {
        return secondaryVisitor;
    }

}
