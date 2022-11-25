package com.nli.probation.unittest.service;

import static com.nli.probation.utils.OfficeTestUtils.compareTwoOffice;
import static com.nli.probation.utils.OfficeTestUtils.compareTwoOfficeList;
import static com.nli.probation.utils.OfficeTestUtils.createCreateOfficeModel;
import static com.nli.probation.utils.OfficeTestUtils.createOfficeModel;
import static com.nli.probation.utils.OfficeTestUtils.createUpdateOfficeModel;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nli.probation.constant.EntityStatusEnum.OfficeStatusEnum;
import com.nli.probation.customexception.DuplicatedEntityException;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.entity.OfficeEntity;
import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.office.CreateOfficeModel;
import com.nli.probation.model.office.OfficeModel;
import com.nli.probation.model.office.UpdateOfficeModel;
import com.nli.probation.repository.OfficeRepository;
import com.nli.probation.service.OfficeService;
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

class OfficeServiceTest {

  private final OfficeRepository officeRepository = Mockito.mock(OfficeRepository.class);
  private final ModelMapper modelMapper = new ModelMapper();
  private final SequenceGeneratorService sequenceGeneratorService = Mockito.mock(
      SequenceGeneratorService.class);

  /**
   * Create new office and save successfully
   */
  @Test
  void when_saveOffice_thenSaveSuccessfully() {
    when(officeRepository.existsByName(anyString())).thenReturn(false);

    OfficeModel officeModel = createOfficeModel();
    OfficeEntity savedEntity = modelMapper.map(officeModel, OfficeEntity.class);
    when(officeRepository.existsByName(anyString())).thenReturn(false);
    when(officeRepository.save(any())).thenReturn(savedEntity);

    OfficeModel expectedModel = createOfficeModel();
    CreateOfficeModel paramModel = createCreateOfficeModel();
    OfficeModel actualModel = new OfficeService(officeRepository, modelMapper,
        sequenceGeneratorService).createOffice(paramModel);
    assertTrue(compareTwoOffice(expectedModel, actualModel));
  }

