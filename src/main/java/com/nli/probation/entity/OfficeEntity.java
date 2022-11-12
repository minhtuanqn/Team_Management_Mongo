package com.nli.probation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor @AllArgsConstructor @Getter  @Setter
@Document(collection = "office")
@Data
public class OfficeEntity {

    @Transient
    public static final String SEQUENCE_NAME = "office_sequence";

    @Id
    private int id;

    @Field("name")
    private String name;

    @Field("location")
    private String location;

    @Field("status")
    private int status;

}
