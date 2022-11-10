package com.nli.probation.service;

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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleService(RoleRepository roleRepository,
                       ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Create new role
     * @param createRoleModel
     * @return saved role
     */
    public RoleModel createRole(CreateRoleModel createRoleModel) {
        //Check exist role name
        if (roleRepository.existsByName(createRoleModel.getName()))
            throw new DuplicatedEntityException("Duplicated name of role");

        //Check exist short name
        if (roleRepository.existsByShortName(createRoleModel.getShortName()))
            throw new DuplicatedEntityException("Duplicated short name of role");

        //Prepare saved entity
        RoleEntity roleEntity = modelMapper.map(createRoleModel, RoleEntity.class);
        roleEntity.setStatus(EntityStatusEnum.RoleStatusEnum.ACTIVE.ordinal());

        //Save entity to DB
        RoleEntity savedEntity = roleRepository.save(roleEntity);
        RoleModel responseRoleModel = modelMapper.map(savedEntity, RoleModel.class);

        return responseRoleModel;
    }

    /**
     * Find rolw by id
     * @param id
     * @return found role
     */
    public RoleModel findRoleById(int id) {
        //Find role by id
        Optional<RoleEntity> searchedRoleOptional = roleRepository.findById(id);
        RoleEntity roleEntity = searchedRoleOptional.orElseThrow(() -> new NoSuchEntityException("Not found role"));
        return modelMapper.map(roleEntity, RoleModel.class);
    }

    /**
     * Delete a role
     * @param id
     * @return deleted model
     */
    public RoleModel deleteRoleById(int id) {
        //Find role by id
        Optional<RoleEntity> deletedRoleOptional = roleRepository.findById(id);
        RoleEntity deletedRoleEntity = deletedRoleOptional.orElseThrow(() -> new NoSuchEntityException("Not found role with id"));

        //Set status for entity
        deletedRoleEntity.setStatus(EntityStatusEnum.RoleStatusEnum.DISABLE.ordinal());

        //Save entity to DB
        RoleEntity responseEntity = roleRepository.save(deletedRoleEntity);
        return modelMapper.map(responseEntity, RoleModel.class);
    }

    /**
     * Update role information
     * @param updateRoleModel
     * @return updated role
     */
    public RoleModel updateRole (UpdateRoleModel updateRoleModel) {
        //Find role by id
        Optional<RoleEntity> foundRoleOptional = roleRepository.findById(updateRoleModel.getId());
        foundRoleOptional.orElseThrow(() -> new NoSuchEntityException("Not found role with id"));

        //Check existed role with name
        if(roleRepository.existsByNameAndIdNot(updateRoleModel.getName(), updateRoleModel.getId()))
            throw new DuplicatedEntityException("Duplicate name for role");

        //Check existed role with short name
        if(roleRepository.existsByShortNameAndIdNot(updateRoleModel.getShortName(), updateRoleModel.getId()))
            throw new DuplicatedEntityException("Duplicate short name for role");

        //Save entity to database
        RoleEntity savedEntity = roleRepository.save(modelMapper.map(updateRoleModel, RoleEntity.class));
        return modelMapper.map(savedEntity, RoleModel.class);
    }

    /**
     * Specification for search name
     * @param searchValue
     * @return specification
     */
    private Specification<RoleEntity> containsName(String searchValue) {
        return ((root, query, criteriaBuilder) -> {
            String pattern = searchValue != null ? "%" + searchValue + "%" : "%%";
            return criteriaBuilder.like(root.get(RoleEntity_.NAME), pattern);
        });
    }

    /**
     * Specification for search short name
     * @param searchValue
     * @return specification
     */
    private Specification<RoleEntity> containsShortName(String searchValue) {
        return ((root, query, criteriaBuilder) -> {
            String pattern = searchValue != null ? "%" + searchValue + "%" : "%%";
            return criteriaBuilder.like(root.get(RoleEntity_.SHORT_NAME), pattern);
        });
    }

    /**
     * Search role like name or short name
     * @param searchValue
     * @param paginationModel
     * @return resource of data
     */
    public ResourceModel<RoleModel> searchRoles(String searchValue, RequestPaginationModel paginationModel) {
        PaginationConverter<RoleModel, RoleEntity> paginationConverter = new PaginationConverter<>();

        //Build pageable
        String defaultSortBy = RoleEntity_.ID;
        Pageable pageable = paginationConverter.convertToPageable(paginationModel, defaultSortBy, RoleEntity.class);

        //Find all roles
        Page<RoleEntity> roleEntityPage = roleRepository.findAll(containsName(searchValue)
                .and(containsShortName(searchValue)), pageable);

        //Convert list of roles entity to list of role model
        List<RoleModel> roleModels = new ArrayList<>();
        for(RoleEntity entity : roleEntityPage) {
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
