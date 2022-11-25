package com.nli.probation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseModel {

  private String message;

  private Object data;

  private int statusCode;

  public ResponseModel statusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public ResponseModel data(Object data) {
    this.data = data;
    return this;
  }

  public ResponseModel message(String message) {
    this.message = message;
    return this;
  }
}
