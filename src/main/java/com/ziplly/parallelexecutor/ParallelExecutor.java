package com.ziplly.parallelexecutor;

import java.util.concurrent.ExecutionException;

/**
 * Main interface for submitting tasks to be run in parallel. It provides a way
 * to register a callback when the tasks are all finished. If needed you can
 * also make a blocking call
 *
 * @author shaan
 */
public interface ParallelExecutor {

    public void run(OperationBuilder builder, Callback callback);

    public ResultContainer get() throws InterruptedException, ExecutionException;
}
