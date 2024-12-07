package com.attendance.controller;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.attendance.util.ApiException;

@RestControllerAdvice
public class ExceptionHandler {
	
	@org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
    public ResponseEntity<JSONObject> handleIllegalArgumentException(ApiException ex) {
		JSONObject obj = new JSONObject();
		obj.put("code", ex.getCode());
		obj.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(obj);
    }

}
