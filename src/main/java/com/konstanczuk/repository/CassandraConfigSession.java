package com.konstanczuk.repository;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfigSession {

  public Session session() {
    String serverIP = "127.0.0.1";
    String keyspace = "test";

    Cluster cluster = Cluster.builder().withoutJMXReporting().addContactPoints(serverIP).build();

    return cluster.connect(keyspace);
  }
}
