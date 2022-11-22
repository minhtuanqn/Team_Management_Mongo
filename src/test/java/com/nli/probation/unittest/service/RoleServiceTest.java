package com.nli.probation.unittest.service;

import static com.nli.probation.utils.OfficeTestUtils.compareTwoOffice;
import static com.nli.probation.utils.OfficeTestUtils.createCreateOfficeModel;
import static com.nli.probation.utils.OfficeTestUtils.createOfficeModel;
import static com.nli.probation.utils.RoleTestUtils.compareTwoRole;
import static com.nli.probation.utils.RoleTestUtils.createCreateRoleModel;
import static com.nli.probation.utils.RoleTestUtils.createRoleModel;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nli.probation.customexception.DuplicatedEntityException;
import com.nli.probation.entity.OfficeEntity;
import com.nli.probation.entity.RoleEntity;
import com.nli.probation.model.office.CreateOfficeModel;
import com.nli.probation.model.office.OfficeModel;
import com.nli.probation.model.role.CreateRoleModel;
import com.nli.probation.model.role.RoleModel;
import com.nli.probation.repository.RoleRepository;
import com.nli.probation.service.OfficeService;
import com.nli.probation.service.RoleService;
import com.nli.probation.service.SequenceGeneratorService;
import javax.management.relation.Role;
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

}