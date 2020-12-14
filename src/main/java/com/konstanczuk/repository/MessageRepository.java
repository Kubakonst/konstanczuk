package com.konstanczuk.repository;

import com.konstanczuk.dto.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CassandraRepository<Message, String>, CustomizedSave<Message> {

  Optional<Slice<Message>> findAllByEmail(String email, Pageable pageable);

  Optional<List<Message>> findAllByMagicNumber(Integer magicNumber);
}
