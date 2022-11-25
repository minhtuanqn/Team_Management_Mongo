package com.nli.probation.model.role;

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
public class CreateRoleModel {

  @NotNull(message = "{role_shortname.null}")
  @Length(message = "{role_shortname.length}", min = 1, max = 100)
  private String shortName;

  @NotNull(message = "{role_name.null}")
  @Length(message = "{role_name.length}", min = 1, max = 200)
  private String name;
}
