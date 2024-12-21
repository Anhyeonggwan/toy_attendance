package com.attendance.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.attendance.vo.Member;

@Mapper
public interface MemberDao {
	
	public int insertMembe(Member member);
	public Member findMemberbyId(String userId);
	public Member getLogin(Map<String, Object> map);
	public Member findUserByIdx(String idx);

}
