package com.konstanczuk.repository;

import static java.util.Objects.requireNonNull;

import com.konstanczuk.dto.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryMessageRepository implements MessageRepository {

  private HashMap<String, List<Message>> mapByEmail = new HashMap<>();
  private HashMap<String, List<Message>> mapByNumber = new HashMap<>();

  public void save(Message message) {
    requireNonNull(message);
    mapByEmail.computeIfAbsent(message.getEmail(), k -> new ArrayList<>()).add(message);
    mapByNumber.computeIfAbsent(message.getMagicNumber(), k -> new ArrayList<>()).add(message);
  }

  public List<Message> findAllByMagicNumber(String magicNumber) {
    return mapByNumber.get(magicNumber);
  }

  public List<Message> findAllByEmail(String email) {
    return mapByEmail.get(email);
  }

  public void remove(Message message) {}

  public void removeAll() {
    mapByEmail.clear();
    mapByNumber.clear();
  }
}
