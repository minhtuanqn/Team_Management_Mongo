package com.nli.probation.repository;

import com.nli.probation.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer>, JpaSpecificationExecutor<RoleEntity> {
    boolean existsByName(String name);
    boolean existsByShortName(String shortName);
    boolean existsByNameAndIdNot(String name, int id);
    boolean existsByShortNameAndIdNot(String shortName, int id);
}
