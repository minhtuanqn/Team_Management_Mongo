package com.nli.probation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Document(collection = "team")
@Data
public class TeamEntity {
    @Transient
    public static final String SEQUENCE_NAME = "team_sequence";

    @Id
    private int id;

    @Field("short_name")
    private String shortName;

    @Field("name")
    private String name;

    @Field("status")
    private int status;

}
