package br.ufc.great.syssu.cat.utils;

public interface JSONable<T> {
    T getObject();
    String getJSON();
}
