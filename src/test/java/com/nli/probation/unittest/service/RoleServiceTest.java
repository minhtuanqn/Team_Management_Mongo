package com.nli.probation.unittest.service;

import static com.nli.probation.utils.RoleTestUtils.compareTwoRole;
import static com.nli.probation.utils.RoleTestUtils.createCreateRoleModel;
import static com.nli.probation.utils.RoleTestUtils.createRoleModel;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nli.probation.constant.EntityStatusEnum.RoleStatusEnum;
import com.nli.probation.customexception.DuplicatedEntityException;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.entity.RoleEntity;
import com.nli.probation.model.role.CreateRoleModel;
import com.nli.probation.model.role.RoleModel;
import com.nli.probation.repository.RoleRepository;
import com.nli.probation.service.RoleService;
import com.nli.probation.service.SequenceGeneratorService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

class RoleServiceTest {

  private final RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
  private final ModelMapper modelMapper = new ModelMapper();
  private final SequenceGeneratorService sequenceGeneratorService = Mockito.mock(
      SequenceGeneratorService.class);

  /**
   * Create new role and save successfully
   */
  @Test
  void when_saveRole_thenSaveSuccessfully() {
    when(roleRepository.existsByName(anyString())).thenReturn(false);

    RoleModel roleModel = createRoleModel();
    RoleEntity savedEntity = modelMapper.map(roleModel, RoleEntity.class);
    when(roleRepository.existsByName(anyString())).thenReturn(false);
    when(roleRepository.existsByShortName(anyString())).thenReturn(false);
    when(roleRepository.save(any())).thenReturn(savedEntity);

    RoleModel expectedModel = createRoleModel();
    CreateRoleModel paramModel = createCreateRoleModel();
    RoleModel actualModel = new RoleService(roleRepository, modelMapper,
        sequenceGeneratorService).createRole(paramModel);
    assertTrue(compareTwoRole(expectedModel, actualModel));
  }

  /**
   * Create new role but name of role has been existed
   */
  @Test
  void when_saveRoleWithExistName_thenThrowDuplicatedEntityException() {
    when(roleRepository.existsByName(anyString())).thenReturn(true);

    CreateRoleModel paramModel = createCreateRoleModel();
    RoleService roleService = new RoleService(roleRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(DuplicatedEntityException.class, () -> roleService.createRole(paramModel));
  }

  /**
   * Create new role but name of role has been existed
   */
  @Test
  void when_saveRoleWithExistShortName_thenThrowDuplicatedEntityException() {
    when(roleRepository.existsByShortName(anyString())).thenReturn(true);

    CreateRoleModel paramModel = createCreateRoleModel();
    RoleService roleService = new RoleService(roleRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(DuplicatedEntityException.class, () -> roleService.createRole(paramModel));
  }

  /**
   * Delete a role and delete successfully
   */
  @Test
  void when_deleteExistRole_thenDeleteSuccessfully() {
    RoleModel roleModel = createRoleModel();

    RoleEntity savedEntity = modelMapper.map(roleModel, RoleEntity.class);
    Optional<RoleEntity> optional = Mockito.mock(Optional.class);
    when(roleRepository.findById(anyInt())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(savedEntity);
    when(roleRepository.save(any())).thenReturn(savedEntity);

    RoleModel expectedModel = createRoleModel();
    expectedModel.setStatus(RoleStatusEnum.DISABLE.ordinal());

    RoleModel actualModel = new RoleService(roleRepository, modelMapper,
        sequenceGeneratorService).deleteRoleById(savedEntity.getId());
    assertTrue(compareTwoRole(expectedModel, actualModel));
  }

  /**
   * Delete role but can not find role by id
   */
  @Test
  void when_deleteNotExistRole_thenThrowNoSuchEntityException() {
    Optional<RoleEntity> optional = Mockito.mock(Optional.class);
    when(roleRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenThrow(NoSuchEntityException.class);

    RoleService roleService = new RoleService(roleRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(NoSuchEntityException.class, () -> roleService.deleteRoleById(1));
  }

  /**
   * Delete role but status of role is disable
   */
  @Test
  void when_deleteRoleWithDisableStatus_thenThrowNoSuchEntityException() {
    RoleEntity foundRole = modelMapper.map(createRoleModel(), RoleEntity.class);
    foundRole.setStatus(RoleStatusEnum.DISABLE.ordinal());

    Optional<RoleEntity> optional = Mockito.mock(Optional.class);
    when(roleRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(foundRole);

    RoleService roleService = new RoleService(roleRepository, modelMapper,
        sequenceGeneratorService);
    int foundId = foundRole.getId();
    assertThrows(NoSuchEntityException.class, () -> roleService.deleteRoleById(foundId));
  }

}
