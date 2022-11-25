package com.nli.probation.model.task;

import com.nli.probation.model.logwork.LogWorkModel;
import com.nli.probation.model.useraccount.UserAccountModel;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskModel {

  private int id;

  private String title;

  private String description;

  private LocalDateTime startTime;

  private double estimatedTime;

  private UserAccountModel assignee;

  private List<LogWorkModel> logWorkModels;

  private double actualTime;

  private int status;
}
