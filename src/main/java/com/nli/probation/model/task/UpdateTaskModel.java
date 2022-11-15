package com.nli.probation.model.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class UpdateTaskModel {
    @NotNull(message = "{task_id.null}")
    private int id;

    @NotNull(message = "{task_title.null}")
    @Length(message = "{task_title.length}", min = 1, max = 500)
    private String title;

    @NotNull(message = "{task_description.null}")
    @Length(message = "{task_description.length}", min = 1, max = 1000)
    private String description;

    @NotNull(message = "{task_starttime.null}")
    private LocalDateTime startTime;

    @Range(message = "{task_estimatedtime.range}", min = 0)
    @NotNull(message = "{task_estimatedtime.null}")
    private double estimatedTime;

    private int assigneeId;

    @NotNull(message = "{task_status.null}")
    @Range(message = "{task_status.range}", min = 0, max = 1)
    private int status;
}
