package com.nli.probation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Document(collection = "database_sequences")
@Data
public class DBSequencesEntity {
    @Id
    private String id;

    private int seq;
}
