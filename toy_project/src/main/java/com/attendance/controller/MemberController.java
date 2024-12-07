package com.attendance.controller;

import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.service.MemberService;
import com.attendance.vo.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class MemberController {
	
	private final MemberService memberService;
	
	@GetMapping("/isDupleId")
	public ResponseEntity<JSONObject> isDupleId(@RequestParam(name = "userId") String userId){
		return ResponseEntity.ok(memberService.isDupleIdpProc(userId));
	}

	@PostMapping("/signup")	// 회원가입
	public ResponseEntity<JSONObject> signupProc(Member member){	
		return ResponseEntity.ok(memberService.signupProc(member));
	}
	
	@GetMapping("/userDetail")
	public ResponseEntity<JSONObject> getUserDetail(Map<String, Object> map){
		return null;
	}

}
