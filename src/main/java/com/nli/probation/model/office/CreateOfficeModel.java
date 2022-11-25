package com.nli.probation.model.office;

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
public class CreateOfficeModel {

  @NotNull(message = "{office_name.null}")
  @Length(message = "{office_name.length}", min = 1, max = 100)
  private String name;

  @NotNull(message = "{office_location.null}")
  @Length(message = "{office_location.length}", min = 1, max = 100)
  private String location;

}
