package com.nli.probation.service;

import com.nli.probation.constant.EntityStatusEnum;
import com.nli.probation.converter.PaginationConverter;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.customexception.TimeCustomException;
import com.nli.probation.entity.LogWorkEntity;
import com.nli.probation.entity.TaskEntity;
import com.nli.probation.metamodel.LogWorkEntity_;
import com.nli.probation.metamodel.TaskEntity_;
import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.logwork.CreateLogWorkModel;
import com.nli.probation.model.logwork.LogWorkModel;
import com.nli.probation.model.logwork.UpdateLogWorkModel;
import com.nli.probation.model.task.TaskModel;
import com.nli.probation.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LogWorkService {
    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;

    public LogWorkService(ModelMapper modelMapper,
                          TaskRepository taskRepository) {
        this.modelMapper = modelMapper;
        this.taskRepository = taskRepository;
    }

    /**
     * Create new log work
     * @param createLogWorkModel
     * @return saved log work
     */
    public LogWorkModel createLogWork(CreateLogWorkModel createLogWorkModel) {
        //Check exist task
        Optional<TaskEntity> existedTaskOptional = taskRepository.findById(createLogWorkModel.getTaskId());
        TaskEntity existedTaskEntity = existedTaskOptional
                .orElseThrow(() -> new NoSuchEntityException("Not found task"));

        //Check time
        if(createLogWorkModel.getStartTime().isAfter(createLogWorkModel.getEndTime()))
            throw new TimeCustomException("Check time of log work again");

        //Prepare saved entity
        LogWorkEntity logWorkEntity = modelMapper.map(createLogWorkModel, LogWorkEntity.class);
        logWorkEntity.setStatus(EntityStatusEnum.LogWorkStatusEnum.ACTIVE.ordinal());
        logWorkEntity.setId(ObjectId.get().toString());
        double totalTime = existedTaskEntity.getActualTime()
                + Duration.between(logWorkEntity.getStartTime(), logWorkEntity.getEndTime()).toMinutes() / 60.0;
        existedTaskEntity.setActualTime(totalTime);
        List<LogWorkEntity> logWorkEntities = existedTaskEntity.getLogWorkList();
        if(logWorkEntities == null) logWorkEntities = new ArrayList<>();
        logWorkEntities.add(logWorkEntity);
        existedTaskEntity.setLogWorkList(logWorkEntities);

        //Save entity to DB
        taskRepository.save(existedTaskEntity);
        return modelMapper.map(logWorkEntity, LogWorkModel.class);

    }

    /**
     * Find log work by id
     * @param taskId
     * @param logWorkId
     * @return log work model
     */
    public LogWorkModel findLogWorkById(int taskId, String logWorkId) {
        //Find task by id
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        TaskEntity taskEntity = taskOptional.orElseThrow(() -> new NoSuchEntityException("Not found task with id"));

        //Find log work in list
        for (LogWorkEntity logWorkEntity: taskEntity.getLogWorkList()) {
            if(logWorkEntity.getId().equals(logWorkId)) {
                return modelMapper.map(logWorkEntity, LogWorkModel.class);
            }
        }

        throw new NoSuchEntityException("Not found log work with id");
    }

    /**
     * Delete a log work
     * @param taskId
     * @param logWorkId
     * @return deleted log work
     */
    public LogWorkModel deleteLogWorkById(int taskId, String logWorkId) {
        //Find task by id
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        TaskEntity taskEntity = taskOptional.orElseThrow(() -> new NoSuchEntityException("Not found task with id"));

        //Find log work in list and update disable status
        int index = Integer.MIN_VALUE;
        for (LogWorkEntity logWorkEntity: taskEntity.getLogWorkList()) {
            if(logWorkEntity.getId().equals(logWorkId)) {
                logWorkEntity.setStatus(EntityStatusEnum.LogWorkStatusEnum.DISABLE.ordinal());
                index = taskEntity.getLogWorkList().indexOf(logWorkEntity);
                break;
            }
        }

        //Check exist log work
        if(index < 0) throw  new NoSuchEntityException(" Not found log work with id");

        //Save entity to DB
        TaskEntity responseEntity = taskRepository.save(taskEntity);
        return modelMapper.map(responseEntity.getLogWorkList().get(index), LogWorkModel.class);
    }

    /**
     * Update log work
     * @param taskId
     * @param updateLogWorkModel
     * @return updated log work
     */
    public LogWorkModel updateLogWork (int taskId, UpdateLogWorkModel updateLogWorkModel) {
        //Find task by id
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        TaskEntity taskEntity = taskOptional.orElseThrow(() -> new NoSuchEntityException("Not found task with id"));

        //Find log work in list
        LogWorkEntity foundLogWork = null;
        for (LogWorkEntity entity: taskEntity.getLogWorkList()) {
            if(entity.getId().equals(updateLogWorkModel.getId())) {
                foundLogWork = entity;
                break;
            }
        }

        //Check exist log work
        if(foundLogWork == null)
            throw  new NoSuchEntityException(" Not found log work with id");

        //Check time
        if(updateLogWorkModel.getStartTime().isAfter(updateLogWorkModel.getEndTime()))
            throw new TimeCustomException("Check time of log work again");

        //Prepare saved entity
        LogWorkEntity updatedLogWork = modelMapper.map(updateLogWorkModel, LogWorkEntity.class);
        double newTimeOfLog = Duration.between(updatedLogWork.getStartTime(), updatedLogWork.getEndTime()).toMinutes() / 60.0;
        double oldTimeOfLog = Duration.between(foundLogWork.getStartTime(), foundLogWork.getEndTime()).toMinutes() / 60.0;
        double newActualTimeOfTask = taskEntity.getActualTime() - oldTimeOfLog + newTimeOfLog;
        taskEntity.setActualTime(newActualTimeOfTask);
        int index = taskEntity.getLogWorkList().indexOf(foundLogWork);
        taskEntity.getLogWorkList().set(index, updatedLogWork);

        //Save entity to database
        TaskEntity savedEntity = taskRepository.save(taskEntity);
        return modelMapper.map(savedEntity.getLogWorkList().get(index), LogWorkModel.class);
    }

    /**
     * Find all log works of a task
     * @param taskId
     * @return
     */
    public List<LogWorkModel> findAllLogWorkOfTask(int taskId) {
        //Find task by id
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        TaskEntity taskEntity = taskOptional.orElseThrow(() -> new NoSuchEntityException("Not found task with id"));

        //Find log work in list
        List<LogWorkModel> logWorkModels = new ArrayList<>();
        for (LogWorkEntity entity: taskEntity.getLogWorkList()) {
            logWorkModels.add(modelMapper.map(entity, LogWorkModel.class));
        }

        return logWorkModels;
    }
}
