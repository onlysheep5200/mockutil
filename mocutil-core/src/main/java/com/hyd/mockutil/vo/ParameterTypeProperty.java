package com.hyd.mockutil.vo;

import lombok.Data;

@Data
public class ParameterTypeProperty {

    private Class baseClass; //raw type of parameterType

    private Class keyClass;

    private ParameterTypeProperty innerProperty; //

    private boolean isMap;

    private boolean isCollection;
}
