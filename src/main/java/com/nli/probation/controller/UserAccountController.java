//package com.nli.probation.controller;
//
//import com.nli.probation.model.RequestPaginationModel;
//import com.nli.probation.model.ResourceModel;
//import com.nli.probation.model.ResponseModel;
//import com.nli.probation.model.task.TaskModel;
//import com.nli.probation.model.useraccount.CreateUserAccountModel;
//import com.nli.probation.model.useraccount.UpdateUserAccountModel;
//import com.nli.probation.model.useraccount.UserAccountModel;
//import com.nli.probation.resolver.annotation.RequestPagingParam;
//import com.nli.probation.service.TaskService;
//import com.nli.probation.service.UserAccountService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@RestController
//@Validated
//@RequestMapping("/user-accounts")
//public class UserAccountController {
//    private final UserAccountService userAccountService;
//    private final TaskService taskService;
//
//    public UserAccountController(UserAccountService userAccountService,
//                                 TaskService taskService) {
//        this.userAccountService = userAccountService;
//        this.taskService = taskService;
//    }
//
//    /**
//     * Create new user account
//     * @param createModel
//     * @return created user account
//     */
//    @PostMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<ResponseModel> createUserAccount(@Valid @RequestBody CreateUserAccountModel createModel) {
//        UserAccountModel savedModel = userAccountService.createUserAccount(createModel);
//        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
//                .data(savedModel)
//                .message("OK");
//        return new ResponseEntity<>(responseModel, HttpStatus.OK);
//    }
//
//    /**
//     * Find user account by id
//     * @param id
//     * @return response entity contains model
//     */
//    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<ResponseModel> findUserAccountById(@PathVariable int id) {
//        UserAccountModel foundUserAccount = userAccountService.findUserAccountById(id);
//        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
//                .data(foundUserAccount)
//                .message("OK");
//        return new ResponseEntity<>(responseModel, HttpStatus.OK);
//    }
//
//    /**
//     * Delete a user account by id
//     * @param id
//     * @return response entity contains deleted model
//     */
//    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<ResponseModel> deleteUserAccount(@PathVariable int id) {
//        UserAccountModel deletedModel = userAccountService.deleteUserAccountById(id);
//        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
//                .data(deletedModel)
//                .message("OK");
//        return new ResponseEntity<>(responseModel, HttpStatus.OK);
//
//    }
//
//    /**
//     * Update user account
//     * @param requestModel
//     * @return response entity contains model
//     */
//    @PutMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<ResponseModel> updateUserAccount(@Valid @RequestBody UpdateUserAccountModel requestModel) {
//        UserAccountModel updatedModel = userAccountService.updateUserAccount(requestModel);
//        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
//                .data(updatedModel)
//                .message("OK");
//        return new ResponseEntity<>(responseModel, HttpStatus.OK);
//    }
//
//    /**
//     * Search user accounts
//     * @param requestPaginationModel
//     * @param searchText
//     * @return response entity contains data resource
//     */
//    @GetMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<Object> searchTeams(@RequestPagingParam RequestPaginationModel requestPaginationModel,
//                                              @RequestParam(value = "searchText", defaultValue = "") String searchText) {
//        ResourceModel<UserAccountModel> accountList = userAccountService
//                .searchAccounts(searchText, requestPaginationModel, 0);
//        return new ResponseEntity<>(accountList, HttpStatus.OK);
//    }
//
//    /**
//     * Search task of an user account
//     * @param requestPaginationModel
//     * @param id
//     * @param searchText
//     * @return response entity contains list of tasks
//     */
//    @GetMapping(path = "{id}/tasks", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<Object> searchTasks(@RequestPagingParam RequestPaginationModel requestPaginationModel,
//                                              @PathVariable int id,
//                                              @RequestParam(value = "searchText", defaultValue = "") String searchText) {
//        ResourceModel<TaskModel> taskList = taskService.searchTasksOfUserId(searchText, requestPaginationModel, id);
//        return new ResponseEntity<>(taskList, HttpStatus.OK);
//    }
//}
