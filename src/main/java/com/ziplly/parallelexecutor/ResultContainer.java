package com.ziplly.parallelexecutor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author shaan
 */
public class ResultContainer {
    private final Map<Operation, Response> responseMap = new LinkedHashMap<>();
    
    public void add(Operation operation, Response<?> response) {
        Preconditions.checkNotNull(operation);
        Preconditions.checkNotNull(response);
        responseMap.put(operation, response);
    }
    
    public Set<Response> getResponses() {
        return Sets.newHashSet(responseMap.values());
    }
    
    public Set<Operation> getOperations() {
        return Sets.newHashSet(responseMap.keySet());
    }
    
    public Response getResponse(Operation operation) {
        Preconditions.checkNotNull(operation);
        return responseMap.get(operation);
    }

    public void addAll(List<Response> responses) {
        for (Response response : responses) {
        }
    }
}
