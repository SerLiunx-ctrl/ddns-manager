package com.serliunx.ddns.core;

/**
 * @author SerLiunx
 * @since 1.0
 */
public interface Service extends Runnable{

    @Override
    default void run(){
        throw new UnsupportedOperationException();
    }
}
