package com.nli.probation.unittest.service;

import static com.nli.probation.utils.OfficeTestUtils.createOfficeModel;
import static com.nli.probation.utils.RoleTestUtils.createRoleModel;
import static com.nli.probation.utils.UserAccountTestUtils.compareTwoUserAccount;
import static com.nli.probation.utils.UserAccountTestUtils.createCreateUserAccountModel;
import static com.nli.probation.utils.UserAccountTestUtils.createUpdateUserAccountModel;
import static com.nli.probation.utils.UserAccountTestUtils.createUserAccountEntity;
import static com.nli.probation.utils.UserAccountTestUtils.createUserAccountModel;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nli.probation.constant.EntityStatusEnum.UserAccountStatusEnum;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.entity.OfficeEntity;
import com.nli.probation.entity.RoleEntity;
import com.nli.probation.entity.UserAccountEntity;
import com.nli.probation.model.useraccount.UserAccountModel;
import com.nli.probation.repository.OfficeRepository;
import com.nli.probation.repository.RoleRepository;
import com.nli.probation.repository.TeamRepository;
import com.nli.probation.repository.UserAccountRepository;
import com.nli.probation.service.SequenceGeneratorService;
import com.nli.probation.service.UserAccountService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.mongodb.core.MongoTemplate;

class UserAccountServiceTest {

  private final UserAccountRepository userAccountRepository = Mockito.mock(
      UserAccountRepository.class);
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
    when(userAccountRepository.existsByEmail(anyString())).thenReturn(false);
    when(userAccountRepository.existsByPhone(anyString())).thenReturn(false);

    RoleEntity savedRoleEntity = modelMapper.map(createRoleModel(), RoleEntity.class);
    Optional<RoleEntity> roleOptional = Mockito.mock(Optional.class);
    when(roleRepository.findById(any())).thenReturn(roleOptional);
    when(roleOptional.orElseThrow(any())).thenReturn(savedRoleEntity);

    OfficeEntity savedOfficeEntity = modelMapper.map(createOfficeModel(), OfficeEntity.class);
    Optional<OfficeEntity> officeOptional = Mockito.mock(Optional.class);
    when(officeRepository.findById(anyInt())).thenReturn(officeOptional);
    when(officeOptional.orElseThrow(any())).thenReturn(savedOfficeEntity);

    UserAccountService userAccountService = new UserAccountService(userAccountRepository,
        teamRepository, officeRepository, modelMapper, roleRepository, sequenceGeneratorService,
        mongoTemplate);
    UserAccountEntity savedEntity = createUserAccountEntity();
    when(userAccountRepository.save(any())).thenReturn(savedEntity);
    UserAccountModel expected = createUserAccountModel();
    UserAccountModel actual = userAccountService.createUserAccount(createCreateUserAccountModel());
    assertTrue(compareTwoUserAccount(expected, actual));
  }

  /**
   * Delete a user account and delete successfully
   */
  @Test
  void when_deleteExistUserAccount_thenDeleteSuccessfully() {
    UserAccountEntity savedEntity = createUserAccountEntity();
    Optional<UserAccountEntity> optional = Mockito.mock(Optional.class);
    when(userAccountRepository.findById(anyInt())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(savedEntity);
    when(userAccountRepository.save(any())).thenReturn(savedEntity);

    UserAccountService userAccountService = new UserAccountService(userAccountRepository,
        teamRepository, officeRepository, modelMapper, roleRepository, sequenceGeneratorService,
        mongoTemplate);

    UserAccountModel expectedModel = createUserAccountModel();
    expectedModel.setStatus(UserAccountStatusEnum.DISABLE.ordinal());

    UserAccountModel actualModel = userAccountService.deleteUserAccountById(savedEntity.getId());
    assertTrue(compareTwoUserAccount(expectedModel, actualModel));
  }

  /**
   * Delete user account but can not find account by id
   */
  @Test
  void when_deleteNotExistUserAccount_thenThrowNoSuchEntityException() {
    Optional<UserAccountEntity> optional = Mockito.mock(Optional.class);
    when(userAccountRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenThrow(NoSuchEntityException.class);

    UserAccountService userAccountService = new UserAccountService(userAccountRepository,
        teamRepository, officeRepository, modelMapper, roleRepository, sequenceGeneratorService,
        mongoTemplate);
    assertThrows(NoSuchEntityException.class,
        () -> userAccountService.deleteUserAccountById(Integer.MAX_VALUE));
  }

  /**
   * Find user account successfully
   */
  @Test
  void when_findExistUserAccount_thenReturnModelSuccessfully() {
    UserAccountEntity foundUserAccount = createUserAccountEntity();
    Optional<UserAccountEntity> optional = Mockito.mock(Optional.class);
    when(userAccountRepository.findById(anyInt())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(foundUserAccount);

    UserAccountModel expectedModel = createUserAccountModel();

    UserAccountService userAccountService = new UserAccountService(userAccountRepository,
        teamRepository, officeRepository, modelMapper, roleRepository, sequenceGeneratorService,
        mongoTemplate);
    UserAccountModel actualModel = userAccountService.findUserAccountById(foundUserAccount.getId());
    assertTrue(compareTwoUserAccount(expectedModel, actualModel));
  }

  /**
   * Update an existed user account and update successfully
   */
  @Test
  void when_updateExistUserAccount_thenUpdateSuccessfully() {
    modelMapper.getConfiguration()
        .setMatchingStrategy(MatchingStrategies.STANDARD);
    modelMapper.getConfiguration().setAmbiguityIgnored(true);
    when(userAccountRepository.existsByEmailAndIdNot(anyString(), anyInt())).thenReturn(false);
    when(userAccountRepository.existsByPhoneAndIdNot(anyString(), anyInt())).thenReturn(false);

    UserAccountEntity foundEntity = createUserAccountEntity();
    Optional<UserAccountEntity> optional = Mockito.mock(Optional.class);
    when(userAccountRepository.findById(anyInt())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(foundEntity);

    RoleEntity savedRoleEntity = modelMapper.map(createRoleModel(), RoleEntity.class);
    Optional<RoleEntity> roleOptional = Mockito.mock(Optional.class);
    when(roleRepository.findById(any())).thenReturn(roleOptional);
    when(roleOptional.orElseThrow(any())).thenReturn(savedRoleEntity);

    OfficeEntity savedOfficeEntity = modelMapper.map(createOfficeModel(), OfficeEntity.class);
    Optional<OfficeEntity> officeOptional = Mockito.mock(Optional.class);
    when(officeRepository.findById(anyInt())).thenReturn(officeOptional);
    when(officeOptional.orElseThrow(any())).thenReturn(savedOfficeEntity);

    UserAccountService userAccountService = new UserAccountService(userAccountRepository,
        teamRepository, officeRepository, modelMapper, roleRepository, sequenceGeneratorService,
        mongoTemplate);
    UserAccountEntity savedEntity = createUserAccountEntity();
    when(userAccountRepository.save(any())).thenReturn(savedEntity);
    UserAccountModel expected = createUserAccountModel();
    UserAccountModel actual = userAccountService.updateUserAccount(createUpdateUserAccountModel());
    assertTrue(compareTwoUserAccount(expected, actual));
  }

}
