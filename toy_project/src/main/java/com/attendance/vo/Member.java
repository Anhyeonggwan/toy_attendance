package com.attendance.vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member { // 회원

  private int userIdx; // 사용자 고유 번호
  private String userId; // 사용자 ID
  private String userPassword; // 비밀번호
  private String userName; // 사용자 이름
  private String userPhone; // 사용자 전화번호
  private String userEmail; // 사용자 이메일
  private LocalDateTime userCreateDate; // 생성일
  private LocalDateTime userUpdateDate; // 수정일
  private int userFailCnt; // 로그인 실패 횟수
  private int userStatus; // 사용자 상태
  private String userGrade;

}
