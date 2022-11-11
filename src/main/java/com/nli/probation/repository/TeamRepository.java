package com.nli.probation.repository;

import com.nli.probation.entity.TeamEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<TeamEntity, Integer> {
    boolean existsByName(String name);
    boolean existsByShortName(String shortName);
    boolean existsByNameAndIdNot(String name, int id);
    boolean existsByShortNameAndIdNot(String shortName, int id);
}
