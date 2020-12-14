package com.konstanczuk.service;

import com.konstanczuk.dto.Message;
import com.konstanczuk.exceptions.ResouceNotFoundException;
import com.konstanczuk.repository.MessageRepository;
import java.util.List;
import java.util.Properties;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  @Autowired private MessageRepository messageRepository;

  public MessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  public Message saveMessage(Message message) {
    messageRepository.save(message, 300);
    return message;
  }

  public void sendEmails(Integer magicNumber) {

    List<Message> messagesToSend =
        messageRepository
            .findAllByMagicNumber(magicNumber)
            .orElseThrow(
                () ->
                    new ResouceNotFoundException(
                        "There is no messages with number: " + magicNumber));
    Session session = prepareEmailProperties();
    for (Message message : messagesToSend) {
      try {
        MimeMessage mes = new MimeMessage(session);
        mes.setFrom("exampleEmail@example.com");
        mes.setRecipient(RecipientType.TO, new InternetAddress(message.getEmail()));
        mes.setSubject(message.getTitle());
        mes.setText(message.getContent());
        Transport.send(mes);
        messageRepository.delete(message);
      } catch (MessagingException mex) {
        mex.printStackTrace();
      }
    }
  }

  public Slice<Message> getAllMessagesByEmail(String email) {
    Pageable pageRequest = CassandraPageRequest.of(0,5);
    return messageRepository
        .findAllByEmail(email, pageRequest)
        .orElseThrow(
            () -> new ResouceNotFoundException("There is no messages with email: " + email));
  }

  private Session prepareEmailProperties() {
    String host = "localhost";
    Properties properties = System.getProperties();
    properties.setProperty("mail.smtp.host", host);
    return Session.getDefaultInstance(properties);
  }

}