  /**
   * Create new office but name of office has been existed
   */
  @Test
  void when_saveOfficeWithExistName_thenThrowDuplicatedEntityException() {
    when(officeRepository.existsByName(anyString())).thenReturn(true);

    CreateOfficeModel paramModel = createCreateOfficeModel();
    OfficeService officeService = new OfficeService(officeRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(DuplicatedEntityException.class, () -> officeService.createOffice(paramModel));
  }

  /**
   * Delete a office and delete successfully
   */
  @Test
  void when_deleteExistOffice_thenDeleteSuccessfully() {
    OfficeModel officeModel = createOfficeModel();

    OfficeEntity savedEntity = modelMapper.map(officeModel, OfficeEntity.class);
    Optional<OfficeEntity> optional = Mockito.mock(Optional.class);
    when(officeRepository.findById(anyInt())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(savedEntity);
    when(officeRepository.save(any())).thenReturn(savedEntity);

    OfficeModel expectedModel = createOfficeModel();
    expectedModel.setStatus(OfficeStatusEnum.DISABLE.ordinal());

    OfficeModel actualModel = new OfficeService(officeRepository, modelMapper,
        sequenceGeneratorService).deleteOfficeById(savedEntity.getId());
    assertTrue(compareTwoOffice(expectedModel, actualModel));
  }

  /**
   * Delete office but can not find office by id
   */
  @Test
  void when_deleteNotExistOffice_thenThrowNoSuchEntityException() {
    Optional<OfficeEntity> optional = Mockito.mock(Optional.class);
    when(officeRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenThrow(NoSuchEntityException.class);

    OfficeService officeService = new OfficeService(officeRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(NoSuchEntityException.class, () -> officeService.deleteOfficeById(1));
  }

  /**
   * Delete office but status of office is disable
   */
  @Test
  void when_deleteOfficeWithDisableStatus_thenThrowNoSuchEntityException() {
    OfficeEntity foundOffice = modelMapper.map(createOfficeModel(), OfficeEntity.class);
    foundOffice.setStatus(OfficeStatusEnum.DISABLE.ordinal());

    Optional<OfficeEntity> optional = Mockito.mock(Optional.class);
    when(officeRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(foundOffice);

    OfficeService officeService = new OfficeService(officeRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(NoSuchEntityException.class, () -> officeService.deleteOfficeById(
        foundOffice.getId()));
  }

  /**
   * Find an office successfully
   */
  @Test
  void when_findExistOffice_thenReturnModelSuccessfully() {
    OfficeModel officeModel = createOfficeModel();

    OfficeEntity foundOffice = modelMapper.map(officeModel, OfficeEntity.class);
    Optional<OfficeEntity> optional = Mockito.mock(Optional.class);
    when(officeRepository.findById(anyInt())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(foundOffice);

    OfficeModel expectedModel = createOfficeModel();

    OfficeModel actualModel = new OfficeService(officeRepository, modelMapper,
        sequenceGeneratorService).findOfficeById(
        officeModel.getId());
    assertTrue(compareTwoOffice(expectedModel, actualModel));
  }

  /**
   * Find an office does not exist
   */
  @Test
  void when_findNotExistOffice_thenThrowNoSuchEntity() {
    Optional<OfficeEntity> optional = Mockito.mock(Optional.class);
    when(officeRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenThrow(NoSuchEntityException.class);

    OfficeService officeService = new OfficeService(officeRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(NoSuchEntityException.class, () -> officeService.findOfficeById(1));
  }

  /**
   * Update an existed office and update successfully
   */
  @Test
  void when_updateExistOffice_thenUpdateSuccessfully() {
    UpdateOfficeModel updateOfficeModel = createUpdateOfficeModel();

    OfficeEntity savedEntity = modelMapper.map(updateOfficeModel, OfficeEntity.class);
    Optional<OfficeEntity> optional = Mockito.mock(Optional.class);
    when(officeRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenReturn(
        modelMapper.map(createOfficeModel(), OfficeEntity.class));
    when(officeRepository.existsByNameAndIdNot(anyString(), anyInt())).thenReturn(false);
    when(officeRepository.save(any())).thenReturn(savedEntity);

    OfficeModel expectedModel = createOfficeModel();

    OfficeModel actualModel = new OfficeService(officeRepository, modelMapper,
        sequenceGeneratorService).updateOffice(createUpdateOfficeModel());
    assertTrue(compareTwoOffice(expectedModel, actualModel));
  }

  /**
   * Update office but can not find office by id
   */
  @Test
  void when_updateNotExistDepartment_thenThrowNoSuchEntityException() {
    Optional<OfficeEntity> optional = Mockito.mock(Optional.class);
    when(officeRepository.findById(any())).thenReturn(optional);
    when(optional.orElseThrow(any())).thenThrow(NoSuchEntityException.class);

    UpdateOfficeModel updateOfficeModel = createUpdateOfficeModel();
    OfficeService officeService = new OfficeService(officeRepository, modelMapper,
        sequenceGeneratorService);
    assertThrows(NoSuchEntityException.class, () -> officeService.updateOffice(updateOfficeModel));
  }

  /**
   * Find offices like name or location then return result
   */
  @Test
  void when_findOfficeLikeNameOrLocationSortByAsc_thenReturnResourceOfListOfOfficesByAsc() {
    List<OfficeEntity> entityList = new ArrayList<>();
    entityList.add(modelMapper.map(createOfficeModel(), OfficeEntity.class));
    Page<OfficeEntity> entityPage = new PageImpl<>(entityList);

    when(officeRepository.findAll(any(), (Pageable) any())).thenReturn(entityPage);

    OfficeService officeService = new OfficeService(officeRepository, modelMapper,
        sequenceGeneratorService);
    ResourceModel<OfficeModel> actualResource = officeService.
        searchOffices("", new RequestPaginationModel(0, 1, "id", "asc"));

    List<OfficeModel> modelList = new ArrayList<>();
    OfficeModel expectModel = createOfficeModel();
    modelList.add(expectModel);
    TestUtils<OfficeModel> testUtils = new TestUtils<>();
    ResourceModel<OfficeModel> expectedResource = testUtils
        .createResourceModel("", "asc", "id",
            1, 1, 0, 1, modelList);
    assertTrue(testUtils.compareTwoResourceInformation(expectedResource, actualResource));
    assertTrue(compareTwoOfficeList(expectedResource.getData(), actualResource.getData()));
  }
}
