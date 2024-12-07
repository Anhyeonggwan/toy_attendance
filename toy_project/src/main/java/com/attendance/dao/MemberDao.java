package com.attendance.dao;

import org.apache.ibatis.annotations.Mapper;

import com.attendance.vo.Member;

@Mapper
public interface MemberDao {
	
	public int insertMembe(Member member);
	public Member findMemberbyId(String userId);

}
