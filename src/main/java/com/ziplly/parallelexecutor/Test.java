package com.ziplly.parallelexecutor;

import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * @author shaan
 */
public class Test {

    public void run() throws InterruptedException, ExecutionException, InstantiationException, IllegalAccessException {
        final OperationA operationA1 = new OperationA(777);
        final OperationA operationA2 = new OperationA(999);
        final OperationA operationA3 = new OperationA(8888);
        OperationBuilder builder = OperationBuilder.create()
                .add(operationA1)
                .add(operationA2)
                .add(operationA3)
                .register(OperationDefinition.class);

        ParallelExecutor executor = DefaultExecutor.create();
        executor.run(builder, new Callback() {

            @Override
            public void success(ResultContainer result) {
                StringBuilder sb = new StringBuilder();
                for (Operation operation : result.getOperations()) {
                    sb.append(String.format("\nOperation %s, response %d",
                            operation, result.getResponse(operation).getOutput()));
                }

                System.out.println("----------------- RESULT ----------------\n"
                        + sb.toString());
            }

            @Override
            public void failure(Throwable th) {
                System.out.println("FAILURE");
            }
        });

        ResultContainer result = executor.get();
        for (Response r : result.getResponses()) {
            System.out.println(String.format("O = " + r.getOutput()));
        }
    }

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.run();
        System.out.println("Finished main");
    }

    private class OperationA extends AbstractOperation<Response<Integer>> {

        private final int N;

        OperationA(int N) {
            this.N = N;
        }

        @Override
        public Response<Integer> execute(ExecutionContext ctx) {
            long startingTime = System.currentTimeMillis();
            int sum = 0;
            Date d = new Date();
            for (int k = 0; k < N; k++) {
                for (int i = 0; i < N; i++) {
                    sum += d.getTime() % (i + 1);
                }
            }
            long elapsedTime = System.currentTimeMillis() - startingTime;
            System.out.println(String.format("Operation (N=%d), elapsed time = %d", N, elapsedTime));
            return new Response<>(sum);
        }
    }
}
