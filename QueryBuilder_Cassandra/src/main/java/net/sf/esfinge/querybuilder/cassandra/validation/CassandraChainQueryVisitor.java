package net.sf.esfinge.querybuilder.cassandra.validation;

import net.sf.esfinge.querybuilder.cassandra.CassandraQueryVisitor;
import net.sf.esfinge.querybuilder.cassandra.config.ConfigReader;
import net.sf.esfinge.querybuilder.cassandra.exceptions.SecondaryQueryLimitExceededException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.ConditionStatement;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CassandraChainQueryVisitor implements QueryVisitor {

    VisitorType visitorType;

    private final CassandraQueryVisitor primaryVisitor;
    private CassandraChainQueryVisitor secondaryVisitor;

    private CassandraChainQueryVisitor joinVisitor;

    private final int queryDepth;

    private VisitorType lastVisitorType = VisitorType.PRIMARY;

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
        if (lastVisitorType == VisitorType.JOIN){
            joinVisitor.visitConector(connector);
        } else {
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
    }

    private void updateArgumentOffsetForPrimaryVisitor() {
        if (joinVisitor != null){
            int joinConditions = joinVisitor.getJoinConditions().size();
            int joinSpecialClauses = joinVisitor.getJoinSpecialComparisonClauses().size();

            primaryVisitor.setArgumentPositionOffset(primaryVisitor.getConditions().size() + primaryVisitor.getSpecialComparisonClauses().size() + joinConditions + joinSpecialClauses);
        }
    }

    private void initJoinQueryVisitor(String parameter){
        if (joinVisitor == null){
            joinVisitor = new CassandraChainQueryVisitor(this.queryDepth + 1, primaryVisitor);
            String joinEntity = parameter.substring(0,1).toUpperCase() + parameter.substring(1,parameter.indexOf("."));
            joinVisitor.visitEntity(joinEntity);
        }
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType) {
        updateArgumentOffsetForPrimaryVisitor();

        if (parameter.contains(".")){
            initJoinQueryVisitor(parameter);

            String joinParameter = parameter.substring(parameter.indexOf(".") + 1);
            joinVisitor.visitCondition(joinParameter, comparisonType);

            lastVisitorType = VisitorType.JOIN;
        } else {
            if (secondaryVisitor == null) {
                primaryVisitor.visitCondition(parameter, comparisonType);
                lastVisitorType = VisitorType.PRIMARY;
            } else {
                secondaryVisitor.visitCondition(parameter, comparisonType);
                lastVisitorType = VisitorType.SECONDARY;
            }
        }

    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, NullOption nullOption) {
        updateArgumentOffsetForPrimaryVisitor();

        if (parameter.contains(".")) {
            initJoinQueryVisitor(parameter);

            String joinParameter = parameter.substring(parameter.indexOf(".") + 1);
            joinVisitor.visitCondition(joinParameter, comparisonType, nullOption);

            lastVisitorType = VisitorType.JOIN;
        } else {
            if (secondaryVisitor == null) {
                primaryVisitor.visitCondition(parameter, comparisonType, nullOption);
                lastVisitorType = VisitorType.PRIMARY;
            } else {
                secondaryVisitor.visitCondition(parameter, comparisonType, nullOption);
                lastVisitorType = VisitorType.SECONDARY;
            }
        }
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, Object value) {
        updateArgumentOffsetForPrimaryVisitor();

        if (parameter.contains(".")) {
            initJoinQueryVisitor(parameter);

            String joinParameter = parameter.substring(parameter.indexOf(".") + 1);
            joinVisitor.visitCondition(joinParameter, comparisonType, value);

            lastVisitorType = VisitorType.JOIN;
        } else {
            if (secondaryVisitor == null) {
                primaryVisitor.visitCondition(parameter, comparisonType, value);
                lastVisitorType = VisitorType.PRIMARY;
            } else {
                secondaryVisitor.visitCondition(parameter, comparisonType, value);
                lastVisitorType = VisitorType.SECONDARY;
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
        if (joinVisitor != null){
            joinVisitor.visitEnd();
        }

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

    public CassandraChainQueryVisitor getJoinVisitor() {
        return joinVisitor;
    }

    public List<ConditionStatement> getJoinConditions() {
        return primaryVisitor.getConditions();
    }

    public List<SpecialComparisonClause> getJoinSpecialComparisonClauses() {
        return primaryVisitor.getSpecialComparisonClauses();
    }

    public int getArgumentPositionOffset() {
        return primaryVisitor.getArgumentPositionOffset();
    }
}
