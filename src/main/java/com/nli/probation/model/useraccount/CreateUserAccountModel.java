package com.nli.probation.model.useraccount;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateUserAccountModel {

  @NotNull(message = "{account_name.null}")
  private String name;

  @NotNull(message = "{account_email.null}")
  private String email;

  @NotNull(message = "{account_phone.null}")
  private String phone;

  private int teamId;

  @NotNull(message = "{office_id.null}")
  private int officeId;

  @NotNull(message = "{role_id.null}")
  private int roleId;
}
