/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ziplly.parallelexecutor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shaan
 */
public class AnnotationHandler {

    public static List<Method> findAnnotatedMethods(Class clazz, Class annotation) {
        List<Method> methods = new ArrayList<>();
        for (final Method method : clazz.getDeclaredMethods()) {
            Annotation executeAnnotation = method.getAnnotation(annotation);
            if (executeAnnotation != null
                    && method.getReturnType().equals(Response.class)) {
                methods.add(method);
            }
        }
        
        return methods;
    }
}
