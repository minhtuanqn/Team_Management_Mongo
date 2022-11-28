package com.nli.probation.service;

import static com.nli.probation.constant.ErrorMessageConst.DELETED_OFFICE;
import static com.nli.probation.constant.ErrorMessageConst.NOT_FOUND_OFFICE;
import static com.nli.probation.constant.ErrorMessageConst.NOT_FOUND_OFFICE_ID;
import static com.nli.probation.constant.ErrorMessageConst.OFFICE_NAME_DUPLICATE;

import com.nli.probation.constant.EntityStatusEnum;
import com.nli.probation.converter.PaginationConverter;
import com.nli.probation.customexception.DuplicatedEntityException;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.entity.OfficeEntity;
import com.nli.probation.metamodel.OfficeEntity_;
import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.office.CreateOfficeModel;
import com.nli.probation.model.office.OfficeModel;
import com.nli.probation.model.office.UpdateOfficeModel;
import com.nli.probation.repository.OfficeRepository;
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
public class OfficeService {

  private final OfficeRepository officeRepository;
  private final ModelMapper modelMapper;
  private final SequenceGeneratorService sequenceGeneratorService;

  public OfficeService(OfficeRepository officeRepository,
      ModelMapper modelMapper,
      SequenceGeneratorService sequenceGeneratorService) {
    this.officeRepository = officeRepository;
    this.modelMapper = modelMapper;
    this.sequenceGeneratorService = sequenceGeneratorService;
  }

  /**
   * Create new office
   *
   * @param createOfficeModel
   * @return saved office
   */
  public OfficeModel createOffice(CreateOfficeModel createOfficeModel) {
    //Check exist office
    if (officeRepository.existsByName(createOfficeModel.getName())) {
      throw new DuplicatedEntityException(OFFICE_NAME_DUPLICATE);
    }

    //Prepare saved entity
    OfficeEntity officeEntity = modelMapper.map(createOfficeModel, OfficeEntity.class);
    officeEntity.setId(sequenceGeneratorService.generateSequence(OfficeEntity.SEQUENCE_NAME));
    officeEntity.setStatus(EntityStatusEnum.OfficeStatusEnum.ACTIVE.ordinal());

    //Save entity to DB
    OfficeEntity savedEntity = officeRepository.save(officeEntity);
    return modelMapper.map(savedEntity, OfficeModel.class);

  }

  /**
   * Find office by id
   *
   * @param id
   * @return found office
   */
  public OfficeModel findOfficeById(int id) {
    //Find office by id
    Optional<OfficeEntity> searchedOfficeOptional = officeRepository.findById(id);
    OfficeEntity officeEntity = searchedOfficeOptional.orElseThrow(
        () -> new NoSuchEntityException(NOT_FOUND_OFFICE));
    return modelMapper.map(officeEntity, OfficeModel.class);
  }

  /**
   * Delete a office
   *
   * @param id
   * @return deleted model
   */
  public OfficeModel deleteOfficeById(int id) {
    //Find office by id
    Optional<OfficeEntity> deletedOfficeOptional = officeRepository.findById(id);
    OfficeEntity deletedOfficeEntity = deletedOfficeOptional.orElseThrow(
        () -> new NoSuchEntityException(NOT_FOUND_OFFICE_ID));
    if (deletedOfficeEntity.getStatus() == EntityStatusEnum.OfficeStatusEnum.DISABLE.ordinal()) {
      throw new NoSuchEntityException(DELETED_OFFICE);
    }

    //Set status for entity
    deletedOfficeEntity.setStatus(EntityStatusEnum.OfficeStatusEnum.DISABLE.ordinal());

    //Save entity to DB
    OfficeEntity responseEntity = officeRepository.save(deletedOfficeEntity);
    return modelMapper.map(responseEntity, OfficeModel.class);
  }

  /**
   * Update office information
   *
   * @param updateOfficeModel
   * @return updated office
   */
  public OfficeModel updateOffice(UpdateOfficeModel updateOfficeModel) {
    //Find office by id
    Optional<OfficeEntity> foundOfficeOptional = officeRepository.findById(
        updateOfficeModel.getId());
    foundOfficeOptional.orElseThrow(() -> new NoSuchEntityException(NOT_FOUND_OFFICE_ID));

    //Check existed office with name
    if (officeRepository.existsByNameAndIdNot(updateOfficeModel.getName(),
        updateOfficeModel.getId())) {
      throw new DuplicatedEntityException(OFFICE_NAME_DUPLICATE);
    }

    //Save entity to database
    OfficeEntity savedEntity = officeRepository.save(
        modelMapper.map(updateOfficeModel, OfficeEntity.class));
    return modelMapper.map(savedEntity, OfficeModel.class);
  }

  /**
   * Specification for search like name or location
   *
   * @param searchValue
   * @return Example type of office entity
   */
  private Example<OfficeEntity> searchNameOrLocation(String searchValue) {
    OfficeEntity officeEntity = new OfficeEntity();
    officeEntity.setName(searchValue);
    officeEntity.setLocation(searchValue);
    officeEntity.setId(Integer.MIN_VALUE);
    officeEntity.setStatus(Integer.MIN_VALUE);
    ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
        .withMatcher(OfficeEntity_.NAME,
            ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
        .withMatcher(OfficeEntity_.LOCATION,
            ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
        .withMatcher(OfficeEntity_.STATUS,
            ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
        .withMatcher(OfficeEntity_.ID, ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase());
    return Example.of(officeEntity, exampleMatcher);
  }

  /**
   * Search office like name or location
   *
   * @param searchValue
   * @param paginationModel
   * @return resource of data
   */
  public ResourceModel<OfficeModel> searchOffices(String searchValue,
      RequestPaginationModel paginationModel) {
    PaginationConverter<OfficeModel, OfficeEntity> paginationConverter = new PaginationConverter<>();

    //Build pageable
    String defaultSortBy = OfficeEntity_.ID;
    Pageable pageable = paginationConverter.convertToPageable(paginationModel, defaultSortBy,
        OfficeEntity.class);

    //Find all offices
    Page<OfficeEntity> officeEntityPage = officeRepository
        .findAll(searchNameOrLocation(searchValue), pageable);

    //Convert list of offices entity to list of offices model
    List<OfficeModel> officeModels = new ArrayList<>();
    for (OfficeEntity entity : officeEntityPage) {
      officeModels.add(modelMapper.map(entity, OfficeModel.class));
    }

    //Prepare resource for return
    ResourceModel<OfficeModel> resourceModel = new ResourceModel<>();
    resourceModel.setData(officeModels);
    resourceModel.setSearchText(searchValue);
    resourceModel.setSortBy(defaultSortBy);
    resourceModel.setSortType(paginationModel.getSortType());
    paginationConverter.buildPagination(paginationModel, officeEntityPage, resourceModel);
    return resourceModel;
  }
}
