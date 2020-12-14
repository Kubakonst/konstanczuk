package com.konstanczuk.controller;

import com.konstanczuk.dto.Message;
import com.konstanczuk.service.MessageService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MessageController {

  @Autowired
  private MessageService messageService;

  @PostMapping("/message")
  public ResponseEntity<Message> addMessage(@RequestBody Message message){
    message.setUuid(UUID.randomUUID().toString());
    messageService.saveMessage(message);
    return ResponseEntity.ok().body(message);
  }

  @PostMapping("/send")
  public ResponseEntity sendMessage(@RequestBody Integer magicNumber){
    messageService.sendEmails(magicNumber);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/messages/{email}")
  public ResponseEntity<Slice<Message>> getAllMessage(@PathVariable String email){
    Slice<Message> messages = messageService.getAllMessagesByEmail(email);
    return ResponseEntity.ok().body(messages);
  }

}
