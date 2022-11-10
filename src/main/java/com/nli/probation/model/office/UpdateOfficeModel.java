package com.nli.probation.model.office;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class UpdateOfficeModel {

    @NotNull(message = "{office_id.null}")
    private int id;

    @NotNull(message = "{office_name.null}")
    @Length(message = "{office_name.length}", min = 1, max = 100)
    private String name;

    @NotNull(message = "{office_location.null}")
    @Length(message = "{office_location.length}", min = 1, max = 100)
    private String location;

    @NotNull(message = "{office_status.null}")
    private int status;
}
