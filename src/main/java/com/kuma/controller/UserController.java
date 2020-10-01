package com.kuma.controller;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kuma.model.BookModel;
import com.kuma.model.SignupForm;
import com.kuma.model.UserModel;
import com.kuma.model.ValidGroup;
import com.kuma.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	public UserModel currentUser(HttpServletRequest httpServletRequest) {
		String userId = httpServletRequest.getRemoteUser();
		UserModel user = userService.currentUser(userId);
		return user;
	}

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
	public String postSignup(@ModelAttribute @Validated(ValidGroup.class) SignupForm form
			,BindingResult bindingResult, Model model
			, @RequestParam("image") MultipartFile multipartFile) {
		if(bindingResult.hasErrors()) {
			return getSignUp(form, model);
		}
		UserModel user = new UserModel();
		user.setSelfId(form.getSelfId());
		user.setPassword(form.getPassword());
		user.setName(form.getName());
		// サービスクラスのinsertを呼び出して変換
		boolean result = userService.insert(user, multipartFile);
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

	@GetMapping("/loginTime")
	public String getTest(Model model, HttpServletRequest httpServletRequest) {
		userService.saveLoginTime(httpServletRequest.getRemoteUser());
		return "redirect:/bookList";
	}

	@GetMapping("/userList")
	public String getUserList(Model model) {
		model.addAttribute("contents", "user/userList :: userList_contents");
		List<UserModel> userList = userService.selectMany();
		model.addAttribute("userList", userList);
		return "/header";
	}

	@GetMapping("/userDetail/{id:.+}")
	public String getUserDetail(Model model, @PathVariable("id") int id, HttpServletRequest httpServletRequest) {
		model.addAttribute("contents", "user/userDetail :: userDetail_contents");
		if(String.valueOf(id).length() > 0) {
			UserModel user = userService.selectOne((int) id);
			if(user.equals(currentUser(httpServletRequest))) {
				return "redirect:/mypage";
			}
			model.addAttribute("user", user);
			List<BookModel> books = userService.hasBook(id);
			model.addAttribute("books", books);
		}
		return "/header";
	}

	@GetMapping("/mypage")
	public String getMyPage(Model model, HttpServletRequest httpServletRequest) {
		model.addAttribute("contents", "user/userDetail :: userDetail_contents");
		UserModel user = currentUser(httpServletRequest);
		model.addAttribute("user", user);
		List<BookModel> books = userService.hasBook(user.getId());
		model.addAttribute("books", books);
		return "/header";
	}

	@GetMapping("/userEdit/{id}")
	public String getUserEdit(@ModelAttribute SignupForm form
			, Model model, @PathVariable("id") int id, HttpServletRequest httpServletRequest) {
		UserModel currentUser = currentUser(httpServletRequest);
		UserModel user = userService.selectOne(id);
		if(String.valueOf(id).length() > 0) {
			if(currentUser.equals(user)) {
				form.setId(currentUser.getId());
				form.setSelfId(currentUser.getSelfId());
				form.setPassword(currentUser.getPassword());
				form.setName(currentUser.getName());
				model.addAttribute("contents", "user/userEdit :: userEdit_contents");
				model.addAttribute("signupForm", form);
				model.addAttribute("image", currentUser.getImage());
				return "/header";
			}
		}
		return "redirect:/mypage";
	}

	@PostMapping(value="/userEdit", params="update")
	public String postUserEdit(@ModelAttribute SignupForm form, Model model
			, @RequestParam("image") MultipartFile multipartFile) {
		UserModel user = new UserModel();
		user.setId(form.getId());
		user.setSelfId(form.getSelfId());
		user.setPassword(form.getPassword());
		user.setName(form.getName());
		if(multipartFile.isEmpty()) {
			String image = userService.selectOne(form.getId()).getImage();
			user.setImage(image);
		}
		try {
			boolean result = userService.updateOne(user, multipartFile);
			if(result == true) {
				model.addAttribute("result", "更新成功");
			} else {
				model.addAttribute("result", "更新失敗");
			}
		} catch(DataAccessException e) {
			model.addAttribute("result", "更新失敗(トランザクションテスト)");
		}
		return getUserList(model);
	}

	@PostMapping(value = "/userEdit", params = "delete")
	public String postUserDelete(@ModelAttribute SignupForm form, Model model, HttpServletRequest httpServletRequest) {
		boolean result = userService.deleteOne(form.getId());
		if(result == true) {
			model.addAttribute("result", "削除成功");
			try {
				httpServletRequest.logout();
			} catch (ServletException e) {
				e.printStackTrace();
			}
			return "redirect:/login";
		} else {
			model.addAttribute("result", "削除失敗");
		}
		return "redirect:/userList";
	}

	@PostMapping("/logout")
	public String postLogout() {
		// ログイン画面へリダイレクト
		return "redirect:/login";
	}
}
