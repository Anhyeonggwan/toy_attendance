package com.attendance.service;

import org.json.simple.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.attendance.dao.MemberDao;
import com.attendance.util.ApiException;
import com.attendance.vo.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{
	
	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public JSONObject isDupleIdpProc(String userId) {
		log.info("userid >>> " + userId);
		JSONObject object = new JSONObject();
		
		if(ObjectUtils.isEmpty(userId)) throw new ApiException("500", "필수 값이 없습니다.");
		
		object.put("code", "200");
		object.put("message", "사용 가능한 아이디입니다.");
		object.put("data", true);
		
		Member member = memberDao.findMemberbyId(userId);
		log.info("member >>> " + member);
		
		if(member != null) {
			object.put("message", "이미 사용 중인 아이디입니다.");
			object.put("data", false);
		}
		
		return object;
	}

	@Override
	public JSONObject signupProc(Member member) {
		JSONObject object = new JSONObject();
		
		String userId = member.getUserId();
		String userPw = member.getUserPassword();
		String name = member.getUserName();
		String phone = member.getUserPhone();
		String email = member.getUserEmail();
		
		if(ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(userPw) || ObjectUtils.isEmpty(name) || ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(email)) {
			throw new ApiException("500", "필수값이 없습니다.");
		}
		
		object = insertMember(member);	// 회원 insert
		
		return object;
	}
	
	// 회원 insert
	private JSONObject insertMember(Member member) {
		JSONObject object = new JSONObject();
		member.setUserPassword(passwordEncoder.encode(member.getUserPassword())); // sha-256 암호화 알고리즘
		int result = memberDao.insertMembe(member);
		if(result < 0) {
			throw new ApiException("500", "처리 중 에러가 발생하였습니다.");
		}
		
		object.put("code", "200");
		object.put("message", "정상적으로 처리 되었습니다.");
		
		return object;
	}
	
}

































