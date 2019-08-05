package net.noboard.fastconverter;

public interface Pipeline extends LifeCycle {
    void add(LifeCycle lifeCycle);
}
