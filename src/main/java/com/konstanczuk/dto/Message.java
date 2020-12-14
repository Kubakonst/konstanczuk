package com.konstanczuk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@AllArgsConstructor
@Table
public class Message {

  @PrimaryKey
  private String uuid;
  private String email;
  private String title;
  private String content;
  @JsonProperty("magic_number")
  private String magicNumber;


}
