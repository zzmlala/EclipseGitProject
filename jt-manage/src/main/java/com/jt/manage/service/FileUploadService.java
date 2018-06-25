package com.jt.manage.service;

import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;

public interface FileUploadService {

	void upload(MultipartFile file);

	PicUploadResult uploadFile(MultipartFile uploadFile);

}
