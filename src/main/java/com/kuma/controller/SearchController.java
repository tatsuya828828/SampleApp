package com.kuma.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kuma.model.BookModel;
import com.kuma.service.UserService;

@Controller
public class SearchController {
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	UserService userService;

	@GetMapping("/search/{word}")
	public String getSearch(Model model, @RequestParam("word") String word) {
		model.addAttribute("contents", "search :: search_contents");
		model.addAttribute("word", word);
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM book WHERE title LIKE ?", "%"+ word +"%");
		int count = jdbc.queryForObject("SELECT COUNT(*) FROM book WHERE title LIKE "+"'%"+ word +"%'", Integer.class);
		List<Object> searchList = new ArrayList<>();
		for(Map<String, Object> map: getList) {
			BookModel book = new BookModel();
			book.setTitle((String) map.get("title"));
			book.setBody((String) map.get("body"));
			book.setUser(userService.selectOne((String)map.get("user_id")));
			searchList.add(book);
		}
		System.out.println(searchList);
		model.addAttribute("searches", searchList);
		model.addAttribute("count", count);
		return "/header";
	}
}
