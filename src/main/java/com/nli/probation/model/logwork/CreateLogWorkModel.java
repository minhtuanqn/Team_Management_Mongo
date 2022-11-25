package com.nli.probation.model.logwork;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateLogWorkModel {

  @NotNull(message = "{task_id.null}")
  private int taskId;

  @NotNull(message = "{logwork_starttime.null}")
  private LocalDateTime startTime;

  @NotNull(message = "{logwork_endtime.null}")
  private LocalDateTime endTime;
}
