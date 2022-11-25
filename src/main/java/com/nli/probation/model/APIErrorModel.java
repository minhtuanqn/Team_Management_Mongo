package com.nli.probation.model;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for handle error api
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class APIErrorModel {

  private LocalDateTime time;

  private String error;

  private Map<String, String> message;
}
