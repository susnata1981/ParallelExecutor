package com.ziplly.parallelexecutor;

/**
 * @author shaan
 */
public class Response<V> {
    private final V output;
    
    public Response(V output) {
        this.output = output;
    }
    
    public V getOutput() {
        return output;
    }
}
