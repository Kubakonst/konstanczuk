package com.konstanczuk;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.konstanczuk.dto.Message;
import com.konstanczuk.repository.InMemoryMessageRepository;
import com.konstanczuk.service.MessageService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  private MessageService messageService;

  @Mock private JavaMailSender javaMailSender;

  @ParameterizedTest
  @MethodSource("messages")
  void shoudDontThrowExceptionDuringSavingMessage(Message message) {
    //when
    messageService = new MessageService(new InMemoryMessageRepository(), javaMailSender);
    //then
    assertDoesNotThrow(
        () -> {
          messageService.saveMessage(message);
        });
  }

  @ParameterizedTest
  @MethodSource("messages")
  void shoudDontThrowExceptionDuringSendingEmails(Message message) {
    //when
    messageService = new MessageService(new InMemoryMessageRepository(), javaMailSender);
    messageService.saveMessage(message);
    //then
    assertDoesNotThrow(
        () -> {
          messageService.sendEmails(message.getMagicNumber());
        });
  }

  @ParameterizedTest
  @MethodSource("listOfMessages")
  void shouldReturnListOfMessagesForGivenEmail(List<Message> messages) {
    //given
    String email = messages.get(0).getEmail();
    //when
    messageService = new MessageService(new InMemoryMessageRepository(), javaMailSender);
    for (Message message:messages){
    messageService.saveMessage(message);}
    //then
    assertEquals(messages, messageService.getAllMessagesByEmail(email));
  }

  private static Stream<Arguments> messages() {
    return Stream.of(
        Arguments.of(
            new Message(UUID.randomUUID().toString(), "test1@mail.com", "title1", "content1", "1")),
        Arguments.of(
            new Message(UUID.randomUUID().toString(), "test2@mail.com", "title2", "content2", "2")),
        Arguments.of(
            new Message(
                UUID.randomUUID().toString(), "test2@mail.com", "title2", "content2", "2")));
  }

  private static Stream<Arguments> listOfMessages() {
    return Stream.of(
        Arguments.of(
            List.of(
                new Message(
                    UUID.randomUUID().toString(), "test1@mail.com", "title1", "content1", "1"),
                new Message(
                    UUID.randomUUID().toString(), "test1@mail.com", "title1", "content1", "1"),
                new Message(
                    UUID.randomUUID().toString(), "test1@mail.com", "title1", "content1", "1"))),
        Arguments.of(
            List.of(
                new Message(
                    UUID.randomUUID().toString(), "test2@mail.com", "title1", "content1", "333"),
                new Message(
                    UUID.randomUUID().toString(), "test2@mail.com", "title1", "content1", "333"),
                new Message(
                    UUID.randomUUID().toString(), "test2@mail.com", "title1", "content1", "333"))));
  }
}
