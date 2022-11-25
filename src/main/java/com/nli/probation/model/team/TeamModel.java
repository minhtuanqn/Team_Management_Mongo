package com.nli.probation.model.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamModel {

  private int id;

  private String shortName;

  private String name;

  private int status;
}
