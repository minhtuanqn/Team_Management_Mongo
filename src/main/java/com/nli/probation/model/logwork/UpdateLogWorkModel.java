package com.nli.probation.model.logwork;

import com.nli.probation.model.task.TaskModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class UpdateLogWorkModel {

    @NotNull(message = "{logwork_id.null}")
    private int id;

    @NotNull(message = "{logwork_starttime.null}")
    private LocalDateTime startTime;

    @NotNull(message = "{logwork_endtime.null}")
    private LocalDateTime endTime;

    @NotNull(message = "{logwork_status.null}")
    private int status;

    @NotNull(message = "{task_id.null}")
    private int taskId;
}
