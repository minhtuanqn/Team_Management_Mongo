package com.nli.probation.unittest.service;

import static com.nli.probation.utils.RoleTestUtils.compareTwoRole;
import static com.nli.probation.utils.RoleTestUtils.createCreateRoleModel;
import static com.nli.probation.utils.RoleTestUtils.createRoleModel;
import static com.nli.probation.utils.TeamTestUtils.compareTwoTeam;
import static com.nli.probation.utils.TeamTestUtils.createCreateTeamModel;
import static com.nli.probation.utils.TeamTestUtils.createTeamModel;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nli.probation.entity.RoleEntity;
import com.nli.probation.entity.TeamEntity;
import com.nli.probation.model.role.CreateRoleModel;
import com.nli.probation.model.role.RoleModel;
import com.nli.probation.model.team.CreateTeamModel;
import com.nli.probation.model.team.TeamModel;
import com.nli.probation.repository.RoleRepository;
import com.nli.probation.repository.TeamRepository;
import com.nli.probation.service.RoleService;
import com.nli.probation.service.SequenceGeneratorService;
import com.nli.probation.service.TeamService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

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
}
