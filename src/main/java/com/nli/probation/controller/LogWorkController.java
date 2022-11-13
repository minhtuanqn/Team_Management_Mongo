package com.nli.probation.controller;

import com.nli.probation.model.ResponseModel;
import com.nli.probation.model.logwork.CreateLogWorkModel;
import com.nli.probation.model.logwork.LogWorkModel;
import com.nli.probation.service.LogWorkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/log-works")
public class LogWorkController {
    private final LogWorkService logWorkService;

    public LogWorkController(LogWorkService logWorkService) {
        this.logWorkService = logWorkService;
    }

    /**
     * Create new log work
     * @param createModel
     * @return response entity contains created model
     */
    @PostMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseModel> createLogWork(@Valid @RequestBody CreateLogWorkModel createModel) {
        LogWorkModel savedModel = logWorkService.createLogWork(createModel);
        ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
                .data(savedModel)
                .message("OK");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }


}
