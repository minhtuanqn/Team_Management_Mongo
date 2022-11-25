package com.nli.probation.unittest.service;

import static com.nli.probation.utils.TeamTestUtils.compareTwoTeam;
import static com.nli.probation.utils.TeamTestUtils.compareTwoTeamList;
import static com.nli.probation.utils.TeamTestUtils.createCreateTeamModel;
import static com.nli.probation.utils.TeamTestUtils.createTeamModel;
import static com.nli.probation.utils.TeamTestUtils.createUpdateTeamModel;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nli.probation.MockConstants;
import com.nli.probation.constant.EntityStatusEnum.TeamStatusEnum;
import com.nli.probation.customexception.DuplicatedEntityException;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.entity.TeamEntity;
import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.team.CreateTeamModel;
import com.nli.probation.model.team.TeamModel;
import com.nli.probation.model.team.UpdateTeamModel;
import com.nli.probation.repository.TeamRepository;
import com.nli.probation.service.SequenceGeneratorService;
import com.nli.probation.service.TeamService;
import com.nli.probation.utils.TestUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class TeamServiceTest {

  private final TeamRepository teamRepository = Mockito.mock(TeamRepository.class);
  private final ModelMapper modelMapper = new ModelMapper();
  private final SequenceGeneratorService sequenceGeneratorService = Mockito.mock(
      SequenceGeneratorService.class);

  /**
   * Create new team and save successfully
   */
  @Test
  void when_saveTeam_thenSaveSuccessfully() {

    TeamModel teamModel = createTeamModel();
    TeamEntity savedEntity = modelMapper.map(teamModel, TeamEntity.class);
    when(teamRepository.existsByName(anyString())).thenReturn(false);
    when(teamRepository.existsByShortName(anyString())).thenReturn(false);
    when(teamRepository.save(any())).thenReturn(savedEntity);

    TeamModel expectedModel = createTeamModel();
    CreateTeamModel paramModel = createCreateTeamModel();
    TeamModel actualModel = new TeamService(teamRepository, modelMapper,
        sequenceGeneratorService).createTeam(paramModel);
    assertTrue(compareTwoTeam(expectedModel, actualModel));
  }

  /**
   * Create new team but name of team has been existed
   */
  @Test
  void when_saveTeamWithExistName_thenThrowDuplicatedEntityException() {
    when(teamRepository.existsByName(anyString())).thenReturn(true);

    CreateTeamModel paramModel = createCreateTeamModel();
    TeamService teamService = new TeamService(teamRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(DuplicatedEntityException.class, () -> teamService.createTeam(paramModel));
  }

  /**
   * Create new team but short name of team has been existed
   */
  @Test
  void when_saveTeamWithExistShortName_thenThrowDuplicatedEntityException() {
    when(teamRepository.existsByShortName(anyString())).thenReturn(true);

    CreateTeamModel paramModel = createCreateTeamModel();
    TeamService teamService = new TeamService(teamRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(DuplicatedEntityException.class, () -> teamService.createTeam(paramModel));
  }

  /**
   * Delete a team and delete successfully
   */
  @Test
  void when_deleteExistTeam_thenDeleteSuccessfully() {
    TeamModel teamModel = createTeamModel();

    TeamEntity savedEntity = modelMapper.map(teamModel, TeamEntity.class);
    Optional<TeamEntity> optional = Mockito.mock(Optional.class);
    when(teamRepository.findById(anyInt())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(savedEntity);
    when(teamRepository.save(any())).thenReturn(savedEntity);

    TeamModel expectedModel = createTeamModel();
    expectedModel.setStatus(TeamStatusEnum.DISABLE.ordinal());

    TeamModel actualModel = new TeamService(teamRepository, modelMapper,
        sequenceGeneratorService).deleteTeamById(MockConstants.TEAM_ID);
    assertTrue(compareTwoTeam(expectedModel, actualModel));
  }

  /**
   * Delete team but can not find team by id
   */
  @Test
  void when_deleteNotExistTeam_thenThrowNoSuchEntityException() {
    Optional<TeamEntity> optional = Mockito.mock(Optional.class);
    when(teamRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenThrow(NoSuchEntityException.class);

    TeamService teamService = new TeamService(teamRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(NoSuchEntityException.class,
        () -> teamService.deleteTeamById(MockConstants.NOT_FOUND_TEAM_ID));
  }

  /**
   * Delete role but status of role is disable
   */
  @Test
  void when_deleteTeamWithDisableStatus_thenThrowNoSuchEntityException() {
    TeamEntity foundTeam = modelMapper.map(createTeamModel(), TeamEntity.class);
    foundTeam.setStatus(TeamStatusEnum.DISABLE.ordinal());

    Optional<TeamEntity> optional = Mockito.mock(Optional.class);
    when(teamRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(foundTeam);

    TeamService teamService = new TeamService(teamRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(NoSuchEntityException.class,
        () -> teamService.deleteTeamById(MockConstants.TEAM_ID));
  }

  /**
   * Find team successfully
   */
  @Test
  void when_findExistTeam_thenReturnModelSuccessfully() {
    TeamModel teamModel = createTeamModel();

    TeamEntity foundTeam = modelMapper.map(teamModel, TeamEntity.class);
    Optional<TeamEntity> optional = Mockito.mock(Optional.class);
    when(teamRepository.findById(anyInt())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(foundTeam);

    TeamModel expectedModel = createTeamModel();

    TeamModel actualModel = new TeamService(teamRepository, modelMapper,
        sequenceGeneratorService).findTeamById(
        MockConstants.TEAM_ID);
    assertTrue(compareTwoTeam(expectedModel, actualModel));
  }

  /**
   * Update an existed team and update successfully
   */
  @Test
  void when_updateExistTeam_thenUpdateSuccessfully() {
    UpdateTeamModel updateTeamModel = createUpdateTeamModel();

    TeamEntity savedEntity = modelMapper.map(updateTeamModel, TeamEntity.class);
    Optional<TeamEntity> optional = Mockito.mock(Optional.class);
    when(teamRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(
        modelMapper.map(createTeamModel(), TeamEntity.class));
    when(teamRepository.existsByNameAndIdNot(anyString(), anyInt())).thenReturn(false);
    when(teamRepository.existsByShortNameAndIdNot(anyString(), anyInt())).thenReturn(false);
    when(teamRepository.save(any())).thenReturn(savedEntity);

    TeamModel expectedModel = createTeamModel();

    TeamModel actualModel = new TeamService(teamRepository, modelMapper,
        sequenceGeneratorService).updateTeam(createUpdateTeamModel());
    assertTrue(compareTwoTeam(expectedModel, actualModel));
  }

  /**
   * Find teams like name or short name then return result
   */
  @Test
  void when_findTeamLikeNameOrShortNameSortByAsc_thenReturnResourceOfListOfRolesByAsc() {
    List<TeamEntity> entityList = new ArrayList<>();
    entityList.add(modelMapper.map(createTeamModel(), TeamEntity.class));
    Page<TeamEntity> entityPage = new PageImpl<>(entityList);

    when(teamRepository.findAll(any(), (Pageable) any())).thenReturn(entityPage);

    TeamService teamService = new TeamService(teamRepository, modelMapper,
        sequenceGeneratorService);
    ResourceModel<TeamModel> actualResource = teamService.
        searchTeams(MockConstants.SEARCH_VALUE,
            new RequestPaginationModel(MockConstants.INDEX, MockConstants.LIMIT,
                MockConstants.SORT_BY, MockConstants.SORT_TYPE));

    List<TeamModel> modelList = new ArrayList<>();
    TeamModel expectModel = createTeamModel();
    modelList.add(expectModel);
    TestUtils<TeamModel> testUtils = new TestUtils<>();
    ResourceModel<TeamModel> expectedResource = testUtils
        .createResourceModel(MockConstants.SEARCH_VALUE, MockConstants.SORT_TYPE,
            MockConstants.SORT_BY,
            MockConstants.TOTAL_RESULT, MockConstants.TOTAL_PAGE, MockConstants.INDEX,
            MockConstants.LIMIT, modelList);
    assertTrue(testUtils.compareTwoResourceInformation(expectedResource, actualResource));
    assertTrue(compareTwoTeamList(expectedResource.getData(), actualResource.getData()));
  }
}
