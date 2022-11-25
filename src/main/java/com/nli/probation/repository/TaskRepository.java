package com.nli.probation.repository;

import com.nli.probation.entity.TaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskEntity, Integer> {

}
