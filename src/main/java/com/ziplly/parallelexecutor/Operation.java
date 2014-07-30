package com.ziplly.parallelexecutor;

/**
 * Interface describing any operation that needs to be executed in parallel.
 * You can use a chain of operations using {@link OperationBuilder} class and
 * hand it over to {@link ParallelExecutor} for parallelizing the individual
 * operations.
 * Usage
 *  OperationBuilder builder = OperationBuilder.create()
 *      .add(new Operation<Response<?>>() {
 *          @Override
 *          public Integer execute(ExecutionContext ctx) {
 *              ...
 *          }
 *      }
 *      .add(...)
 * 
 *  ParallelExecutor executor = DefaultExecutor.create();
 *  executor.run(builder, new Callback() {
 *      @Override
 *      public void onSuccess(ResultContainer result) {
 *          ...
 *      }
 * 
 *      @Override
 *      public void onFailure(Throwable th) {   
 *          ...
 *      }
 *  });
 * 
 * @author shaan
 * @param <V> type of the Response object
 */
public interface Operation<V extends Response<?>> {

    public V execute(ExecutionContext ctx);
}
