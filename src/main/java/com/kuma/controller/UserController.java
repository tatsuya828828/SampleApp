package com.kuma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
		System.out.println(form);
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

	@GetMapping("/login")
	public String getLogin(Model model) {
		model.addAttribute("contents", "user/login :: login_contents");
		return "/header";
	}

	@PostMapping("/login")
	public String postLogin(Model model) {
		return "redirect:/userList";
	}

	@GetMapping("/mypage")
	public String getHome(Model model) {
		model.addAttribute("contents", "user/mypage :: mypage_contents");
		return "/header";
	}

	@GetMapping("/userList")
	public String getUserList(Model model) {
		model.addAttribute("contents", "user/userList :: userList_contents");
		List<UserModel> userList = userService.selectMany();
		model.addAttribute("userList", userList);
		return "/header";
	}

	@GetMapping("/userDetail/{id:.+}")
	public String getUserDetail(@ModelAttribute SignupForm form, Model model, @PathVariable("id") String userId) {
		model.addAttribute("contents", "user/userDetail :: userDetail_contents");
		if(userId != null && userId.length()>0) {
			UserModel user = userService.selectOne(userId);
			form.setUserId(user.getUserId());
			form.setPassword(user.getPassword());
			form.setName(user.getName());
			model.addAttribute("signupForm", form);
		}
		return "/header";
	}
}
