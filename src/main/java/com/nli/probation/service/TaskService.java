package com.nli.probation.service;

import com.nli.probation.constant.EntityStatusEnum;
import com.nli.probation.converter.PaginationConverter;
import com.nli.probation.customexception.NoSuchEntityException;
import com.nli.probation.entity.TaskEntity;
import com.nli.probation.entity.UserAccountEntity;
import com.nli.probation.metamodel.TaskEntity_;
import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.task.CreateTaskModel;
import com.nli.probation.model.task.TaskModel;
import com.nli.probation.model.task.UpdateTaskModel;
import com.nli.probation.model.useraccount.UserAccountModel;
import com.nli.probation.repository.TaskRepository;
import com.nli.probation.repository.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserAccountRepository userAccountRepository;
    private final ModelMapper modelMapper;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final MongoTemplate mongoTemplate;

    public TaskService(TaskRepository taskRepository,
                       UserAccountRepository userAccountRepository,
                       ModelMapper modelMapper,
                       SequenceGeneratorService sequenceGeneratorService,
                       MongoTemplate mongoTemplate) {
        this.taskRepository = taskRepository;
        this.userAccountRepository = userAccountRepository;
        this.modelMapper = modelMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Create new task
     * @param createTaskModel
     * @return saved task
     */
    public TaskModel createTask(CreateTaskModel createTaskModel) {

        //Check assignee
        Optional<UserAccountEntity> existAccountOptional = userAccountRepository.findById(createTaskModel.getAssigneeId());

        //Prepare saved entity
        TaskEntity taskEntity = modelMapper.map(createTaskModel, TaskEntity.class);
        taskEntity.setId(sequenceGeneratorService.generateSequence(TaskEntity.SEQUENCE_NAME));
        taskEntity.setStatus(EntityStatusEnum.TaskStatusEnum.ACTIVE.ordinal());
        taskEntity.setLogWorkList(new ArrayList<>());
        existAccountOptional.ifPresentOrElse(existAccountEntity -> taskEntity.setUserAccountId(createTaskModel.getAssigneeId()),
                () -> taskEntity.setUserAccountId(0));
        taskEntity.setActualTime(0);

        //Save entity to DB
        TaskEntity savedEntity = taskRepository.save(taskEntity);
        TaskModel responseTaskModel = modelMapper.map(savedEntity, TaskModel.class);
        existAccountOptional.ifPresent(existAccountEntity -> responseTaskModel.setAssignee(modelMapper.map(existAccountEntity, UserAccountModel.class)));

        return responseTaskModel;
    }

    /**
     * Find task by id
     * @param id
     * @return found task
     */
    public TaskModel findTaskById(int id) {
        //Find task by id
        Optional<TaskEntity> searchedTaskOptional = taskRepository.findById(id);
        TaskEntity taskEntity = searchedTaskOptional.orElseThrow(() -> new NoSuchEntityException("Not found task"));
        TaskModel taskModel = modelMapper.map(taskEntity, TaskModel.class);

        //Check assignee id
        Optional<UserAccountEntity> accountOptional = userAccountRepository.findById(taskEntity.getUserAccountId());
        accountOptional.ifPresent(accountEntity -> taskModel.setAssignee(modelMapper.map(accountEntity, UserAccountModel.class)));

        return taskModel;
    }

    /**
     * Delete a task
     * @param id
     * @return deleted model
     */
    public TaskModel deleteTaskById(int id) {
        //Find task by id
        Optional<TaskEntity> deletedTaskOptional = taskRepository.findById(id);
        TaskEntity deletedTaskEntity = deletedTaskOptional.orElseThrow(() -> new NoSuchEntityException("Not found task with id"));
        if(deletedTaskEntity.getStatus() == EntityStatusEnum.TaskStatusEnum.DISABLE.ordinal())
            throw new NoSuchEntityException("This task was deleted");

        //Set status for entity
        deletedTaskEntity.setStatus(EntityStatusEnum.TaskStatusEnum.DISABLE.ordinal());

        //Save entity to DB
        TaskEntity responseEntity = taskRepository.save(deletedTaskEntity);
        TaskModel taskModel = modelMapper.map(responseEntity, TaskModel.class);
        if(deletedTaskEntity.getUserAccountId() > 0) {
            Optional<UserAccountEntity> accountOptional = userAccountRepository.findById(deletedTaskEntity.getUserAccountId());
            accountOptional.ifPresent(accountEntity -> taskModel.setAssignee(modelMapper.map(accountEntity, UserAccountModel.class)));
        }
        return taskModel;
    }

    /**
     * Update task information
     * @param updateTaskModel
     * @return updated task
     */
    public TaskModel updateTask (UpdateTaskModel updateTaskModel) {
        //Find task by id
        Optional<TaskEntity> foundTaskOptional = taskRepository.findById(updateTaskModel.getId());
        foundTaskOptional.orElseThrow(() -> new NoSuchEntityException("Not found task with id"));

        //Check assignee
        Optional<UserAccountEntity> existAccountOptional = userAccountRepository.findById(updateTaskModel.getAssigneeId());

        //Prepare saved entity
        TaskEntity taskEntity = modelMapper.map(updateTaskModel, TaskEntity.class);
        existAccountOptional.ifPresentOrElse(existAccount -> taskEntity.setUserAccountId(updateTaskModel.getAssigneeId()),
                () -> taskEntity.setUserAccountId(0));

        //Save entity to database
        TaskEntity savedEntity = taskRepository.save(taskEntity);
        TaskModel taskModel = modelMapper.map(savedEntity, TaskModel.class);
        existAccountOptional.ifPresent(existAccountEntity -> taskModel.setAssignee(modelMapper.map(existAccountEntity, UserAccountModel.class)));

        return taskModel;
    }

    /**
     * Search task like title
     * @param searchValue
     * @param paginationModel
     * @return resource of data
     */
    public ResourceModel<TaskModel> searchTasks(String searchValue, RequestPaginationModel paginationModel) {
        PaginationConverter<TaskModel, TaskEntity> paginationConverter = new PaginationConverter<>();

        //Build pageable
        String defaultSortBy = TaskEntity_.START_TIME;
        Pageable pageable = paginationConverter.convertToPageable(paginationModel, defaultSortBy, TaskEntity.class);

        //Create query object
        Query query = new Query(Criteria.where(TaskEntity_.TITLE).regex(".*" + searchValue + ".*")).with(pageable);

        //Find all tasks
        List<TaskEntity> taskEntityList = mongoTemplate.find(query, TaskEntity.class);
        Page<TaskEntity> taskEntityPage = PageableExecutionUtils.getPage(taskEntityList, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), TaskEntity.class));

        //Convert list of offices entity to list of task models
        List<TaskModel> taskModels = new ArrayList<>();
        for(TaskEntity entity : taskEntityPage) {
            TaskModel model = modelMapper.map(entity, TaskModel.class);
            if(entity.getUserAccountId() > 0) {
                Optional<UserAccountEntity> assigneeOptional = userAccountRepository.findById(entity.getUserAccountId());
                assigneeOptional.ifPresent(assigneeEntity -> model.setAssignee(modelMapper.map(assigneeEntity, UserAccountModel.class)));
            }
            taskModels.add(model);
        }

        //Prepare resource for return
        ResourceModel<TaskModel> resourceModel = new ResourceModel<>();
        resourceModel.setData(taskModels);
        resourceModel.setSearchText(searchValue);
        resourceModel.setSortBy(defaultSortBy);
        resourceModel.setSortType(paginationModel.getSortType());
        paginationConverter.buildPagination(paginationModel, taskEntityPage, resourceModel);
        return resourceModel;
    }

    /**
     * Assign/reassign a user for a task
     * @param taskId
     * @param userId
     * @return saved task model
     */
    public TaskModel assignTaskToUser(int taskId, int userId) {
        //Check task
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        TaskEntity taskEntity = taskOptional.orElseThrow(() -> new NoSuchEntityException("Not found task"));

        //Check user
        Optional<UserAccountEntity> userOptional = userAccountRepository.findById(userId);
        UserAccountEntity userEntity = userOptional.orElseThrow(() -> new NoSuchEntityException("Not found user account"));

        //Update user in task
        taskEntity.setUserAccountId(userId);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);

        //Prepare for response model
        TaskModel responseModel = modelMapper.map(savedTaskEntity, TaskModel.class);
        responseModel.setAssignee(modelMapper.map(userEntity, UserAccountModel.class));
        return  responseModel;
    }

    /**
     * Search task of user
     * @param searchValue
     * @param paginationModel
     * @param userId
     * @return resource contains list task of user
     */
    public ResourceModel<TaskModel> searchTasksOfUserId(String searchValue, RequestPaginationModel paginationModel, int userId) {
        //Check exist user account
        Optional<UserAccountEntity> accountOptional = userAccountRepository.findById(userId);
        accountOptional.orElseThrow(() -> new NoSuchEntityException("Not found user accpount"));

        PaginationConverter<TaskModel, TaskEntity> paginationConverter = new PaginationConverter<>();

        //Build pageable
        String defaultSortBy = TaskEntity_.START_TIME;
        Pageable pageable = paginationConverter.convertToPageable(paginationModel, defaultSortBy, TaskEntity.class);

        //Create query object
        Query query = new Query(Criteria.where(TaskEntity_.TITLE).regex(".*" + searchValue + ".*")
                .andOperator(Criteria.where(TaskEntity_.USER_ACCOUNT_ID).is(userId))).with(pageable);

        //Find all tasks
        List<TaskEntity> taskEntityList = mongoTemplate.find(query, TaskEntity.class);
        Page<TaskEntity> taskEntityPage = PageableExecutionUtils.getPage(taskEntityList, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), TaskEntity.class));

        //Convert list of offices entity to list of task models
        List<TaskModel> taskModels = new ArrayList<>();
        for(TaskEntity entity : taskEntityPage) {
            TaskModel model = modelMapper.map(entity, TaskModel.class);
            if(entity.getUserAccountId() > 0) {
                Optional<UserAccountEntity> assigneeOptional = userAccountRepository.findById(entity.getUserAccountId());
                assigneeOptional.ifPresent(assigneeEntity -> model.setAssignee(modelMapper.map(assigneeEntity, UserAccountModel.class)));
            }
            taskModels.add(model);
        }

        //Prepare resource for return
        ResourceModel<TaskModel> resourceModel = new ResourceModel<>();
        resourceModel.setData(taskModels);
        resourceModel.setSearchText(searchValue);
        resourceModel.setSortBy(defaultSortBy);
        resourceModel.setSortType(paginationModel.getSortType());
        paginationConverter.buildPagination(paginationModel, taskEntityPage, resourceModel);
        return resourceModel;
    }
}
