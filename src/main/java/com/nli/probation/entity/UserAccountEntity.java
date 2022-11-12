package com.nli.probation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Document(collection = "user_account")
@Data
public class UserAccountEntity {

    @Transient
    public static final String SEQUENCE_NAME = "account_sequence";

    @Id
    private int id;

    @Field("name")
    private String name;

    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("status")
    private  int status;

    @Field("team_id")
    private int teamId;

    @Field("office_id")
    private int officeId;

    @Field("role_id")
    private int roleId;

}
