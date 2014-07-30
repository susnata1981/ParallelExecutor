package com.ziplly.parallelexecutor;

/**
 * @author shaan
 */
public interface Callback {
    public void success(ResultContainer result);
    
    public void failure(Throwable th);
}
