package com.nli.probation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor @AllArgsConstructor @Getter  @Setter
@Document(collection = "office")
@Data
public class OfficeEntity {

    @Transient
    public static final String SEQUENCE_NAME = "office_sequence";

    @Id
    private int id;

    private String name;

    private String location;

    private int status;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "officeEntity")
//    private Set<UserAccountEntity> userList;
}
