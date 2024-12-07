package com.attendance.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException{
	
	private String code;
	private String message;

}
