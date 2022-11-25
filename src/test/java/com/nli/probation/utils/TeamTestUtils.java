package com.nli.probation.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nli.probation.MockConstants;
import com.nli.probation.entity.TeamEntity;
import com.nli.probation.model.team.CreateTeamModel;
import com.nli.probation.model.team.TeamModel;
import com.nli.probation.model.team.UpdateTeamModel;
import java.util.List;

public class TeamTestUtils {

  /**
   * Create mock team
   *
   * @return mock team model
   */
  public static TeamModel createTeamModel() {
    TeamModel teamModel = new TeamModel();
    teamModel.setId(MockConstants.TEAM_ID);
    teamModel.setName(MockConstants.TEAM_NAME);
    teamModel.setShortName(MockConstants.TEAM_SHORT_NAME);
    teamModel.setStatus(MockConstants.TEAM_STATUS);
    return teamModel;
  }

  /**
   * Create mock create team
   *
   * @return mock create team model
   */
  public static CreateTeamModel createCreateTeamModel() {
    CreateTeamModel createTeamModel = new CreateTeamModel();
    createTeamModel.setName(MockConstants.TEAM_NAME);
    createTeamModel.setShortName(MockConstants.TEAM_SHORT_NAME);
    return createTeamModel;
  }

  /**
   * Create mock update team
   *
   * @return mock update team model
   */
  public static UpdateTeamModel createUpdateTeamModel() {
    UpdateTeamModel updateTeamModel = new UpdateTeamModel();
    updateTeamModel.setId(MockConstants.TEAM_ID);
    updateTeamModel.setName(MockConstants.TEAM_NAME);
    updateTeamModel.setShortName(MockConstants.TEAM_SHORT_NAME);
    updateTeamModel.setStatus(MockConstants.TEAM_STATUS);
    return updateTeamModel;
  }

  /**
   * Create mock team entity
   *
   * @return mock team entity
   */
  public static TeamEntity createTeamEntity() {
    TeamEntity teamEntity = new TeamEntity();
    teamEntity.setId(MockConstants.TEAM_ID);
    teamEntity.setName(MockConstants.TEAM_NAME);
    teamEntity.setShortName(MockConstants.TEAM_SHORT_NAME);
    teamEntity.setStatus(MockConstants.TEAM_STATUS);
    return teamEntity;
  }

  /**
   * Campare two team model
   *
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
   *
   * @param expectedList
   * @param actualList
   * @return true or false
   */
  public static boolean compareTwoTeamList(List<TeamModel> expectedList,
      List<TeamModel> actualList) {
    assertEquals(expectedList.size(), actualList.size());
    for (TeamModel teamModel : expectedList) {
      compareTwoTeam(teamModel, actualList.get(expectedList.indexOf(teamModel)));
    }
    return true;
  }
}
