package com.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberPageController {
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/loginForm")
	public String loginForm() {
		return "member/login";
	}
	
	@RequestMapping("/user/normal/pageDetail")
	public String pageDetail(@RequestParam(name="idx") String idx, Model model) {
		System.out.println("idx >>> " + idx);
		model.addAttribute("idx", idx);
		return "member/memberDetail";
	}

}
