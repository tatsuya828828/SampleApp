package com.kuma.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.kuma.model.SignupForm;
import com.kuma.model.UserModel;
import com.kuma.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String getTop(Model model) {
		model.addAttribute("contents", "top :: top_contents");
		return "/header";
	}

	@GetMapping("/signup")
	public String getSignUp(@ModelAttribute SignupForm form, Model model) {
		model.addAttribute("contents", "user/signup :: signup_contents");
		return "/header";
	}
	@PostMapping("/signup")
	public String postSignup(@ModelAttribute SignupForm form, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return getSignUp(form, model);
		}

		UserModel user = new UserModel();
		user.setUserId(form.getUserId());
		user.setPassword(form.getPassword());
		user.setName(form.getName());
		// サービスクラスのinsertを呼び出して変換
		boolean result = userService.insert(user);
		// 登録結果の判定
		if(result == true) {
			System.out.println("登録成功");
		} else {
			System.out.println("登録失敗");
		}
		return "redirect:/login";
	}
}
