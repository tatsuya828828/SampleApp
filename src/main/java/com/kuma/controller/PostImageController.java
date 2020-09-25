package com.kuma.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PostImageController {
	@PostMapping("/imageUpload")
	public String postImageUpload(@RequestParam("image") MultipartFile multipartFile) {
		System.out.println("aiueo");
		System.out.println(multipartFile.getOriginalFilename());
		if(!multipartFile.isEmpty()) {
			try {
				String uploadPath = "src/main/resources/images/";
				byte[] bytes = multipartFile.getBytes();
				File file = new File(uploadPath+multipartFile.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(file));
				stream.write(bytes);
				stream.close();
				System.out.println("登録成功");
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		return "redirect:/";
	}
}
