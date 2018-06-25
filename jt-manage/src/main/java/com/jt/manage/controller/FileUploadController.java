package com.jt.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
import com.jt.manage.service.FileUploadService;

@Controller
public class FileUploadController {
	
	@Autowired
	private FileUploadService fileUploadService;
	
	
	@RequestMapping(value="/file")
	@ResponseBody
	public String file(MultipartFile file){
		
		fileUploadService.upload(file);
		
		return "文件上传成功!!!!";
	}
	
	
	//实现文件上传 
	// {"error":0,"url":"图片的保存路径","width":图片的宽度,"height":图片的高度}
	@RequestMapping("/pic/upload")
	@ResponseBody
	public PicUploadResult fileUpload(MultipartFile uploadFile){
		
		return fileUploadService.uploadFile(uploadFile);
	}
	
	
	
	
	
	
	
	
	
}
