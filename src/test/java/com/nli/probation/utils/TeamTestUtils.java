package com.nli.probation.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nli.probation.constant.EntityStatusEnum.TeamStatusEnum;
import com.nli.probation.model.team.CreateTeamModel;
import com.nli.probation.model.team.TeamModel;
import com.nli.probation.model.team.UpdateTeamModel;
import java.util.List;

public class TeamTestUtils {
  /**
   * Create mock team
   * @return mock team model
   */
  public static TeamModel createTeamModel() {
    TeamModel teamModel = new TeamModel();
    teamModel.setId(1);
    teamModel.setName("LexisNexis");
    teamModel.setShortName("LNI");
    teamModel.setStatus(TeamStatusEnum.ACTIVE.ordinal());
    return teamModel;
  }

  /**
   * Create mock create team
   * @return mock create team model
   */
  public static CreateTeamModel createCreateTeamModel() {
    CreateTeamModel createTeamModel = new CreateTeamModel();
    createTeamModel.setName("LexisNexis");
    createTeamModel.setShortName("LNI");
    return createTeamModel;
  }

  /**
   * Create mock update team
   * @return mock update team model
   */
  public static UpdateTeamModel createUpdateTeamModel() {
    UpdateTeamModel updateTeamModel = new UpdateTeamModel();
    updateTeamModel.setId(1);
    updateTeamModel.setName("LexisNexis");
    updateTeamModel.setShortName("LNI");
    updateTeamModel.setStatus(TeamStatusEnum.ACTIVE.ordinal());
    return updateTeamModel;
  }

  /**
   * Campare two team model
   * @param expected
   * @param actual
   * @return true or false
   */
  public static boolean compareTwoTeam(TeamModel expected, TeamModel actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getShortName(), actual.getShortName());
    assertEquals(expected.getStatus(), actual.getStatus());
    return true;
  }

  /**
   * Compare two list of team
   * @param expectedList
   * @param actualList
   * @return true or false
   */
  public static boolean compareTwoTeamList(List<TeamModel> expectedList, List<TeamModel> actualList) {
    assertEquals(expectedList.size(), actualList.size());
    for (TeamModel teamModel : expectedList) {
      compareTwoTeam(teamModel, actualList.get(expectedList.indexOf(teamModel)));
    }
    return true;
  }
}
