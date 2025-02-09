package com.attendance.service;

import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.attendance.config.JwtProvider;
import com.attendance.config.RefreshToken;
import com.attendance.dao.MemberDao;
import com.attendance.dao.RefreshTokenRepository;
import com.attendance.util.ApiException;
import com.attendance.vo.Member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

// access token은 유효 기간이 짧기 때문에 탈취하더라도 오랫동안 사용할 수 없다. 
// 하지만 refresh token을 탈취당한다면 refresh token이 만료되기 전까지 이 refresh token으로 계속 access token을 발급해낼 수 있다. 
// 심지어 refresh token의 유효 기간을 길게 설정했기 때문에 오랜 기간 사용자인 체 요청을 할 수 있다. 
// 이를 해결하기 위한 방법은 RTR(refresh token rotation)이 존재한다.

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{
	
	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final RefreshTokenRepository repository;
	
	@Override
	public JSONObject isDupleIdpProc(String userId) {
		JSONObject object = new JSONObject();
		
		if(ObjectUtils.isEmpty(userId)) throw new ApiException("500", "필수 값이 없습니다.");
		
		object.put("code", "200");
		object.put("message", "사용 가능한 아이디입니다.");
		object.put("data", true);
		
		Member member = memberDao.findMemberbyId(userId);
		
		if(member != null) {
			object.put("message", "이미 사용 중인 아이디입니다.");
			object.put("data", false);
		}
		
		return object;
	}

	@Override
	@Transactional
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
		
		if(!(boolean)isDupleIdpProc(member.getUserId()).get("data")) {
			throw new ApiException("500", "이미 사용중인 아이디입니다.");
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
	
	
	@Override
	public JSONObject getLogin(Map<String, Object> map, HttpServletResponse response) {
		
		String user_id = map.get("user_id").toString();
		String password = map.get("password").toString();
		
		if(ObjectUtils.isEmpty(user_id) || ObjectUtils.isEmpty(password)) throw new ApiException("500", "필수값이 없습니다.");
		
		Member member = memberDao.findMemberbyId(user_id);
		
		if(member == null) throw new ApiException("500", "아이디 또는 비밀번호가 올바르지 않습니다.");
		
		if(!passwordEncoder.matches(password, member.getUserPassword())) throw new ApiException("500", "아이디 또는 비밀번호가 올바르지 않습니다.");
		
		// jwt 토큰 생성
		String accessToken = jwtProvider.generateAccessToken(member.getUserIdx());
		
		// 기존에 가지고 있는 사용자의 refresh token 제거
		RefreshToken.removeUserRemoveRefreshToken(member.getUserId());
		
		// refresh token 생성 후 저장
		String reFreshToken = jwtProvider.generateRefreshToken(member.getUserId()); 
		RefreshToken.putRefreshToken(reFreshToken, member.getUserId());
		
		repository.save(new com.attendance.vo.RefreshToken(reFreshToken, member.getUserIdx()));
		
		setAccesstokenCookie(response, accessToken);
		
		JSONObject object = new JSONObject();
		object.put("code", "200");
		object.put("accessToken", accessToken);
		object.put("refreshToken", reFreshToken);
		object.put("idx", member.getUserIdx());
		
		return object;
	}
	
	private void setAccesstokenCookie(HttpServletResponse response, String accessToken) {
		Cookie cookie = new Cookie("accessToken", accessToken);
		cookie.setHttpOnly(true); // 자바스크립트에서 사용 불가
		//cookie.setSecure(true); // HTTPS에서만 전송 (배포 환경에서 사용)
	    cookie.setMaxAge(1800); // 쿠키 만료 시간 (30분)
	    cookie.setPath("/");
	    
	    response.addCookie(cookie);
	}
	
	@Override
	public JSONObject logout(Map<String, Object> map) {
		
		JSONObject obj = new JSONObject();
		
		String memberIdx = map.get("user_idx").toString();
		String user_id = map.get("user_id").toString();
		
		if(ObjectUtils.isEmpty(memberIdx) || ObjectUtils.isEmpty(user_id)) throw new ApiException("503", "필수값이 존재하지 않습니다.");
		
		String refreshToken = RefreshToken.getRefreshTokenFromId(user_id);
		RefreshToken.removeUserRemoveRefreshToken(user_id);
		
		repository.deleteRefreshToken(refreshToken);
		
		obj.put("code", "200");
		obj.put("message", "로그아웃하였습니다.");
		
		return obj;
	}
	
	@Override
	public JSONObject getUserDetail(Map<String, String> map) {
		JSONObject object = new JSONObject();
		
		String idx = map.get("idx").toString();
		
		if(ObjectUtils.isEmpty(idx)) throw new ApiException("500", "필수 값이 없습니다.");
		
		
		try {
			Integer.parseInt(idx);
		}catch (NumberFormatException e) {
			throw new ApiException("500", "처리 중 에러가 발생하였습니다.");
		}
		
		Member member = memberDao.findUserByIdx(idx);
		
		if(member == null) throw new ApiException("500", "회원이 존재하지 않습니다.");
		
		object.put("code", "200");
		object.put("message", "success");
		object.put("data", member);
		
		return object;
	}
}

































