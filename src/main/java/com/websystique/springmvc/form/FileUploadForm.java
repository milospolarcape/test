package com.websystique.springmvc.form;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadForm {

	
	MultipartFile files;

	public MultipartFile getFiles() {
		return files;
	}

	public void setFiles(MultipartFile files) {
		this.files = files;
	}

	
}
