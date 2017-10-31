package mockutil;

import com.google.common.collect.Lists;
import mockutil.vo.Wrapper;
import org.junit.Test;

import java.lang.reflect.Field;
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
    }
}
