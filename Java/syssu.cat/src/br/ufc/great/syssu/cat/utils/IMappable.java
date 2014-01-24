package br.ufc.great.syssu.cat.utils;

import java.util.Map;

public interface IMappable<T> {
    Map<String, Object> getMap();
    T getObject();
}
