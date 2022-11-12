package com.nli.probation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Document(collection = "task")
@Data
public class TaskEntity {

    @Transient
    public static final String SEQUENCE_NAME = "task_sequence";

    @Id
    @Field("id")
    private int id;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("start_time")
    private LocalDateTime startTime;

    @Field("estimated_time")
    private double estimatedTime;

    @Field("assignee")
    private int userAccountId;

    @Field("actual_time")
    private double actualTime;

    @Field("status")
    private int status;

    @Field("log_works")
    private List<LogWorkEntity> logWorkList;
}
