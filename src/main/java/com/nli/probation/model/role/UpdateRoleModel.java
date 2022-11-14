package com.nli.probation.model.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class UpdateRoleModel {
    @NotNull(message = "{role_id.null}")
    private int id;

    @NotNull(message = "{role_shortname.null}")
    @Length(message = "{role_shortname.length}", min = 1, max = 100)
    private String shortName;

    @NotNull(message = "{role_name.null}")
    @Length(message = "{role_name.length}", min = 1, max = 200)
    private String name;

    @NotNull(message = "{role_status.null}")
    @Range(message = "{role_status.range}", min = 0, max = 1)
    private int status;
}
