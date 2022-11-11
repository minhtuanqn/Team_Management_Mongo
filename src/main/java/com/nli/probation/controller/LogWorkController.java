//package com.nli.probation.controller;
//
//import com.nli.probation.model.ResponseModel;
//import com.nli.probation.model.logwork.CreateLogWorkModel;
//import com.nli.probation.model.logwork.LogWorkModel;
//import com.nli.probation.model.logwork.UpdateLogWorkModel;
//import com.nli.probation.service.LogWorkService;
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
//@RequestMapping("/log-works")
//public class LogWorkController {
//    private final LogWorkService logWorkService;
//
//    public LogWorkController(LogWorkService logWorkService) {
//        this.logWorkService = logWorkService;
//    }
//
//    /**
//     * Create new log work
//     * @param createModel
//     * @return response entity contains created model
//     */
//    @PostMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<ResponseModel> createLogWork(@Valid @RequestBody CreateLogWorkModel createModel) {
//        LogWorkModel savedModel = logWorkService.createLogWork(createModel);
//        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
//                .data(savedModel)
//                .message("OK");
//        return new ResponseEntity<>(responseModel, HttpStatus.OK);
//    }
//
//    /**
//     * Find log work by id
//     * @param id
//     * @return response entity contains model
//     */
//    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<ResponseModel> findLogWorkById(@PathVariable int id) {
//        LogWorkModel foundLogWork = logWorkService.findLogWorkById(id);
//        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
//                .data(foundLogWork)
//                .message("OK");
//        return new ResponseEntity<>(responseModel, HttpStatus.OK);
//    }
//
//    /**
//     * Delete a log work by id
//     * @param id
//     * @return response entity contains deleted model
//     */
//    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<ResponseModel> deleteLogWork(@PathVariable int id) {
//        LogWorkModel deletedModel = logWorkService.deleteLogWorkById(id);
//        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
//                .data(deletedModel)
//                .message("OK");
//        return new ResponseEntity<>(responseModel, HttpStatus.OK);
//
//    }
//
//    /**
//     * Update log work
//     * @param requestModel
//     * @return response entity contains model
//     */
//    @PutMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<ResponseModel> updateLogWork(@Valid @RequestBody UpdateLogWorkModel requestModel) {
//        LogWorkModel updatedModel = logWorkService.updateLogWork(requestModel);
//        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
//                .data(updatedModel)
//                .message("OK");
//        return new ResponseEntity<>(responseModel, HttpStatus.OK);
//    }
//}
