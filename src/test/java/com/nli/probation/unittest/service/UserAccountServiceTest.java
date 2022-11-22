package com.nli.probation.unittest.service;

import static com.nli.probation.utils.OfficeTestUtils.createOfficeModel;
import static com.nli.probation.utils.RoleTestUtils.createRoleModel;
import static com.nli.probation.utils.TeamTestUtils.compareTwoTeam;
import static com.nli.probation.utils.TeamTestUtils.createCreateTeamModel;
import static com.nli.probation.utils.TeamTestUtils.createTeamModel;
import static com.nli.probation.utils.UserAccountTestUtils.compareTwoUserAccount;
import static com.nli.probation.utils.UserAccountTestUtils.createCreateUserAccountModel;
import static com.nli.probation.utils.UserAccountTestUtils.createUserAccountModel;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nli.probation.entity.OfficeEntity;
import com.nli.probation.entity.RoleEntity;
import com.nli.probation.entity.TeamEntity;
import com.nli.probation.entity.UserAccountEntity;
import com.nli.probation.model.team.CreateTeamModel;
import com.nli.probation.model.team.TeamModel;
import com.nli.probation.model.useraccount.CreateUserAccountModel;
import com.nli.probation.model.useraccount.UserAccountModel;
import com.nli.probation.repository.OfficeRepository;
import com.nli.probation.repository.RoleRepository;
import com.nli.probation.repository.TeamRepository;
import com.nli.probation.repository.UserAccountRepository;
import com.nli.probation.service.SequenceGeneratorService;
import com.nli.probation.service.TeamService;
import com.nli.probation.service.UserAccountService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.mongodb.core.MongoTemplate;

class UserAccountServiceTest {
  private final UserAccountRepository userAccountRepository = Mockito.mock(UserAccountRepository.class);
  private final TeamRepository teamRepository = Mockito.mock(TeamRepository.class);
  private final RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
  private final OfficeRepository officeRepository = Mockito.mock(OfficeRepository.class);
  private final ModelMapper modelMapper = new ModelMapper();
  private final SequenceGeneratorService sequenceGeneratorService = Mockito.mock(
      SequenceGeneratorService.class);
  private final MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);

  /**
   * Create new user account and save successfully
   */
  @Test
  void when_saveUserAccount_thenSaveSuccessfully() {

    modelMapper.getConfiguration()
        .setMatchingStrategy(MatchingStrategies.STANDARD);
    modelMapper.getConfiguration().setAmbiguityIgnored(true);
    UserAccountModel userAccountModel = createUserAccountModel();
    UserAccountEntity savedEntity = modelMapper.map(userAccountModel, UserAccountEntity.class);
    savedEntity.setId(1);
    savedEntity.setRoleId(1);
    savedEntity.setTeamId(1);
    savedEntity.setOfficeId(1);
    when(userAccountRepository.existsByEmail(anyString())).thenReturn(false);
    when(userAccountRepository.existsByPhone(anyString())).thenReturn(false);

    RoleEntity savedRoleEntity = modelMapper.map(createRoleModel(), RoleEntity.class);
    Optional<RoleEntity> optional = Mockito.mock(Optional.class);
    when(roleRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(savedRoleEntity);

    OfficeEntity savedOfficeEntity = modelMapper.map(createOfficeModel(), OfficeEntity.class);
    Optional<OfficeEntity> officeOptional = Mockito.mock(Optional.class);
    when(officeRepository.findById(anyInt())).thenReturn(officeOptional);
    when(officeOptional.orElseThrow(any())).thenReturn(savedOfficeEntity);

    UserAccountService userAccountService = new UserAccountService(userAccountRepository, teamRepository, officeRepository, modelMapper, roleRepository, sequenceGeneratorService, mongoTemplate);
    when(userAccountRepository.save(any())).thenReturn(savedEntity);
    UserAccountModel expected = createUserAccountModel();
    UserAccountModel actual = userAccountService.createUserAccount(createCreateUserAccountModel());
    assertTrue(compareTwoUserAccount(expected, actual));
  }
}
