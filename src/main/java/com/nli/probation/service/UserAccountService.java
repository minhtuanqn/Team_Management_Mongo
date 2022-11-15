package com.nli.probation.service;

import com.nli.probation.constant.EntityStatusEnum;
import com.nli.probation.converter.PaginationConverter;
import com.nli.probation.customexception.DuplicatedEntityException;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.entity.*;
import com.nli.probation.metamodel.UserAccountEntity_;
import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.office.OfficeModel;
import com.nli.probation.model.role.RoleModel;
import com.nli.probation.model.team.TeamModel;
import com.nli.probation.model.useraccount.CreateUserAccountModel;
import com.nli.probation.model.useraccount.UpdateUserAccountModel;
import com.nli.probation.model.useraccount.UserAccountModel;
import com.nli.probation.repository.OfficeRepository;
import com.nli.probation.repository.RoleRepository;
import com.nli.probation.repository.TeamRepository;
import com.nli.probation.repository.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final TeamRepository teamRepository;
    private final OfficeRepository officeRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final MongoTemplate mongoTemplate;


    public UserAccountService(UserAccountRepository userAccountRepository,
                              TeamRepository teamRepository,
                              OfficeRepository officeRepository,
                              ModelMapper modelMapper,
                              RoleRepository roleRepository,
                              SequenceGeneratorService sequenceGeneratorService,
                              MongoTemplate mongoTemplate) {
        this.userAccountRepository = userAccountRepository;
        this.teamRepository = teamRepository;
        this.officeRepository = officeRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Create new user account
     * @param createUserAccountModel
     * @return create user account
     */
    public UserAccountModel createUserAccount(CreateUserAccountModel createUserAccountModel) {
        //Check exist email
        if (userAccountRepository.existsByEmail(createUserAccountModel.getEmail()))
            throw new DuplicatedEntityException("Duplicated email of user account");

        //Check exist phone
        if(userAccountRepository.existsByPhone(createUserAccountModel.getPhone()))
            throw new DuplicatedEntityException("Duplicated phone of user account");

        //Check exist team
        Optional<TeamEntity> existedTeamOptional = teamRepository.findById(createUserAccountModel.getTeamId());

        //Check exist office
        Optional<OfficeEntity> existedOfficeOptional = officeRepository.findById(createUserAccountModel.getOfficeId());
        OfficeEntity existedOfficeEntity = existedOfficeOptional
                .orElseThrow(() -> new NoSuchEntityException("Not found office"));

        //Check exist role
        Optional<RoleEntity> existedRoleOptional = roleRepository.findById(createUserAccountModel.getRoleId());
        RoleEntity existedRoleEntity = existedRoleOptional
                .orElseThrow(() -> new NoSuchEntityException("Not found role"));

        //Prepare saved entity
        UserAccountEntity userAccountEntity = modelMapper.map(createUserAccountModel, UserAccountEntity.class);
        userAccountEntity.setId(sequenceGeneratorService.generateSequence(UserAccountEntity.SEQUENCE_NAME));
        userAccountEntity.setStatus(EntityStatusEnum.UserAccountStatusEnum.ACTIVE.ordinal());
        userAccountEntity.setOfficeId(userAccountEntity.getOfficeId());
        userAccountEntity.setRoleId(userAccountEntity.getRoleId());
        existedTeamOptional.ifPresentOrElse(existedTeamEntity -> userAccountEntity.setTeamId(userAccountEntity.getTeamId()),
                () -> userAccountEntity.setTeamId(0));

        //Save entity to DB
        UserAccountEntity savedEntity = userAccountRepository.save(userAccountEntity);
        UserAccountModel responseUserAccountModel = modelMapper.map(savedEntity, UserAccountModel.class);
        responseUserAccountModel.setOfficeModel(modelMapper.map(existedOfficeEntity, OfficeModel.class));
        responseUserAccountModel.setRoleModel(modelMapper.map(existedRoleEntity, RoleModel.class));
        existedTeamOptional.ifPresent(existedTeamEntity -> responseUserAccountModel.setTeamModel(modelMapper.map(existedTeamEntity, TeamModel.class)));

        return responseUserAccountModel;
    }

    /**
     * Find user account by id
     * @param id
     * @return found user account
     */
    public UserAccountModel findUserAccountById(int id) {
        //Find user account by id
        Optional<UserAccountEntity> searchedAccountOptional = userAccountRepository.findById(id);
        UserAccountEntity userAccountEntity = searchedAccountOptional.orElseThrow(() -> new NoSuchEntityException("Not found user account"));
        UserAccountModel userAccountModel = modelMapper.map(userAccountEntity, UserAccountModel.class);

        //Find team information
        Optional<TeamEntity> teamOptional = teamRepository.findById(userAccountEntity.getTeamId());
        teamOptional.ifPresent(teamEntity -> userAccountModel.setTeamModel(modelMapper.map(teamEntity, TeamModel.class)));

        //Find office information
        Optional<OfficeEntity> officeOptional = officeRepository.findById(userAccountEntity.getOfficeId());
        officeOptional.ifPresent(officeEntity -> userAccountModel.setOfficeModel(modelMapper.map(officeEntity, OfficeModel.class)));

        //Find role information
        Optional<RoleEntity> roleOptional = roleRepository.findById(userAccountEntity.getRoleId());
        roleOptional.ifPresent(roleEntity -> userAccountModel.setRoleModel(modelMapper.map(roleEntity, RoleModel.class)));

        return userAccountModel;
    }

    /**
     * Delete a user account
     * @param id
     * @return deleted model
     */
    public UserAccountModel deleteUserAccountById(int id) {
        //Find user account by id
        Optional<UserAccountEntity> deletedAccountOptional = userAccountRepository.findById(id);
        UserAccountEntity deletedAccountEntity = deletedAccountOptional.orElseThrow(() -> new NoSuchEntityException("Not found user account with id"));
        if(deletedAccountEntity.getStatus() == EntityStatusEnum.UserAccountStatusEnum.DISABLE.ordinal())
            throw new NoSuchEntityException("This user account was deleted");

        //Set status for entity
        deletedAccountEntity.setStatus(EntityStatusEnum.UserAccountStatusEnum.DISABLE.ordinal());

        //Save entity to DB
        UserAccountEntity responseEntity = userAccountRepository.save(deletedAccountEntity);
        UserAccountModel userAccountModel = modelMapper.map(responseEntity, UserAccountModel.class);

        //Find team information
        Optional<TeamEntity> teamOptional = teamRepository.findById(responseEntity.getTeamId());
        teamOptional.ifPresent(teamEntity -> userAccountModel.setTeamModel(modelMapper.map(teamEntity, TeamModel.class)));

        //Find office information
        Optional<OfficeEntity> officeOptional = officeRepository.findById(responseEntity.getOfficeId());
        officeOptional.ifPresent(officeEntity -> userAccountModel.setOfficeModel(modelMapper.map(officeEntity, OfficeModel.class)));

        //Find role information
        Optional<RoleEntity> roleOptional = roleRepository.findById(responseEntity.getRoleId());
        roleOptional.ifPresent(roleEntity -> userAccountModel.setRoleModel(modelMapper.map(roleEntity, RoleModel.class)));

        return userAccountModel;
    }

    /**
     * Update user account information
     * @param updateUserAccountModel
     * @return updated user account
     */
    public UserAccountModel updateUserAccount (UpdateUserAccountModel updateUserAccountModel) {
        //Find user account by id
        Optional<UserAccountEntity> foundAccountOptional = userAccountRepository.findById(updateUserAccountModel.getId());
        UserAccountEntity foundAccountEntity = foundAccountOptional.orElseThrow(() -> new NoSuchEntityException("Not found user account with id"));

        //Check existed user account with email
        if(userAccountRepository.existsByEmailAndIdNot(updateUserAccountModel.getEmail(), updateUserAccountModel.getId()))
            throw new DuplicatedEntityException("Duplicate email for user account");

        //Check existed user account with phone
        if(userAccountRepository.existsByEmailAndIdNot(updateUserAccountModel.getPhone(), updateUserAccountModel.getId()))
            throw new DuplicatedEntityException("Duplicate phone for user account");

        //Check exist team
        Optional<TeamEntity> existedTeamOptional = teamRepository.findById(updateUserAccountModel.getTeamId());

        //Check exist office
        Optional<OfficeEntity> existedOfficeOptional = officeRepository.findById(updateUserAccountModel.getOfficeId());
        OfficeEntity existedOfficeEntity = existedOfficeOptional.orElseThrow(() -> new NoSuchEntityException("Not found office"));

        //Check exist role
        Optional<RoleEntity> existedRoleOptional = roleRepository.findById(updateUserAccountModel.getRoleId());
        RoleEntity existedRoleEntity = existedRoleOptional.orElseThrow(() -> new NoSuchEntityException("Not found role"));

        //Prepare saved entity
        UserAccountEntity userAccountEntity = modelMapper.map(updateUserAccountModel, UserAccountEntity.class);
        userAccountEntity.setRoleId(existedOfficeEntity.getId());
        userAccountEntity.setOfficeId(existedOfficeEntity.getId());
        existedTeamOptional.ifPresentOrElse(existedTeamEntity -> userAccountEntity.setTeamId(userAccountEntity.getTeamId()),
                () -> userAccountEntity.setTeamId(0));

        //Save entity to DB
        UserAccountEntity savedEntity = userAccountRepository.save(userAccountEntity);
        UserAccountModel responseUserAccountModel = modelMapper.map(savedEntity, UserAccountModel.class);
        responseUserAccountModel.setOfficeModel(modelMapper.map(existedOfficeEntity, OfficeModel.class));
        responseUserAccountModel.setRoleModel(modelMapper.map(existedRoleEntity, RoleModel.class));
        existedTeamOptional.ifPresent(existedTeamEntity -> responseUserAccountModel.setTeamModel(modelMapper.map(existedTeamEntity, TeamModel.class)));

        return responseUserAccountModel;
    }


    /**
     * Search user account like name or email
     * @param searchValue
     * @param paginationModel
     * @return resource of data
     */
    public ResourceModel<UserAccountModel> searchAccounts(String searchValue, RequestPaginationModel paginationModel,
                                                          Integer teamId) {
        PaginationConverter<UserAccountModel, UserAccountEntity> paginationConverter = new PaginationConverter<>();

        //Build pageable
        String defaultSortBy = UserAccountEntity_.ID;
        Pageable pageable = paginationConverter.convertToPageable(paginationModel, defaultSortBy, UserAccountEntity.class);

        //Create query object
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where(UserAccountEntity_.EMAIL).regex(".*" + searchValue + ".*"),
                Criteria.where(UserAccountEntity_.NAME).regex(".*" + searchValue + ".*"));
        Query query = new Query(criteria).with(pageable);

        //Find all user accounts
        List<UserAccountEntity> userAccountEntities;
        if(teamId != 0) {
            Optional<TeamEntity> teamOptional = teamRepository.findById(teamId);
            teamOptional.orElseThrow(() -> new NoSuchEntityException("Not found team"));
            userAccountEntities = mongoTemplate.find(query.addCriteria(Criteria.where(UserAccountEntity_.TEAM_ID).is(teamId)), UserAccountEntity.class);
        } else {
            userAccountEntities = mongoTemplate.find(query, UserAccountEntity.class);
        }
        Page<UserAccountEntity> accountEntityPage = PageableExecutionUtils.getPage(userAccountEntities, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), UserAccountEntity.class));

        //Convert list of user accounts entity to list of user account model
        List<UserAccountModel> accountModels = new ArrayList<>();
        for(UserAccountEntity entity : accountEntityPage) {
            UserAccountModel userAccountModel = modelMapper.map(entity, UserAccountModel.class);
            //Check exist team
            Optional<TeamEntity> teamOptional = teamRepository.findById(entity.getTeamId());
            teamOptional.ifPresent(teamEntity -> userAccountModel.setTeamModel(modelMapper.map(teamEntity, TeamModel.class)));

            //Check exist office
            Optional<OfficeEntity> officeOptional = officeRepository.findById(entity.getOfficeId());
            officeOptional.ifPresent(officeEntity -> userAccountModel.setOfficeModel(modelMapper.map(officeEntity, OfficeModel.class)));

            //Check exist role
            Optional<RoleEntity> roleOptional = roleRepository.findById(entity.getRoleId());
            roleOptional.ifPresent(roleEntity -> userAccountModel.setRoleModel(modelMapper.map(roleEntity, RoleModel.class)));
            accountModels.add(userAccountModel);
        }

        //Prepare resource for return
        ResourceModel<UserAccountModel> resourceModel = new ResourceModel<>();
        resourceModel.setData(accountModels);
        resourceModel.setSearchText(searchValue);
        resourceModel.setSortBy(defaultSortBy);
        resourceModel.setSortType(paginationModel.getSortType());
        paginationConverter.buildPagination(paginationModel, accountEntityPage, resourceModel);
        return resourceModel;
    }

    /**
     * Add list of users to team
     * @param teamId
     * @param userIds
     * @return list of saved user accounts
     */
    @Transactional(rollbackFor = IllegalArgumentException.class)
    public List<UserAccountModel> addUserListToTeam(int teamId, List<Integer> userIds)  {
        //Check exist team
        Optional<TeamEntity> teamOptional = teamRepository.findById(teamId);
        TeamEntity teamEntity = teamOptional.orElseThrow(() -> new NoSuchEntityException("Not found team"));

        //Find all user accounts by id list and set team id
        List<UserAccountEntity> userAccountEntities = userAccountRepository.findAllByIdIn(userIds);
        for(UserAccountEntity userAccountEntity: userAccountEntities) {
            userAccountEntity.setTeamId(teamId);
        }

        //Save list of user accounts to database
        List<UserAccountModel> userAccountModels = new ArrayList<>();
        List<UserAccountEntity> savedAccounts = userAccountRepository.saveAll(userAccountEntities);
        for(UserAccountEntity userAccountEntity : savedAccounts) {
            UserAccountModel userAccountModel = modelMapper.map(userAccountEntity, UserAccountModel.class);

            //Find role and set to response model
            Optional<RoleEntity> roleOptional = roleRepository.findById(userAccountEntity.getRoleId());
            roleOptional.ifPresent(roleEntity -> userAccountModel.setRoleModel(modelMapper.map(roleEntity, RoleModel.class)));

            //Find office and set to response model
            Optional<OfficeEntity> officeOptional = officeRepository.findById(userAccountEntity.getOfficeId());
            officeOptional.ifPresent(officeEntity -> userAccountModel.setOfficeModel(modelMapper.map(officeEntity, OfficeModel.class)));

            //Set team to response model
            userAccountModel.setTeamModel(modelMapper.map(teamEntity, TeamModel.class));

            //Add user account to list
            userAccountModels.add(userAccountModel);
        }
        return userAccountModels;
    }

    /**
     * Delete list of users from team
     * @param teamId
     * @param userIds
     * @return list of saved user accounts
     */
    @Transactional(rollbackFor = IllegalArgumentException.class)
    public List<UserAccountModel> deleteUserListFromTeam(int teamId, List<Integer> userIds)  {
        //Check exist team
        Optional<TeamEntity> teamOptional = teamRepository.findById(teamId);
        TeamEntity teamEntity = teamOptional.orElseThrow(() -> new NoSuchEntityException("Not found team"));

        //Find all user accounts by id list and set team id
        List<UserAccountEntity> userAccountEntities = userAccountRepository.findAllByIdIn(userIds);
        for(UserAccountEntity userAccountEntity: userAccountEntities) {
            if(userAccountEntity.getTeamId() == teamId) {
                userAccountEntity.setTeamId(0);
            }
        }

        //Save list of user accounts to database
        List<UserAccountModel> userAccountModels = new ArrayList<>();
        List<UserAccountEntity> savedAccounts = userAccountRepository.saveAll(userAccountEntities);
        for(UserAccountEntity userAccountEntity : savedAccounts) {
            UserAccountModel userAccountModel = modelMapper.map(userAccountEntity, UserAccountModel.class);

            //Find role and set to response model
            Optional<RoleEntity> roleOptional = roleRepository.findById(userAccountEntity.getRoleId());
            roleOptional.ifPresent(roleEntity -> userAccountModel.setRoleModel(modelMapper.map(roleEntity, RoleModel.class)));

            //Find office and set to response model
            Optional<OfficeEntity> officeOptional = officeRepository.findById(userAccountEntity.getOfficeId());
            officeOptional.ifPresent(officeEntity -> userAccountModel.setOfficeModel(modelMapper.map(officeEntity, OfficeModel.class)));

            //Fimd team and set to response model
            Optional<TeamEntity> savedTeamOptional = teamRepository.findById(userAccountEntity.getTeamId());
            savedTeamOptional.ifPresent(savedTeamEntity -> userAccountModel.setTeamModel(modelMapper.map(savedTeamEntity, TeamModel.class)));

            //Add user account to list
            userAccountModels.add(userAccountModel);
        }
        return userAccountModels;
    }
}
