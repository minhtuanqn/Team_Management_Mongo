package com.nli.probation.service;

import com.nli.probation.constant.EntityStatusEnum;
import com.nli.probation.converter.PaginationConverter;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.customexception.TimeCustomException;
import com.nli.probation.entity.LogWorkEntity;
import com.nli.probation.entity.TaskEntity;
import com.nli.probation.metamodel.LogWorkEntity_;
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
        if(logWorkEntities == null)
            logWorkEntities = new ArrayList<>();
        logWorkEntities.add(logWorkEntity);
        existedTaskEntity.setLogWorkList(logWorkEntities);

        //Save entity to DB
        TaskEntity savedEntity = taskRepository.save(existedTaskEntity);
        return modelMapper.map(savedEntity, LogWorkModel.class);

    }

//    /**
//     * Find log work by id
//     * @param id
//     * @return found log work
//     */
//    public LogWorkModel findLogWorkById(int id) {
//        //Find log work by id
//        Optional<LogWorkEntity> searchedLogOptional = logWorkRepository.findById(id);
//        LogWorkEntity logWorkEntity = searchedLogOptional.orElseThrow(() -> new NoSuchEntityException("Not found log work"));
//        LogWorkModel logWorkModel = modelMapper.map(logWorkEntity, LogWorkModel.class);
//        logWorkModel.setTaskModel(modelMapper.map(logWorkEntity.getTaskEntity(), TaskModel.class));
//        return logWorkModel;
//    }
//
//    /**
//     * Delete a log work
//     * @param id
//     * @return deleted model
//     */
//    public LogWorkModel deleteLogWorkById(int id) {
//        //Find log work by id
//        Optional<LogWorkEntity> deletedLogWorkOptional = logWorkRepository.findById(id);
//        LogWorkEntity deletedLogEntity = deletedLogWorkOptional.orElseThrow(() -> new NoSuchEntityException("Not found log with id"));
//
//        //Set status for entity
//        deletedLogEntity.setStatus(EntityStatusEnum.LogWorkStatusEnum.DISABLE.ordinal());
//
//        //Save entity to DB
//        LogWorkEntity responseEntity = logWorkRepository.save(deletedLogEntity);
//        LogWorkModel logWorkModel = modelMapper.map(responseEntity, LogWorkModel.class);
//        logWorkModel.setTaskModel(modelMapper.map(responseEntity.getTaskEntity(), TaskModel.class));
//        return logWorkModel;
//    }
//
//    /**
//     * Update log work information
//     * @param updateLogWorkModel
//     * @return updated log work
//     */
//    public LogWorkModel updateLogWork (UpdateLogWorkModel updateLogWorkModel) {
//        //Find log work by id
//        Optional<LogWorkEntity> foundLogOptional = logWorkRepository.findById(updateLogWorkModel.getId());
//        LogWorkEntity foundLogEntity = foundLogOptional
//                .orElseThrow(() -> new NoSuchEntityException("Not found log work with id"));
//
//        //Check task
//        Optional<TaskEntity> existTaskOptional = taskRepository.findById(updateLogWorkModel.getTaskId());
//        TaskEntity existTaskEntity = existTaskOptional.orElseThrow(() -> new NoSuchEntityException("Not found task"));
//
//        //Check time
//        if(updateLogWorkModel.getStartTime().isAfter(updateLogWorkModel.getEndTime()))
//            throw new TimeCustomException("Check time of log work again");
//
//        //Prepare saved entity
//        LogWorkEntity logWorkEntity = modelMapper.map(updateLogWorkModel, LogWorkEntity.class);
//        double newTimeOfLog = Duration.between(logWorkEntity.getStartTime(), logWorkEntity.getEndTime()).toMinutes() / 60.0;
//        double oldTimeOfLog = Duration.between(logWorkEntity.getStartTime(), logWorkEntity.getEndTime()).toMinutes() / 60.0;
//        double newActualTimeOfTask = existTaskEntity.getActualTime() - oldTimeOfLog + newTimeOfLog;
//        existTaskEntity.setActualTime(newActualTimeOfTask);
//        logWorkEntity.setTaskEntity(existTaskEntity);
//
//        //Save entity to database
//        LogWorkEntity savedEntity = logWorkRepository.save(logWorkEntity);
//        LogWorkModel logWorkModel = modelMapper.map(savedEntity, LogWorkModel.class);
//        logWorkModel.setTaskModel(modelMapper.map(existTaskEntity, TaskModel.class));
//        return logWorkModel;
//    }
//
//    /**
//     * Specification for search log work by task entity
//     * @param taskEntity
//     * @return specification
//     */
//    private Specification<LogWorkEntity> belongToTask(TaskEntity taskEntity) {
//        return ((root, query, criteriaBuilder) -> {
//            return criteriaBuilder.equal(root.get(LogWorkEntity_.TASK_ENTITY), taskEntity);
//        });
//    }
//
//    /**
//     * Search log work of task
//     * @param taskId
//     * @param searchValue
//     * @param paginationModel
//     * @return resource of log work
//     */
//    public ResourceModel<LogWorkModel> searchLogWorkOfTask(int taskId, String searchValue, RequestPaginationModel paginationModel) {
//        PaginationConverter<LogWorkModel, LogWorkEntity> paginationConverter = new PaginationConverter<>();
//
//        //Check task
//        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
//        TaskEntity taskEntity = taskOptional.orElseThrow(() -> new NoSuchEntityException("Not found task"));
//
//        //Build pageable
//        String defaultSortBy = LogWorkEntity_.START_TIME;
//        Pageable pageable = paginationConverter.convertToPageable(paginationModel, defaultSortBy, LogWorkEntity.class);
//
//        //Find all log work
//        Page<LogWorkEntity> logEntityPage = logWorkRepository.findAll(belongToTask(taskEntity), pageable);
//
//        //Convert list of task entity to list of log models
//        List<LogWorkModel> logModels = new ArrayList<>();
//        for(LogWorkEntity entity : logEntityPage) {
//            LogWorkModel model = modelMapper.map(entity, LogWorkModel.class);
//            logModels.add(model);
//        }
//
//        //Prepare resource for return
//        ResourceModel<LogWorkModel> resourceModel = new ResourceModel<>();
//        resourceModel.setData(logModels);
//        resourceModel.setSearchText(searchValue);
//        resourceModel.setSortBy(defaultSortBy);
//        resourceModel.setSortType(paginationModel.getSortType());
//        paginationConverter.buildPagination(paginationModel, logEntityPage, resourceModel);
//        return resourceModel;
//    }
}
