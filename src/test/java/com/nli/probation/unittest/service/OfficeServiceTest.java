package com.nli.probation.unittest.service;

import static com.nli.probation.utils.TestUtils.compareTwoOffice;
import static com.nli.probation.utils.TestUtils.createCreateOfficeModel;
import static com.nli.probation.utils.TestUtils.createOfficeModel;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nli.probation.customexception.DuplicatedEntityException;
import com.nli.probation.entity.OfficeEntity;
import com.nli.probation.model.office.CreateOfficeModel;
import com.nli.probation.model.office.OfficeModel;
import com.nli.probation.repository.OfficeRepository;
import com.nli.probation.service.OfficeService;
import com.nli.probation.service.SequenceGeneratorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

class OfficeServiceTest {

  private final OfficeRepository officeRepository = Mockito.mock(OfficeRepository.class);
  private final ModelMapper modelMapper = new ModelMapper();
  private final SequenceGeneratorService sequenceGeneratorService = Mockito.mock(SequenceGeneratorService.class);

  /**
   * Create new office and save successfully
   */
  @Test
  void when_saveOffice_thenSaveSuccessfully() {
    when(officeRepository.existsByName(anyString())).thenReturn(false);

    OfficeModel officeModel = createOfficeModel();
    OfficeEntity savedEntity = modelMapper.map(officeModel, OfficeEntity.class);
    when(officeRepository.save(any())).thenReturn(savedEntity);

    OfficeModel expectedModel = createOfficeModel();
    CreateOfficeModel paramModel = createCreateOfficeModel();
    OfficeModel actualModel = new OfficeService(officeRepository, modelMapper, sequenceGeneratorService).createOffice(paramModel);
    assertTrue(compareTwoOffice(expectedModel, actualModel));
  }

  /**
   * Create new office but name of office has been existed
   */
  @Test
  void when_saveOfficeWithExistName_thenThrowDuplicatedEntityException() {
    when(officeRepository.existsByName(anyString())).thenReturn(true);

    CreateOfficeModel paramModel = createCreateOfficeModel();
    OfficeService officeService = new OfficeService(officeRepository, modelMapper, sequenceGeneratorService);
    assertThrows(DuplicatedEntityException.class, () -> officeService.createOffice(paramModel));
  }


}
