package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.secondaryquery;

import net.sf.esfinge.querybuilder.cassandra.exceptions.MethodInvocationException;
import net.sf.esfinge.querybuilder.cassandra.reflection.CassandraReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SecondaryQueryUtils {


    public static boolean reflectiveEquals(Object obj1, Object obj2) {
        if (!obj1.getClass().equals(obj2.getClass()))
            return false;

        Method[] obj1Getters = CassandraReflectionUtils.getClassGetters(obj1.getClass());
        Method[] obj2Getters = CassandraReflectionUtils.getClassGetters(obj2.getClass());

        for (int i = 0; i < obj1Getters.length; i++){
            try {
                if (!obj1Getters[i].invoke(obj1).equals(obj2Getters[i].invoke(obj2)))
                    return false;
            } catch (Exception e) {
                throw new MethodInvocationException(e.getMessage());
            }
        }

        return true;
    }

    public static <E> List removeDuplicateElementsFromList(List<E> list){
        List<E> result = new ArrayList<>();

        for (E e : list){
            if (!isObjectInList(e,result))
                result.add(e);
        }

        return result;
    }

    private static <E> boolean isObjectInList(E object, List<E> list) {
        for (E e : list) {
            if (SecondaryQueryUtils.reflectiveEquals(e, object))
                return true;
        }

        return false;
    }
}
