package com.konstanczuk.repository;

public interface CustomizedSave<T> {
  <S extends T> S save(S entity, int ttl);
}
