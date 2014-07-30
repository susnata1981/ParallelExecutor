package com.ziplly.parallelexecutor;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import java.util.concurrent.Callable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author shaan
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractExecutorTest {

    @Mock
    private ListeningExecutorService executorService;
    @Mock
    private ListenableFuture<Response> response;
    @Mock
    private Callback callback;
    
    private ParallelExecutor executor;
    private Operation operation1;
    private Operation operation2;

    @Before
    public void setUp() {
        this.executor = DefaultExecutor.create(executorService);
        this.operation1 = new Operation() {

            @Override
            public Response<Integer> execute(ExecutionContext ctx) {
                return new Response<>(0);
            }
        };

        this.operation2 = new Operation() {

            @Override
            public Response<Integer> execute(ExecutionContext ctx) {
                return new Response<>(0);
            }
        };
    }

    @Test
    public void doRunTest() {
        OperationBuilder builder = OperationBuilder.create()
                .add(operation1)
                .add(operation2);

        when(executorService.submit((Callable<Response>) any(Callback.class)))
                .thenReturn(response);
        executor.run(builder, null);

        verify(executorService, times(2)).submit(any(Callable.class));
    }
}
