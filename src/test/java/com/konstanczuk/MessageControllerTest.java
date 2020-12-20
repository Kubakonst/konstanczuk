package com.konstanczuk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konstanczuk.controller.MessageController;
import com.konstanczuk.dto.Message;
import com.konstanczuk.service.MessageService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(controllers = MessageController.class)
class MessageControllerTest {

  @MockBean MessageService messageService;
  @Autowired MessageController messageController;
  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  @ParameterizedTest
  @MethodSource("messages")
  void shouldCreateMessageWhenValid(Message testMessage) throws Exception {
    // given
    when(messageService.saveMessage(testMessage)).thenReturn(testMessage);
    String body = objectMapper.writeValueAsString(testMessage);
    // when
    String response =
        mockMvc
            .perform(
                post("/api/message").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then
    assertEquals(body, response);
  }

  @ParameterizedTest
  @MethodSource("listOfMessages")
  void returnAllMessagesForGivenEmail(List<Message> testMessages) throws Exception {
    // given
    when(messageService.getAllMessagesByEmail(testMessages.get(0).getEmail()))
        .thenReturn(testMessages);
    String expectedResult = objectMapper.writeValueAsString(testMessages);

    // when
    String response =
        mockMvc
            .perform(
                get("/api/messages/" + testMessages.get(0).getEmail())
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then
    assertEquals(expectedResult, response);
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
                    UUID.randomUUID().toString(), "test1@mail.com", "title1", "content1", "333"),
                new Message(
                    UUID.randomUUID().toString(), "test1@mail.com", "title1", "content1", "333"),
                new Message(
                    UUID.randomUUID().toString(), "test1@mail.com", "title1", "content1", "333"))));
  }
}
