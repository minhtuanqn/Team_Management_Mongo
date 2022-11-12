package com.nli.probation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Document(collection = "role")
@Data
public class RoleEntity {
    @Transient
    public static final String SEQUENCE_NAME = "role_sequence";

    @Id
    @Field("id")
    private int id;

    @Field("short_name")
    private String shortName;

    @Field("name")
    private String name;

    @Field("status")
    private int status;
    
}
