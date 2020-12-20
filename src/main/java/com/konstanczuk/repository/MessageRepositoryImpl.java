package com.konstanczuk.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.konstanczuk.dto.Message;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepositoryImpl implements MessageRepository {
  @Autowired private CassandraConfigSession cassandraConfigSession;

  public MessageRepositoryImpl(CassandraConfigSession cassandraConfigSession) {
    this.cassandraConfigSession = cassandraConfigSession;
  }

  public void save(Message message) {
    String cqlStatementC =
        "INSERT INTO test.message (uuid, email, title, content, magicNumber) "
            + "VALUES ('"
            + message.getUuid()
            + "','"
            + message.getEmail()
            + "','"
            + message.getTitle()
            + "','"
            + message.getContent()
            + "','"
            + message.getMagicNumber()
            + "') USING TTL 300;";
    cassandraConfigSession.session().execute(cqlStatementC);
  }

  public List<Message> findAllByMagicNumber(String magicNumber) {
    List<Message> messages = new ArrayList<>();
    String cqlStatement =
        "SELECT * FROM test.message where magicnumber= '" + magicNumber + "' ALLOW FILTERING;";
    ResultSet rs = cassandraConfigSession.session().execute(cqlStatement);
    for (Row row : rs) {
      Message message =
          new Message(
              row.getString("uuid"),
              row.getString("email"),
              row.getString("title"),
              row.getString("content"),
              row.getString("magicnumber"));
      messages.add(message);
    }
    return messages;
  }

  public List<Message> findAllByEmail(String email) {
    List<Message> messages = new ArrayList<>();
    String cqlStatement =
        "SELECT * FROM test.message where emial= '" + email + "' ALLOW FILTERING;";
    ResultSet rs = cassandraConfigSession.session().execute(cqlStatement);
    for (Row row : rs) {
      Message message =
          new Message(
              row.getString("uuid"),
              row.getString("email"),
              row.getString("title"),
              row.getString("content"),
              row.getString("magicnumber"));
      messages.add(message);
    }
    return messages;
  }

  public void remove(Message message) {
    String cqlStatement =
        "DELETE FROM test.message WHERE uuid= '"
            + message.getUuid()
            + "' and email= '"
            + message.getEmail()
            + "' and magicnumber= '"
            + message.getMagicNumber()
            + "' IF EXISTS;";
    cassandraConfigSession.session().execute(cqlStatement);
  }

  public void removeAll() {
    String cqlStatement = "TRUNCATE test.message;";
    cassandraConfigSession.session().execute(cqlStatement);
  }
}
