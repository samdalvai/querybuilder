package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join;

import net.sf.esfinge.querybuilder.cassandra.CassandraEntityClassProvider;

import java.util.List;

public class JoinUtils {

    public static boolean matchesJoinCondition(Object obj, List<JoinClause> joinClauses){
        CassandraEntityClassProvider provider = new CassandraEntityClassProvider();

        Class clazz =  provider.getEntityClass(joinClauses.get(0).getPropertyTypeName());

        for (JoinClause joinClause : joinClauses) {
            if (!matchesJoinCondition(clazz, obj, joinClause))
                return false;
        }

        return true;
    }

    public static boolean matchesJoinCondition(Class clazz, Object obj, JoinClause joinClauses){


        return false;
    }

}
