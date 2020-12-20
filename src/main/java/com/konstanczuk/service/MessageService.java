package com.konstanczuk.service;

import com.konstanczuk.dto.Message;
import com.konstanczuk.exceptions.ResouceNotFoundException;
import com.konstanczuk.repository.MessageRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  private MessageRepository messageRepository;
  private JavaMailSender javaMailSender;

  public MessageService(MessageRepository messageRepository, JavaMailSender javaMailSender) {
    this.messageRepository = messageRepository;
    this.javaMailSender = javaMailSender;
  }

  public Message saveMessage(Message message) {
    if (message.getUuid().isEmpty()) {
      message.setUuid(UUID.randomUUID().toString());
    }
    messageRepository.save(message);
    return message;
  }

  public void sendEmails(String magicNumber) {

    List<Message> messagesToSend = messageRepository.findAllByMagicNumber(magicNumber);
    if (messagesToSend.size() == 0) {
      throw new ResouceNotFoundException("There is no messages with number: " + magicNumber);
    }
    for (Message message : messagesToSend) {
      SimpleMailMessage helper = new SimpleMailMessage();

      helper.setTo(message.getEmail());
      helper.setText(message.getContent());
      helper.setSubject(message.getTitle());

      javaMailSender.send(helper);
      messageRepository.remove(message);
    }
  }

  public List<Message> getAllMessagesByEmail(String email) {

    List<Message> messages = messageRepository.findAllByEmail(email);
    if (messages.size() == 0) {
      throw new ResouceNotFoundException("There is no messages with email: " + email);
    }
    return messages;
  }
}
