package com.nli.probation.repository;

import com.nli.probation.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<TeamEntity, Integer>, JpaSpecificationExecutor<TeamEntity> {
    boolean existsByName(String name);

    boolean existsByShortName(String shortName);

    boolean existsByNameAndIdNot(String name, int id);

    boolean existsByShortNameAndIdNot(String shortName, int id);
}
