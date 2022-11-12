package com.nli.probation.entity;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Data
@Document
public class LogWorkEntity {
    @Id
    private int id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int status;

}
