package com.nli.probation.unittest.service;

import static com.nli.probation.utils.RoleTestUtils.compareTwoRole;
import static com.nli.probation.utils.RoleTestUtils.compareTwoRoleList;
import static com.nli.probation.utils.RoleTestUtils.createCreateRoleModel;
import static com.nli.probation.utils.RoleTestUtils.createRoleModel;
import static com.nli.probation.utils.RoleTestUtils.createUpdateRoleModel;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nli.probation.MockConstants;
import com.nli.probation.constant.EntityStatusEnum.RoleStatusEnum;
import com.nli.probation.customexception.DuplicatedEntityException;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.entity.RoleEntity;
import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.role.CreateRoleModel;
import com.nli.probation.model.role.RoleModel;
import com.nli.probation.model.role.UpdateRoleModel;
import com.nli.probation.repository.RoleRepository;
import com.nli.probation.service.RoleService;
import com.nli.probation.service.SequenceGeneratorService;
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
   * Create new role but short name of role has been existed
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
        sequenceGeneratorService).deleteRoleById(MockConstants.ROLE_ID);
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
    assertThrows(NoSuchEntityException.class,
        () -> roleService.deleteRoleById(MockConstants.NOT_FOUND_ROLE_ID));
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
    assertThrows(NoSuchEntityException.class,
        () -> roleService.deleteRoleById(MockConstants.ROLE_ID));
  }

  /**
   * Find role successfully
   */
  @Test
  void when_findExistRole_thenReturnModelSuccessfully() {
    RoleModel roleModel = createRoleModel();

    RoleEntity foundRole = modelMapper.map(roleModel, RoleEntity.class);
    Optional<RoleEntity> optional = Mockito.mock(Optional.class);
    when(roleRepository.findById(anyInt())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(foundRole);

    RoleModel expectedModel = createRoleModel();

    RoleModel actualModel = new RoleService(roleRepository, modelMapper,
        sequenceGeneratorService).findRoleById(MockConstants.ROLE_ID);
    assertTrue(compareTwoRole(expectedModel, actualModel));
  }

  /**
   * Find an role does not exist
   */
  @Test
  void when_findNotExistRole_thenThrowNoSuchEntity() {
    Optional<RoleEntity> optional = Mockito.mock(Optional.class);
    when(roleRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenThrow(NoSuchEntityException.class);

    RoleService roleService = new RoleService(roleRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(NoSuchEntityException.class,
        () -> roleService.findRoleById(MockConstants.NOT_FOUND_ROLE_ID));
  }

  /**
   * Update an existed role and update successfully
   */
  @Test
  void when_updateExistRole_thenUpdateSuccessfully() {
    UpdateRoleModel updateRoleModel = createUpdateRoleModel();

    RoleEntity savedEntity = modelMapper.map(updateRoleModel, RoleEntity.class);
    Optional<RoleEntity> optional = Mockito.mock(Optional.class);
    when(roleRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(
        modelMapper.map(createRoleModel(), RoleEntity.class));
    when(roleRepository.existsByNameAndIdNot(anyString(), anyInt())).thenReturn(false);
    when(roleRepository.existsByShortNameAndIdNot(anyString(), anyInt())).thenReturn(false);
    when(roleRepository.save(any())).thenReturn(savedEntity);

    RoleModel expectedModel = createRoleModel();

    RoleModel actualModel = new RoleService(roleRepository, modelMapper,
        sequenceGeneratorService).updateRole(createUpdateRoleModel());
    assertTrue(compareTwoRole(expectedModel, actualModel));
  }

  /**
   * Find roles like name or short name then return result
   */
  @Test
  void when_findRoleLikeNameOrShortNameSortByAsc_thenReturnResourceOfListOfRolesByAsc() {
    List<RoleEntity> entityList = new ArrayList<>();
    entityList.add(modelMapper.map(createRoleModel(), RoleEntity.class));
    Page<RoleEntity> entityPage = new PageImpl<>(entityList);

    when(roleRepository.findAll(any(), (Pageable) any())).thenReturn(entityPage);

    RoleService roleService = new RoleService(roleRepository, modelMapper,
        sequenceGeneratorService);
    ResourceModel<RoleModel> actualResource = roleService.
        searchRoles(MockConstants.SEARCH_VALUE,
            new RequestPaginationModel(MockConstants.INDEX, MockConstants.LIMIT,
                MockConstants.SORT_BY, MockConstants.SORT_TYPE));

    List<RoleModel> modelList = new ArrayList<>();
    RoleModel expectModel = createRoleModel();
    modelList.add(expectModel);
    TestUtils<RoleModel> testUtils = new TestUtils<>();
    ResourceModel<RoleModel> expectedResource = testUtils
        .createResourceModel(MockConstants.SEARCH_VALUE, MockConstants.SORT_TYPE,
            MockConstants.SORT_BY,
            MockConstants.TOTAL_RESULT, MockConstants.TOTAL_PAGE, MockConstants.INDEX,
            MockConstants.LIMIT, modelList);
    assertTrue(testUtils.compareTwoResourceInformation(expectedResource, actualResource));
    assertTrue(compareTwoRoleList(expectedResource.getData(), actualResource.getData()));
  }

}
