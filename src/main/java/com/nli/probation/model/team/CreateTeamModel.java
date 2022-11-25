package com.nli.probation.model.team;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateTeamModel {

  @NotNull(message = "{team_shortname.null}")
  @Length(message = "{team_shortname.length}", min = 1, max = 100)
  private String shortName;

  @NotNull(message = "{team_name.null}")
  @Length(message = "{team_name.length}", min = 1, max = 200)
  private String name;
}
