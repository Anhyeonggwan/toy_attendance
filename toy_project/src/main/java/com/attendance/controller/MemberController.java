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

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class MemberController {
	
	private final MemberService memberService;
	
	@PostMapping("/normal/logout")
	public ResponseEntity<JSONObject> logout(@RequestParam Map<String, Object> map){
		return ResponseEntity.ok(memberService.logout(map));
	}
	
	@GetMapping("/isDupleId")
	public ResponseEntity<JSONObject> isDupleId(@RequestParam(name = "userId") String userId){
		return ResponseEntity.ok(memberService.isDupleIdpProc(userId));
	}

	@PostMapping("/signup")	// 회원가입
	public ResponseEntity<JSONObject> signupProc(Member member){	
		return ResponseEntity.ok(memberService.signupProc(member));
	}
	
	@GetMapping("/normal/userDetail")
	public ResponseEntity<JSONObject> getUserDetail(@RequestParam Map<String, String> map){
		return ResponseEntity.ok(memberService.getUserDetail(map));
	}
	
	@PostMapping("/login")
	public ResponseEntity<JSONObject> getLogin(@RequestParam Map<String, Object> map, HttpServletResponse response){
		return ResponseEntity.ok(memberService.getLogin(map, response));
	}

}
