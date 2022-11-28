package com.nli.probation.service;

import static com.nli.probation.constant.ErrorMessageConst.DELETED_ROLE;
import static com.nli.probation.constant.ErrorMessageConst.NOT_FOUND_ROLE;
import static com.nli.probation.constant.ErrorMessageConst.NOT_FOUND_ROLE_ID;
import static com.nli.probation.constant.ErrorMessageConst.ROLE_NAME_DUPLICATE;
import static com.nli.probation.constant.ErrorMessageConst.ROLE_SHORT_NAME_DUPLICATE;

import com.nli.probation.constant.EntityStatusEnum;
import com.nli.probation.converter.PaginationConverter;
import com.nli.probation.customexception.DuplicatedEntityException;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.entity.RoleEntity;
import com.nli.probation.metamodel.RoleEntity_;
import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.role.CreateRoleModel;
import com.nli.probation.model.role.RoleModel;
import com.nli.probation.model.role.UpdateRoleModel;
import com.nli.probation.repository.RoleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

  private final RoleRepository roleRepository;
  private final ModelMapper modelMapper;
  private final SequenceGeneratorService sequenceGeneratorService;

  public RoleService(RoleRepository roleRepository,
      ModelMapper modelMapper,
      SequenceGeneratorService sequenceGeneratorService) {
    this.roleRepository = roleRepository;
    this.modelMapper = modelMapper;
    this.sequenceGeneratorService = sequenceGeneratorService;
  }

  /**
   * Create new role
   *
   * @param createRoleModel
   * @return saved role
   */
  public RoleModel createRole(CreateRoleModel createRoleModel) {
    //Check exist role name
    if (roleRepository.existsByName(createRoleModel.getName())) {
      throw new DuplicatedEntityException(ROLE_NAME_DUPLICATE);
    }

    //Check exist short name
    if (roleRepository.existsByShortName(createRoleModel.getShortName())) {
      throw new DuplicatedEntityException(ROLE_SHORT_NAME_DUPLICATE);
    }

    //Prepare saved entity
    RoleEntity roleEntity = modelMapper.map(createRoleModel, RoleEntity.class);
    roleEntity.setId(sequenceGeneratorService.generateSequence(RoleEntity.SEQUENCE_NAME));
    roleEntity.setStatus(EntityStatusEnum.RoleStatusEnum.ACTIVE.ordinal());

    //Save entity to DB
    RoleEntity savedEntity = roleRepository.save(roleEntity);
    return modelMapper.map(savedEntity, RoleModel.class);
  }

  /**
   * Find rolw by id
   *
   * @param id
   * @return found role
   */
  public RoleModel findRoleById(int id) {
    //Find role by id
    Optional<RoleEntity> searchedRoleOptional = roleRepository.findById(id);
    RoleEntity roleEntity = searchedRoleOptional.orElseThrow(
        () -> new NoSuchEntityException(NOT_FOUND_ROLE));
    return modelMapper.map(roleEntity, RoleModel.class);
  }

  /**
   * Delete a role
   *
   * @param id
   * @return deleted model
   */
  public RoleModel deleteRoleById(int id) {
    //Find role by id
    Optional<RoleEntity> deletedRoleOptional = roleRepository.findById(id);
    RoleEntity deletedRoleEntity = deletedRoleOptional.orElseThrow(
        () -> new NoSuchEntityException(NOT_FOUND_ROLE_ID));
    if (deletedRoleEntity.getStatus() == EntityStatusEnum.RoleStatusEnum.DISABLE.ordinal()) {
      throw new NoSuchEntityException(DELETED_ROLE);
    }

    //Set status for entity
    deletedRoleEntity.setStatus(EntityStatusEnum.RoleStatusEnum.DISABLE.ordinal());

    //Save entity to DB
    RoleEntity responseEntity = roleRepository.save(deletedRoleEntity);
    return modelMapper.map(responseEntity, RoleModel.class);
  }

  /**
   * Update role information
   *
   * @param updateRoleModel
   * @return updated role
   */
  public RoleModel updateRole(UpdateRoleModel updateRoleModel) {
    //Find role by id
    Optional<RoleEntity> foundRoleOptional = roleRepository.findById(updateRoleModel.getId());
    foundRoleOptional.orElseThrow(() -> new NoSuchEntityException(NOT_FOUND_ROLE_ID));

    //Check existed role with name
    if (roleRepository.existsByNameAndIdNot(updateRoleModel.getName(), updateRoleModel.getId())) {
      throw new DuplicatedEntityException(ROLE_NAME_DUPLICATE);
    }

    //Check existed role with short name
    if (roleRepository.existsByShortNameAndIdNot(updateRoleModel.getShortName(),
        updateRoleModel.getId())) {
      throw new DuplicatedEntityException(ROLE_SHORT_NAME_DUPLICATE);
    }

    //Save entity to database
    RoleEntity savedEntity = roleRepository.save(
        modelMapper.map(updateRoleModel, RoleEntity.class));
    return modelMapper.map(savedEntity, RoleModel.class);
  }

  /**
   * Specification for search like name or short name
   *
   * @param searchValue
   * @return Example type of role entity
   */
  private Example<RoleEntity> searchNameOrShortName(String searchValue) {
    RoleEntity roleEntity = new RoleEntity();
    roleEntity.setName(searchValue);
    roleEntity.setShortName(searchValue);
    roleEntity.setId(Integer.MIN_VALUE);
    roleEntity.setStatus(Integer.MIN_VALUE);
    ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
        .withMatcher(RoleEntity_.NAME,
            ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
        .withMatcher(RoleEntity_.SHORT_NAME,
            ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
        .withMatcher(RoleEntity_.STATUS,
            ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
        .withMatcher(RoleEntity_.ID, ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase());
    return Example.of(roleEntity, exampleMatcher);
  }

  /**
   * Search role like name or short name
   *
   * @param searchValue
   * @param paginationModel
   * @return resource of data
   */
  public ResourceModel<RoleModel> searchRoles(String searchValue,
      RequestPaginationModel paginationModel) {
    PaginationConverter<RoleModel, RoleEntity> paginationConverter = new PaginationConverter<>();

    //Build pageable
    String defaultSortBy = RoleEntity_.ID;
    Pageable pageable = paginationConverter.convertToPageable(paginationModel, defaultSortBy,
        RoleEntity.class);

    //Find all roles
    Page<RoleEntity> roleEntityPage = roleRepository.findAll(searchNameOrShortName(searchValue),
        pageable);

    //Convert list of roles entity to list of role model
    List<RoleModel> roleModels = new ArrayList<>();
    for (RoleEntity entity : roleEntityPage) {
      roleModels.add(modelMapper.map(entity, RoleModel.class));
    }

    //Prepare resource for return
    ResourceModel<RoleModel> resourceModel = new ResourceModel<>();
    resourceModel.setData(roleModels);
    resourceModel.setSearchText(searchValue);
    resourceModel.setSortBy(defaultSortBy);
    resourceModel.setSortType(paginationModel.getSortType());
    paginationConverter.buildPagination(paginationModel, roleEntityPage, resourceModel);
    return resourceModel;
  }
}
