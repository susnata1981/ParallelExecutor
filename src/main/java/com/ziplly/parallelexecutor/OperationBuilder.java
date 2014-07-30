package com.ziplly.parallelexecutor;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.ziplly.parallelexecutor.annotation.Execute;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class using Fluent API pattern to build chain of {@link Operation}
 * classes.
 *
 * @author shaan
 */
public class OperationBuilder {

    private final List<Operation> operations = new ArrayList<>();
    
    private OperationBuilder() {
    }

    public static OperationBuilder create() {
        return new OperationBuilder();
    }

    public OperationBuilder add(Operation operation) {
        operations.add(operation);
        return this;
    }

    public <T> OperationBuilder register(final Class<T> clazz) 
            throws InstantiationException, IllegalAccessException {
        
        List<Method> methods = AnnotationHandler.findAnnotatedMethods(clazz, Execute.class);
        for (Method method : methods) {
            Operation proxyOperation = createProxyOperation(clazz, method);
            operations.add(proxyOperation);
        }

        return this;
    }

    public List<Operation> getOperations() {
        return ImmutableList.copyOf(operations.iterator());
    }

    private Operation createProxyOperation(final Class clazz, final Method method) {
        Operation proxyOperation = new AbstractOperation() {
            @Override
            public Response execute(ExecutionContext ctx) {
                try {
                    Object orig = clazz.newInstance();
                    return (Response) method.invoke(orig, new Object[]{});
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    Logger.getLogger(OperationBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }

                return new Response<>(Optional.absent());
            }
        };
        
        return proxyOperation;
    }
}
