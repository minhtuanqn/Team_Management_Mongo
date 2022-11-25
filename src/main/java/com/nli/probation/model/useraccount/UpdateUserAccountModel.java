package com.nli.probation.model.useraccount;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserAccountModel {

  @NotNull(message = "{account_id.null}")
  private int id;

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

  @NotNull(message = "{account_status.null}")
  @Range(message = "{account_status.range}", min = 0, max = 1)
  private int status;
}
