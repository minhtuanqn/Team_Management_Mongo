package com.nli.probation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Document(collection = "role")
@Data
public class RoleEntity {
    @Transient
    public static final String SEQUENCE_NAME = "role_sequence";

    @Id
    private int id;

    private String shortName;

    private String name;

    private int status;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roleEntity")
//    private Set<UserAccountEntity> userList;
}
