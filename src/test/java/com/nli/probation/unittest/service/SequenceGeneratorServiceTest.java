package com.nli.probation.unittest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nli.probation.service.SequenceGeneratorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;

class SequenceGeneratorServiceTest {

  private final MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);

  /**
   * Get sequence id of document successfully
   */
  @Test
  void when_getStartSequenceNumberOfDocument_thenReturnNumberSuccessfully() {
    when(mongoOperations.findAndModify(any(), any(),
        (Class<Object>) any(), any())).thenReturn(null);
    SequenceGeneratorService sequenceGeneratorService = new SequenceGeneratorService(
        mongoOperations);
    int actual = sequenceGeneratorService.generateSequence("seqName");
    assertEquals(1, actual);
  }
}
