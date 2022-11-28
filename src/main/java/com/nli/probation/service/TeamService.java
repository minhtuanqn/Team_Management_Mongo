package com.nli.probation.service;

import static com.nli.probation.constant.ErrorMessageConst.DELETED_TEAM;
import static com.nli.probation.constant.ErrorMessageConst.NOT_FOUND_TEAM;
import static com.nli.probation.constant.ErrorMessageConst.NOT_FOUND_TEAM_ID;
import static com.nli.probation.constant.ErrorMessageConst.TEAM_NAME_DUPLICATE;
import static com.nli.probation.constant.ErrorMessageConst.TEAM_SHORT_NAME_DUPLICATE;

import com.nli.probation.constant.EntityStatusEnum;
import com.nli.probation.converter.PaginationConverter;
import com.nli.probation.customexception.DuplicatedEntityException;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.entity.TeamEntity;
import com.nli.probation.metamodel.TeamEntity_;
import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.team.CreateTeamModel;
import com.nli.probation.model.team.TeamModel;
import com.nli.probation.model.team.UpdateTeamModel;
import com.nli.probation.repository.TeamRepository;
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
public class TeamService {

  private final TeamRepository teamRepository;
  private final ModelMapper modelMapper;
  private final SequenceGeneratorService sequenceGeneratorService;

  public TeamService(TeamRepository teamRepository,
      ModelMapper modelMapper,
      SequenceGeneratorService sequenceGeneratorService) {
    this.teamRepository = teamRepository;
    this.modelMapper = modelMapper;
    this.sequenceGeneratorService = sequenceGeneratorService;
  }

  /**
   * Create new team
   *
   * @param createTeamModel
   * @return saved team
   */
  public TeamModel createTeam(CreateTeamModel createTeamModel) {
    //Check exist team name
    if (teamRepository.existsByName(createTeamModel.getName())) {
      throw new DuplicatedEntityException(TEAM_NAME_DUPLICATE);
    }

    //Check exist short name
    if (teamRepository.existsByShortName(createTeamModel.getShortName())) {
      throw new DuplicatedEntityException(TEAM_SHORT_NAME_DUPLICATE);
    }

    //Prepare saved entity
    TeamEntity teamEntity = modelMapper.map(createTeamModel, TeamEntity.class);
    teamEntity.setId(sequenceGeneratorService.generateSequence(TeamEntity.SEQUENCE_NAME));
    teamEntity.setStatus(EntityStatusEnum.TeamStatusEnum.ACTIVE.ordinal());

    //Save entity to DB
    TeamEntity savedEntity = teamRepository.save(teamEntity);
    return modelMapper.map(savedEntity, TeamModel.class);
  }

  /**
   * Find team by id
   *
   * @param id
   * @return found team
   */
  public TeamModel findTeamById(int id) {
    //Find team by id
    Optional<TeamEntity> searchedTeamOptional = teamRepository.findById(id);
    TeamEntity teamEntity = searchedTeamOptional.orElseThrow(
        () -> new NoSuchEntityException(NOT_FOUND_TEAM));
    return modelMapper.map(teamEntity, TeamModel.class);
  }

  /**
   * Delete a team
   *
   * @param id
   * @return deleted model
   */
  public TeamModel deleteTeamById(int id) {
    //Find team by id
    Optional<TeamEntity> deletedTeamOptional = teamRepository.findById(id);
    TeamEntity deletedTeamEntity = deletedTeamOptional.orElseThrow(
        () -> new NoSuchEntityException(NOT_FOUND_TEAM_ID));
    if (deletedTeamEntity.getStatus() == EntityStatusEnum.TeamStatusEnum.DISABLE.ordinal()) {
      throw new NoSuchEntityException(DELETED_TEAM);
    }

    //Set status for entity
    deletedTeamEntity.setStatus(EntityStatusEnum.TeamStatusEnum.DISABLE.ordinal());

    //Save entity to DB
    TeamEntity responseEntity = teamRepository.save(deletedTeamEntity);
    return modelMapper.map(responseEntity, TeamModel.class);
  }

  /**
   * Update team information
   *
   * @param updateTeamModel
   * @return updated team
   */
  public TeamModel updateTeam(UpdateTeamModel updateTeamModel) {
    //Find team by id
    Optional<TeamEntity> foundTeamOptional = teamRepository.findById(updateTeamModel.getId());
    foundTeamOptional.orElseThrow(
        () -> new NoSuchEntityException(NOT_FOUND_TEAM_ID));

    //Check existed team with name
    if (teamRepository.existsByNameAndIdNot(updateTeamModel.getName(), updateTeamModel.getId())) {
      throw new DuplicatedEntityException(TEAM_NAME_DUPLICATE);
    }

    //Check existed team with short name
    if (teamRepository.existsByShortNameAndIdNot(updateTeamModel.getShortName(),
        updateTeamModel.getId())) {
      throw new DuplicatedEntityException(TEAM_SHORT_NAME_DUPLICATE);
    }

    //Save entity to database
    TeamEntity savedEntity = teamRepository.save(
        modelMapper.map(updateTeamModel, TeamEntity.class));
    return modelMapper.map(savedEntity, TeamModel.class);
  }

  /**
   * Specification for search like name or short name
   *
   * @param searchValue
   * @return Example type of team entity
   */
  private Example<TeamEntity> searchNameOrShortName(String searchValue) {
    TeamEntity teamEntity = new TeamEntity();
    teamEntity.setName(searchValue);
    teamEntity.setShortName(searchValue);
    teamEntity.setId(Integer.MIN_VALUE);
    teamEntity.setStatus(Integer.MIN_VALUE);
    ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
        .withMatcher(TeamEntity_.NAME,
            ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
        .withMatcher(TeamEntity_.SHORT_NAME,
            ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
        .withMatcher(TeamEntity_.STATUS,
            ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
        .withMatcher(TeamEntity_.ID, ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase());
    return Example.of(teamEntity, exampleMatcher);
  }

  /**
   * Search team like name or short name
   *
   * @param searchValue
   * @param paginationModel
   * @return resource of data
   */
  public ResourceModel<TeamModel> searchTeams(String searchValue,
      RequestPaginationModel paginationModel) {
    PaginationConverter<TeamModel, TeamEntity> paginationConverter = new PaginationConverter<>();

    //Build pageable
    String defaultSortBy = TeamEntity_.ID;
    Pageable pageable = paginationConverter.convertToPageable(paginationModel, defaultSortBy,
        TeamEntity.class);

    //Find all teams
    Page<TeamEntity> teamEntityPage = teamRepository.findAll(searchNameOrShortName(searchValue),
        pageable);

    //Convert list of teams entity to list of team model
    List<TeamModel> teamModels = new ArrayList<>();
    for (TeamEntity entity : teamEntityPage) {
      teamModels.add(modelMapper.map(entity, TeamModel.class));
    }

    //Prepare resource for return
    ResourceModel<TeamModel> resourceModel = new ResourceModel<>();
    resourceModel.setData(teamModels);
    resourceModel.setSearchText(searchValue);
    resourceModel.setSortBy(defaultSortBy);
    resourceModel.setSortType(paginationModel.getSortType());
    paginationConverter.buildPagination(paginationModel, teamEntityPage, resourceModel);
    return resourceModel;
  }

}
