package com.nli.probation.repository;

import com.nli.probation.entity.RoleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<RoleEntity, Integer> {

  boolean existsByName(String name);

  boolean existsByShortName(String shortName);

  boolean existsByNameAndIdNot(String name, int id);

  boolean existsByShortNameAndIdNot(String shortName, int id);
}
