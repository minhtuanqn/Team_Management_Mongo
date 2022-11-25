package com.nli.probation.model.logwork;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogWorkModel {

  private String id;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private int status;

}
