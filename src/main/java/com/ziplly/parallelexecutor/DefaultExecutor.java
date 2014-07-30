package com.ziplly.parallelexecutor;

import com.google.common.base.Preconditions;
import static com.google.common.base.Preconditions.checkState;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Default implementation of {@link ParallelExecutor} class.
 * 
 * @author shaan
 */
public class DefaultExecutor extends AbstractExecutor {

    private final List<OperationResultPair> operationResultPairs;
    private ListenableFuture<List<Response>> allAsList;
    private State state;

    /**
     * Static factory for creating an instance.
     * @return an instance of {@link DefaultExecutor} 
     */
    public static DefaultExecutor create() {
        return create(MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5)));
    }

    public static DefaultExecutor create(ListeningExecutorService executorService) {
        return new DefaultExecutor(executorService);
    }
    
    /**
     * Private constructor
     */
    private DefaultExecutor(ListeningExecutorService executorService) {
        super(executorService);
        this.state = State.IDLE;
        this.operationResultPairs = new ArrayList<>();
    }

    /**
     * Starts parallel execution of the operations in threads.
     * 
     * @param builder representing all the operations that needs to be performed.
     * @param callback handler to handle the response once processing is over.
     */
    @Override
    protected void doRun(OperationBuilder builder, Callback callback) {
        checkState(state == State.IDLE);

        this.state = State.RUNNING;
        for (Operation operation : builder.getOperations()) {
            OperationResultPair operationResultPair = submit(operation);
            operationResultPairs.add(operationResultPair);
        }

        waitForCompletion(callback);
        this.state = State.WAITING;
    }

    /**
     * Blocking call to get the result of all the operations in progress.
     * 
     * @return {@link ResultContainer} instance holding the result of all 
     *          operations.
     * @throws InterruptedException
     * @throws ExecutionException 
     */
    @Override
    public ResultContainer get() throws InterruptedException, ExecutionException {
        checkState(state == State.WAITING);
        Preconditions.checkNotNull(allAsList);
        List<Response> responses = allAsList.get();
        ResultContainer container = new ResultContainer();
        for (int i = 0; i < responses.size(); i++) {
            container.add(operationResultPairs.get(i).getOperation(), responses.get(i));
        }

        return container;
    }
    
    /**
     * Waits for processing to be complete and once done uses callback to notify
     * clients.
     */
    private void waitForCompletion(final Callback callback) {
        List<ListenableFuture<Response>> futures = getFutures(operationResultPairs);
        this.allAsList = Futures.allAsList(futures);
        Futures.addCallback(allAsList, new FutureCallback<List<Response>>() {

            @Override
            public void onSuccess(List<Response> responses) {
                ResultContainer container = new ResultContainer();
                for (int i = 0; i < responses.size(); i++) {
                    container.add(operationResultPairs.get(i).getOperation(),
                            responses.get(i));
                }

                if (callback != null) {
                    callback.success(container);
                }

                DefaultExecutor.this.markAsFinished();
            }

            @Override
            public void onFailure(Throwable thrwbl) {
                DefaultExecutor.this.markAsFinished();
                throw new RuntimeException("Failed");
            }
        });
    }

    private List<ListenableFuture<Response>> getFutures(
            List<OperationResultPair> operationResultPairs) {

        List<ListenableFuture<Response>> futures = new ArrayList<>();
        for (OperationResultPair pair : operationResultPairs) {
            futures.add(pair.getResponse());
        }

        return futures;
    }

    private void markAsFinished() {
        this.state = State.FINISHED;
        stop();
    }

    enum State {
        IDLE,
        RUNNING,
        WAITING,
        FINISHED;
    }
}
