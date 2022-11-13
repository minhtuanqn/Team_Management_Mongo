package com.nli.probation.controller;

import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.ResponseModel;
import com.nli.probation.model.logwork.LogWorkModel;
import com.nli.probation.model.task.CreateTaskModel;
import com.nli.probation.model.task.TaskModel;
import com.nli.probation.model.task.UpdateTaskModel;
import com.nli.probation.resolver.annotation.RequestPagingParam;
import com.nli.probation.service.LogWorkService;
import com.nli.probation.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final LogWorkService logWorkService;

    public TaskController(TaskService taskService,
                          LogWorkService logWorkService
    ) {
        this.taskService = taskService;
        this.logWorkService = logWorkService;
    }

    /**
     * Create new task
     * @param createModel
     * @return response entity contains created model
     */
    @PostMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseModel> createTask(@Valid @RequestBody CreateTaskModel createModel) {
        TaskModel savedModel = taskService.createTask(createModel);
        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
                .data(savedModel)
                .message("OK");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    /**
     * Find task by id
     * @param id
     * @return response entity contains model
     */
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseModel> findTaskById(@PathVariable int id) {
        TaskModel foundTask = taskService.findTaskById(id);
        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
                .data(foundTask)
                .message("OK");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    /**
     * Delete a task by id
     * @param id
     * @return response entity contains deleted model
     */
    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseModel> deleteTask(@PathVariable int id) {
        TaskModel deletedModel = taskService.deleteTaskById(id);
        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
                .data(deletedModel)
                .message("OK");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);

    }

    /**
     * Update task
     * @param requestModel
     * @return response entity contains model
     */
    @PutMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseModel> updateTask(@Valid @RequestBody UpdateTaskModel requestModel) {
        TaskModel updatedModel = taskService.updateTask(requestModel);
        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
                .data(updatedModel)
                .message("OK");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

//    /**
//     * Search tasks
//     * @param requestPaginationModel
//     * @param searchText
//     * @return response entity contains data resource
//     */
//    @GetMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<Object> searchTasks(@RequestPagingParam RequestPaginationModel requestPaginationModel,
//                                              @RequestParam(value = "searchText", defaultValue = "") String searchText) {
//        ResourceModel<TaskModel> taskList = taskService.searchTasks(searchText, requestPaginationModel);
//        return new ResponseEntity<>(taskList, HttpStatus.OK);
//    }

    /**
     * Assign/reassign a task to an user account
     * @param id
     * @param userId
     * @return saved task model
     */
    @PutMapping(path = "{id}/user-accounts/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseModel> assignUserForTask(@PathVariable int id, @PathVariable int userId) {
        TaskModel taskModel = taskService.assignTaskToUser(id, userId);
        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
                .data(taskModel)
                .message("OK");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

//    /**
//     * Search log works of task
//     * @param requestPaginationModel
//     * @param id
//     * @param searchText
//     * @return resource of log works
//     */
//    @GetMapping(path = "{id}/log-works", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<Object> searchLogWorkOfTask(@RequestPagingParam RequestPaginationModel requestPaginationModel,
//                                                           @PathVariable int id,
//                                                           @RequestParam(value = "searchText", defaultValue = "") String searchText) {
//        ResourceModel<LogWorkModel> logList = logWorkService.searchLogWorkOfTask(id, searchText, requestPaginationModel);
//        return new ResponseEntity<>(logList, HttpStatus.OK);
//    }

    /**
     * Find log work
     * @param id
     * @param logId
     * @return found log work
     */
    @GetMapping(path = "/{id}/log-works/{logId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseModel> findLogWorkById(@PathVariable int id, @PathVariable String logId) {
        LogWorkModel foundLogWork = logWorkService.findLogWorkById(id, logId);
        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
                .data(foundLogWork)
                .message("OK");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    /**
     * Delete a log work
     * @param id
     * @param logId
     * @return deleted log work
     */
    @DeleteMapping(path = "/{id}/log-works/{logId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseModel> deleteLogWork(@PathVariable int id, @PathVariable String logId) {
        LogWorkModel deletedModel = logWorkService.deleteLogWorkById(id, logId);
        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
                .data(deletedModel)
                .message("OK");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);

    }

}
