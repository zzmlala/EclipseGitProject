package com.jt.manage.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;

@Service
public class FileUploadServiceImpl implements FileUploadService {
	
	@Value("${image.url}")
	private String urlDir;//定义虚拟路径
	
	@Value("${image.localpath}")
	private String filepath;//定义文件路径
	
	
	/**
	 * 1.如何保证上传的文件是图片
	 * 2.上传文件到E:/jt-upload
	 */
	@Override
	public void upload(MultipartFile file) {
		
		//1.获取文件上传的名称   abc.jpg
		String fileName = file.getOriginalFilename();
		
		//2.判断是否为图片
		if(fileName.matches("^.*(jpg|png|gif)$")){
			
			//3.创建文件夹
			File fileDir = new File("E:/jt-upload");
			
			//4.判断文件是否存在
			if(!fileDir.exists()){
				//文件不存在,则创建文件夹
				fileDir.mkdirs();
			}
			
			try {
				//5.实现文件上传
				file.transferTo(new File(fileDir.getPath()+"/"+fileName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 1.检验文件是否为图片
	 * 2.判断是否为恶意程序
	 * 3.由于文件的个数较多采用分文件存储的格式
	 * 4.为了避免文件重复 采用追加字符串的方式定义
	 * 5.实现文件上传
	 */
	@Override
	public PicUploadResult uploadFile(MultipartFile uploadFile) {
		PicUploadResult picUploadResult = 
				new PicUploadResult();
		
		//1.判断是否为图片
		String fileName = uploadFile.getOriginalFilename();
		
		if(!fileName.matches("^.*(png|jpg|gif)$")){
			picUploadResult.setError(1);//不是图片的类型
			return picUploadResult;
		}
		
		try {
			
		
		//2.判断是否为恶意程序
		BufferedImage bufferedImage = 
				ImageIO.read(uploadFile.getInputStream());
		
		//2.1获取宽度和高度
		int height = bufferedImage.getHeight();
		int width = bufferedImage.getWidth();
		
		if(height ==0 || width==0){
			picUploadResult.setError(1);//不是图片
			return picUploadResult;
		}
		
		
		
		String dateDir = 
				new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		
		
		//生成对应的文件夹 E:/jt-upload/2018/12/12
		String dirpath = filepath + dateDir;
		
		File file = new File(dirpath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		String uuidName = 
				UUID.randomUUID().toString().replace("-","");
		String randomNum = new Random().nextInt(999)+"";
		
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		
		//e:jt-upload/2018/06/12/uuid+random.jpg
		String fileDirPath = dirpath + "/" +uuidName +randomNum +fileType;
		
		//准备文件上传
		uploadFile.transferTo(new File(fileDirPath));
		
		//生成正确的页面回显信息
		picUploadResult.setHeight(height+"");
		picUploadResult.setWidth(width+"");
		
		/**
		 * 1.本地磁盘路径
		 * 	E:\jt-upload\2018\06\05\523.jpg
		 * 2.网络的虚拟路径
		 * 	image.jt.com\2018\06\05\523.jpg
		 */
		
		String urlPath = urlDir + dateDir +"/" +
				uuidName + randomNum + fileType;
		picUploadResult.setUrl(urlPath);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return picUploadResult;
	}
	
	
	
	
	
	
	
}
