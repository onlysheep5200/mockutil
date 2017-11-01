package com.hyd.mockutil.vo;

import com.hyd.mockutil.annotation.MockStrategyEnum;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Data
public class FieldProperty {

    private Field field;

    private Class fieldClass;

    private Method setterMethod;

    private MockStrategyEnum mockStrategy;

    private String mockStrategySetting;

    private boolean ignore = false;

    private List<ParameterTypeProperty> parameterTypes;

    private Object instance;

    private Object bidingInstance;

    List<FieldProperty> fieldsOfField;

    private boolean isRawCollection;

    private boolean isRawMap;

    private boolean isBase;
}
