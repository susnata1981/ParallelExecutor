ParallelExecutor
================

Library to create operations that needs to be run in parallel and received a callback with the joined results.

Often times while developing applications we encounter situations where we've to make multiple queries to the database or multiple isolated units of computation in response to a web/rpc request. Wouldn't it be nice if we can parallelize those operations?

Isolated individual computations/lookups could be done in parallel and then their results can be joined back for creating the final response. Java threads can do the job for us but there's quite a bit of boiler plate code everytime we need to do this - create callables/create executor service/submit jobs/wait for all futures to complete either using polling or some fancy callback mechanism. This library tends to take the pain of having to write this boilerplate code - 
just annotate the methods that want to be run in parallel or if you like builder pattern just create a pipeline of operations and the framework would take care of the result. You'll need to register a callback that would be invoked once all the operations complete. There's an api to make a blocking call as well to wait for the response.

If you've any ideas or would like to extend the framework, please get in touch with me - susnata at gmail dot com.

