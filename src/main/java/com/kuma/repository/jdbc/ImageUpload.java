package com.kuma.repository.jdbc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.web.multipart.MultipartFile;

public class ImageUpload {
	public static String postImage(MultipartFile multipartFile, String id) {
		if(!multipartFile.isEmpty()) {
			try {
				String uploadPath = "src/main/resources/static/images/";
				byte[] bytes = multipartFile.getBytes();
				File file = new File(uploadPath+ id+"_"+ multipartFile.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(file));
				stream.write(bytes);
				stream.close();
				System.out.println("画像登録成功");
			} catch(Exception e) {
				System.out.println(e);
			}
			return "/images/"+ id +"_"+ multipartFile.getOriginalFilename();
		}
		return "/images/NOIMAGE.png";
	}
}
