package com.attendance.service;

import org.json.simple.JSONObject;

import com.attendance.vo.Member;

public interface MemberService {
	
	public JSONObject signupProc(Member member);	// 회원 가입
	public JSONObject isDupleIdpProc(String userId); // 회원 가입 시 아이디 중복 체크

}
