package com.nli.probation.controller;

import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.ResponseModel;
import com.nli.probation.model.role.CreateRoleModel;
import com.nli.probation.model.role.RoleModel;
import com.nli.probation.model.role.UpdateRoleModel;
import com.nli.probation.resolver.annotation.RequestPagingParam;
import com.nli.probation.service.RoleService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/roles")
public class RoleController {

  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  /**
   * Create new role
   *
   * @param createModel
   * @return response entity contains created model
   */
  @PostMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseModel> createRole(@Valid @RequestBody CreateRoleModel createModel) {
    RoleModel savedModel = roleService.createRole(createModel);
    ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
        .data(savedModel)
        .message("OK");
    return new ResponseEntity<>(responseModel, HttpStatus.OK);
  }

  /**
   * Find role by id
   *
   * @param id
   * @return response entity contains model
   */
  @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseModel> findRoleById(@PathVariable int id) {
    RoleModel foundRole = roleService.findRoleById(id);
    ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
        .data(foundRole)
        .message("OK");
    return new ResponseEntity<>(responseModel, HttpStatus.OK);
  }

  /**
   * Delete a role by id
   *
   * @param id
   * @return response entity contains deleted model
   */
  @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseModel> deleteRole(@PathVariable int id) {
    RoleModel deletedModel = roleService.deleteRoleById(id);
    ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
        .data(deletedModel)
        .message("OK");
    return new ResponseEntity<>(responseModel, HttpStatus.OK);

  }

  /**
   * Update role
   *
   * @param requestModel
   * @return response entity contains model
   */
  @PutMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseModel> updateRole(
      @Valid @RequestBody UpdateRoleModel requestModel) {
    RoleModel updatedModel = roleService.updateRole(requestModel);
    ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
        .data(updatedModel)
        .message("OK");
    return new ResponseEntity<>(responseModel, HttpStatus.OK);
  }

  /**
   * Search roles
   *
   * @param requestPaginationModel
   * @param searchText
   * @return response entity contains data resource
   */
  @GetMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Object> searchRoles(
      @RequestPagingParam RequestPaginationModel requestPaginationModel,
      @RequestParam(value = "searchText", defaultValue = "") String searchText) {
    ResourceModel<RoleModel> roleList = roleService.searchRoles(searchText, requestPaginationModel);
    return new ResponseEntity<>(roleList, HttpStatus.OK);
  }
}
