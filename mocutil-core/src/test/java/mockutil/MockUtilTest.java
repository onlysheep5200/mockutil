package mockutil;

import com.google.common.collect.Lists;
import mockutil.vo.Wrapper;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class MockUtilTest {
    @Test
    public void testJavaType(){
        Wrapper wrapper = new Wrapper();
        wrapper.setItems(Lists.newArrayList());
        Field[] declaredFields = Wrapper.class.getDeclaredFields();
        for (Field field : declaredFields){
            field.setAccessible(true);
            if (field.getType().equals(List.class)){
                Type genericType = field.getGenericType();

                if (genericType == null) continue;

                if (genericType instanceof ParameterizedType){
                    ParameterizedType parameterizedType = (ParameterizedType)genericType;
                    Class type = (Class)parameterizedType.getActualTypeArguments()[0];
                    System.out.println(type);
                }
            }
        }
        Method[] declaredMethods = MockUtilTest.class.getDeclaredMethods();
        Method declaredMethod = declaredMethods[0];
    }

    class BaseClassTest{
        private int i ;
    }

    @Test
    public void testBaseTest(){
        BaseClassTest classTest = new BaseClassTest();
        Field[] declaredFields = classTest.getClass().getDeclaredFields();
        Field declaredField = declaredFields[0];
        System.out.println(declaredField.getType());
        Assert.assertTrue(declaredField.getType().equals(int.class));
    }
}
