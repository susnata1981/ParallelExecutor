package com.ziplly.parallelexecutor;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Base class for {@link ParallelExecutor} implementation.
 * 
 * @author shaan
 */
public abstract class AbstractExecutor implements ParallelExecutor {
    private final ListeningExecutorService executors;
    private final ExecutionContext ctx;

    public AbstractExecutor(ListeningExecutorService executorService) {
        this.ctx = createExecutionContext();
        this.executors = executorService;
    }

    @Override
    public void run(OperationBuilder builder, Callback callback) {
        Preconditions.checkNotNull(builder);

        if (builder.getOperations().isEmpty()) {
            return;
        }

        doRun(builder, callback);
    }

    protected OperationResultPair submit(
            final Operation operation) {
        ListenableFuture<Response> response = executors.submit(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return operation.execute(ctx);
            }
        });

        OperationResultPair operationResult = new OperationResultPair();
        operationResult.setOperation(operation);
        operationResult.setResponse(response);
        return operationResult;
    }

    protected void stop() {
        executors.shutdown();
    }
    
    protected abstract void doRun(OperationBuilder builder, Callback callback);

    private ExecutionContext createExecutionContext() {
        ExecutionContext context = new ExecutionContext();
        context.setStartTime(new Date().getTime());
        return context;
    }

    public static class OperationResultPair {

        private Operation operation;
        private ListenableFuture<Response> response;

        public Operation getOperation() {
            return operation;
        }

        public void setOperation(Operation operation) {
            this.operation = operation;
        }

        public ListenableFuture<Response> getResponse() {
            return response;
        }

        public void setResponse(ListenableFuture<Response> response) {
            this.response = response;
        }
    }
}
