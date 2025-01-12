package com.attendance.service;

import java.util.Map;

import org.json.simple.JSONObject;

import com.attendance.vo.Member;

public interface MemberService {
	
	public JSONObject signupProc(Member member);	// 회원 가입
	public JSONObject isDupleIdpProc(String userId); // 회원 가입 시 아이디 중복 체크
	public JSONObject getLogin(Map<String, Object> map); // 로그인
	public JSONObject getUserDetail(Map<String, String> map);	// 회원 상세
	public JSONObject logout(Map<String, Object> map);

}
