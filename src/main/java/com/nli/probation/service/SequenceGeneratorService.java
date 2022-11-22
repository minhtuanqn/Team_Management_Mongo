package com.nli.probation.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import com.nli.probation.entity.DBSequencesEntity;
import java.util.Objects;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class SequenceGeneratorService {

  private final MongoOperations mongoOperations;

  public SequenceGeneratorService(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  /**
   * Generate sequence number for mongo DB
   *
   * @param seqName
   * @return sequence number
   */
  public int generateSequence(String seqName) {
    //Get sequence number
    Query query = new Query(Criteria.where("id").is(seqName));
    //Update the sequence number
    Update update = new Update().inc("seq", 1);
    //Modify in document
    DBSequencesEntity counter = mongoOperations.findAndModify(query,
        update, options().returnNew(true).upsert(true),
        DBSequencesEntity.class);
    return !Objects.isNull(counter) ? counter.getSeq() : 1;
  }
}
