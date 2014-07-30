package com.ziplly.parallelexecutor;

import com.ziplly.parallelexecutor.annotation.Execute;

/**
 * @author shaan
 */
public class OperationDefinition {

    @Execute
    public Response<Integer> print() {
        System.out.println("print erica!");
        return new Response<>(new Integer(0));
    }
    
    @Execute
    public Response<Integer> print2() {
        System.out.println("print erica!");
        return new Response<>(new Integer(2));
    }
}
