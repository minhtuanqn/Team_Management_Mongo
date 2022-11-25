package com.nli.probation.model.useraccount;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nli.probation.model.office.OfficeModel;
import com.nli.probation.model.role.RoleModel;
import com.nli.probation.model.team.TeamModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAccountModel {

  private int id;

  private String name;

  private String email;

  private String phone;

  private int status;

  @JsonProperty("team")
  private TeamModel teamModel;

  @JsonProperty("office")
  private OfficeModel officeModel;

  @JsonProperty("role")
  private RoleModel roleModel;
}
