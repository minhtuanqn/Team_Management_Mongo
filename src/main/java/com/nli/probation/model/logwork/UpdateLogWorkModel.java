package com.nli.probation.model.logwork;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateLogWorkModel {

  @NotNull(message = "{logwork_id.null}")
  private String id;

  @NotNull(message = "{logwork_starttime.null}")
  private LocalDateTime startTime;

  @NotNull(message = "{logwork_endtime.null}")
  private LocalDateTime endTime;

  @NotNull(message = "{logwork_status.null}")
  @Range(message = "{logwork_status.range}", min = 0, max = 1)
  private int status;
}
