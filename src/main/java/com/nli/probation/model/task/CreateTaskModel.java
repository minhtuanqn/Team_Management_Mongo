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
public class CreateTaskModel {

    @NotNull(message = "{task_title.null}")
    @Length(message = "{task_title.length}", min = 1, max = 500)
    private String title;

    @NotNull(message = "{task_description.null}")
    @Length(message = "{task_description.length}", min = 1, max = 1000)
    private String description;

    @NotNull(message = "{task_starttime.null}")
    private LocalDateTime startTime;

    @NotNull(message = "{task_estimatedtime.null}")
    @Range(message = "{task.estimatedtime.range}", min = 0)
    private double estimatedTime;

    private int assigneeId;

}
