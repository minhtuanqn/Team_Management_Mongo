package com.nli.probation.repository;

import com.nli.probation.entity.OfficeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeRepository extends MongoRepository<OfficeEntity, Integer> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, int id);
}
