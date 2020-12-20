package com.konstanczuk.repository;

import com.konstanczuk.dto.Message;
import java.util.List;

public interface MessageRepository {

  void save(Message message);

  List<Message> findAllByMagicNumber(String magicNumber);

  List<Message> findAllByEmail(String email);

  void remove(Message message);

  void removeAll();
}
