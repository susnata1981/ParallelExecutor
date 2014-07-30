package com.ziplly.parallelexecutor;

import java.util.UUID;

/**
 * Base class implementation for operation interface.
 * 
 * @author shaan
 * @param <V> type of Response object
 */
public abstract class AbstractOperation<V extends Response<?>> implements Operation<V> {
    private final UUID uuid;

    public AbstractOperation() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUUID() {
        return uuid;
    }
    
    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        
        if (!(o instanceof AbstractOperation)){
            return false;
        }
        
        AbstractOperation ao = (AbstractOperation)o;
        return uuid.equals(ao.uuid); 
    }
    
    @Override
    public String toString() {
        return "Operation [ "+uuid+" ]";
    }
}
