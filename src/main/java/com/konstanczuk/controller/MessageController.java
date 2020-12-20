package com.konstanczuk.controller;

import com.konstanczuk.dto.Message;
import com.konstanczuk.service.MessageService;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api")
public class MessageController {

  private static Logger logger = LoggerFactory.getLogger(MessageController.class);

  private MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping("/message")
  public ResponseEntity<Message> addMessage(@RequestBody Message message) {
    logger.info("Successful create message with emailAddress: {}", message.getEmail());
    messageService.saveMessage(message);
    return ResponseEntity.ok().body(message);
  }

  @PostMapping("/send")
  public ResponseEntity sendMessage(@RequestBody Message message) {
    messageService.sendEmails(message.getMagicNumber());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/messages/{email}")
  public ResponseEntity<List<Message>> getAllMessage(@PathVariable String email) {
    List<Message> messages = messageService.getAllMessagesByEmail(email);
    logger.info("Successful retur message with emailAddress: {}", messages);
    return ResponseEntity.ok().body(messages);
  }
}
