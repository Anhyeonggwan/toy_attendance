package com.attendance.service;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import com.attendance.dao.SampleDao;
import com.attendance.vo.Sample;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

  private final SampleDao sampleDao;

  @Override
  public JSONObject sampleTest(String name, String phone) {
    JSONObject obj = new JSONObject();
    String code = "200";
    String message = "";

    if (name.equals("")) {
      code = "503";
      message = "필수 값이 없습니다.(이름)";
    }

    if (phone.equals("")) {
      code = "503";
      message = "필수 값이 없습니다.(이름)";
    }

    if (code.equals("200")) {
      Sample sample = Sample.builder().name(name).phone(phone).build();
      if (sampleDao.insertSample(sample) < 0) {
        code = "500";
        message = "다시 시도해 주십시오.";
      }
    }

    obj.put("code", code);
    obj.put("message", message);

    return obj;
  }

}
