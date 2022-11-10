package com.nli.probation.model.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class UpdateTeamModel {

    @NotNull(message = "{team_id.null}")
    private int id;

    @NotNull(message = "{team_shortname.null}")
    @Length(message = "{team_shortname.length}", min = 1, max = 100)
    private String shortName;

    @NotNull(message = "{team_name.null}")
    @Length(message = "{team_name.length}", min = 1, max = 200)
    private String name;

    @NotNull(message = "{team_status.null}")
    private int status;
}
