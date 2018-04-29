package com.operapay.core;

/**
 * @author Perfect <perfectm@opay.team>
 */
public interface SuccessCallback<T> {
    void invoke(T result);
}
