package com.attendance.controller;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.attendance.service.SampleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sample")
@RequiredArgsConstructor
public class SampleController {

  private final SampleService sampleService;

  // 테스트
  @PostMapping("/test")
  public ResponseEntity<JSONObject> sampleTest(@RequestParam(name = "name") String name,
      @RequestParam(name = "phone") String phone) {
    JSONObject object = sampleService.sampleTest(name, phone);
    return ResponseEntity.ok(object);
  }

}
