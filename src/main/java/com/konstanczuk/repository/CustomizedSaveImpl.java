package com.konstanczuk.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;

public class CustomizedSaveImpl<T> implements CustomizedSave<T> {

  @Autowired
  private CassandraOperations operations;

  @Override
  public <S extends T> S save(S entity, int ttl) {
    InsertOptions insertOptions = org.springframework.data.cassandra.core.InsertOptions.builder().ttl(ttl).build();
    operations.insert(entity, insertOptions);
    return entity;
  }
}
