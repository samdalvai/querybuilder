package net.sf.esfinge.querybuilder.cassandra.reflection;

import net.sf.esfinge.querybuilder.cassandra.exceptions.GetterNotFoundInClassException;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReflectionUtilsTest {

    @Test
    public void getClassGettersTest() {
        Method[] getters = ReflectionUtils.getClassGetters(TestClass.class);

        List<String> actual = Arrays.stream(getters).map(g -> g.getName()).collect(Collectors.toList());

        List<String> expected = new ArrayList<>();
        expected.add("getName");
        expected.add("getLastName");
        expected.add("getId");

        assertEquals(3, getters.length);
        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void getClassGettersForFieldsTest() {
        List<String> fields = new ArrayList<>();
        fields.add("name");
        fields.add("lastName");
        fields.add("id");

        Method[] gettersForFields = ReflectionUtils.getClassGettersForFields(TestClass.class, fields);

        List<String> actual = Arrays.stream(gettersForFields).map(g -> g.getName()).collect(Collectors.toList());

        List<String> expected = new ArrayList<>();
        expected.add("getName");
        expected.add("getLastName");
        expected.add("getId");

        assertEquals(3, gettersForFields.length);
        assertEquals(expected, actual);
    }

    @Test(expected = GetterNotFoundInClassException.class)
    public void getClassGettersForFieldsWithFieldNotPresentTest() {
        List<String> fields = new ArrayList<>();
        fields.add("name");
        fields.add("lastName");
        fields.add("notPresent");

        ReflectionUtils.getClassGettersForFields(TestClass.class, fields);
    }
}