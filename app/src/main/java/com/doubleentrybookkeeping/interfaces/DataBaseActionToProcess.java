package com.doubleentrybookkeeping.interfaces;

@FunctionalInterface
public interface DataBaseActionToProcess<O, S, B, H> {
    boolean run(O o, S s, B b, H h);
}