package com.hyd.mockutil.utils;

import com.google.common.base.Preconditions;
import com.hyd.mockutil.annotation.MockStrategy;
import com.hyd.mockutil.vo.FieldProperty;
import com.hyd.mockutil.vo.ParameterTypeProperty;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FieldUtil {

    public static List<Class> PRIMITIVE_CLASS = Arrays.asList(int.class,long.class,char.class,byte.class,double.class,float.class);

    public static List<Class> BASE_CLASS_LIST = Arrays.asList(Integer.class,Long.class,Character.class,Byte.class,Double.class, Float.class,
            String.class,Boolean.class, int.class,long.class,char.class,byte.class,double.class,float.class);

    public static FieldProperty parseField(Field field,Object bidingInstance) {
        Preconditions.checkNotNull(field);
        Preconditions.checkNotNull(bidingInstance);
        field.setAccessible(true);
        FieldProperty fieldProperty = new FieldProperty();
        fieldProperty.setField(field);
        fieldProperty.setBidingInstance(bidingInstance);
        fieldProperty.setFieldClass(field.getType());
        Annotation[] declaredAnnotations = field.getDeclaredAnnotations();

        //MockStrategy annotation
        MockStrategy mockStrategy = null;
        for (Annotation annotation : declaredAnnotations){
            if (annotation instanceof MockStrategy){
                mockStrategy = (MockStrategy) annotation;
                break;
            }
        }
        fieldProperty.setMockStrategy(mockStrategy.value());
        fieldProperty.setMockStrategySetting(mockStrategy.setting());

        //setter method
        int modifiers = field.getModifiers();
        boolean isPublic = Modifier.isPublic(modifiers);
        if (!isPublic) {
            //setter method
            String setterName = String.format("set%s%s", field.getName().substring(0, 1).toUpperCase()
                    , field.getName().length() > 1 ? field.getName().substring(1) : "");
            try {
                Method setterMethod = bidingInstance.getClass().getMethod(setterName, field.getType());
                fieldProperty.setSetterMethod(setterMethod);
            } catch (NoSuchMethodException e) {
                // not public and have no setter --> ignore this field
                fieldProperty.setIgnore(true);
            }
        }
        // base class parse end
        if (BASE_CLASS_LIST.contains(fieldProperty.getFieldClass())){
            fieldProperty.setBase(true);
            return fieldProperty;
        }


        //collection or map
        if (fieldProperty.getFieldClass().isAssignableFrom(Collection.class)) {
            handleCollection(fieldProperty);
        }
        else if(fieldProperty.getFieldClass().isAssignableFrom(Map.class)) {
            handleMap(fieldProperty);
        }else if (fieldProperty.getFieldClass().isAssignableFrom(Serializable.class)){
            handleCustomClass(fieldProperty);
        }
        return fieldProperty;
    }

    private static void handleMap(FieldProperty fieldProperty) {
        //默认map的key不可为泛型
    }

    private static void handleCollection(FieldProperty fieldProperty) {
        Field field = fieldProperty.getField();
        Type genericType = field.getGenericType();
        if (! (genericType instanceof ParameterizedType)){
            fieldProperty.setRawCollection(true);
            return;
        }
        ParameterizedType pt = (ParameterizedType)genericType;

        ParameterTypeProperty parameterTypeProperty = new ParameterTypeProperty();
        handleParameterType4Collection(pt,parameterTypeProperty);

    }

    private static void handleParameterType4Collection(ParameterizedType pt, ParameterTypeProperty parameterTypeProperty) {

        parameterTypeProperty.setBaseClass((Class) pt.getRawType());
        parameterTypeProperty.setCollection(true);

        //collection only have 1 arg
        Type argType = pt.getActualTypeArguments()[0];
        ParameterTypeProperty subParameterType = new ParameterTypeProperty();
        parameterTypeProperty.setInnerProperty(subParameterType);
        if (!(argType instanceof ParameterizedType)){
            subParameterType.setBaseClass((Class)argType);
        }else{
            pt = (ParameterizedType) argType;
            if (Collection.class.isAssignableFrom((Class<?>) pt.getRawType())) {
                handleParameterType4Collection(pt,subParameterType);
            } else if (Map.class.isAssignableFrom((Class<?>) pt.getRawType())){
                //TODO:处理map对应的parameterTypeProperty参数
            }
        }
    }


    private static void handleCustomClass(FieldProperty fieldProperty) {

    }

}
