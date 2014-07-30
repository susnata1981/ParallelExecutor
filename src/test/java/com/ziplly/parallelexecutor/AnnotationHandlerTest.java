package com.ziplly.parallelexecutor;

import com.ziplly.parallelexecutor.annotation.Execute;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author shaan
 */
@RunWith(MockitoJUnitRunner.class)

public class AnnotationHandlerTest {

    @Test
    public void findAnnotatedMethodsTest() throws NoSuchMethodException {
        List<Method> methods
                = AnnotationHandler.findAnnotatedMethods(TestAnnotation.class, Execute.class);

        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestAnnotation.class.getMethod("methodA", new Class[]{}));
        expectedMethods.add(TestAnnotation.class.getMethod("methodB", new Class[]{}));
        assertEquals(expectedMethods, methods);
    }

    private class TestAnnotation {

        @Execute
        public Response methodA() {
            return new Response<>(10);
        }

        @Execute
        public Response methodB() {
            return new Response<>(10);
        }

        @Execute
        public void methodC() {
        }
    }
}
