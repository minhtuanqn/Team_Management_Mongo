package com.nli.probation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class RequestPaginationModel {
    private int index;
    private int limit;
    private String sortBy;
    private String sortType;
}
