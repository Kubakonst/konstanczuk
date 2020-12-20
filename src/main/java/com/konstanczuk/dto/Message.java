package com.konstanczuk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {

  private String uuid;
  private String email;
  private String title;
  private String content;

  @JsonProperty("magic_number")
  private String magicNumber;

  public String getUuid() {
    if (uuid == null || uuid.isEmpty()) {
      return "Empty uuid";
    }
    return uuid;
  }
}
